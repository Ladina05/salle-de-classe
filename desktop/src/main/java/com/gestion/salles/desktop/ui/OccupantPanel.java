package com.gestion.salles.desktop.ui;

import com.gestion.salles.desktop.api.ApiClient;
import com.gestion.salles.desktop.model.Occupant;
import com.gestion.salles.desktop.model.Prof;
import com.gestion.salles.desktop.model.Salle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class OccupantPanel extends JPanel {

    private final ApiClient api;
    private final JComboBox<Prof> profCombo = new JComboBox<>();
    private final JComboBox<Salle> salleCombo = new JComboBox<>();
    private final JSpinner dateSpinner;
    private final DefaultTableModel model;
    private final JTable table;

    public OccupantPanel(ApiClient api) {
        super(new BorderLayout(12, 12));
        this.api = api;
        setOpaque(false);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));

        model = new DefaultTableModel(
                new Object[]{"Id", "Code prof", "Professeur", "Code salle", "Salle", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                remplirDepuisSelection();
            }
        });

        JPanel card = UiKit.card("CRUD occupations (OCCUPER)");
        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setOpaque(false);

        JPanel form = UiKit.formPanel();
        UiKit.addFormRow(form, 0, "Professeur", profCombo);
        UiKit.addFormRow(form, 1, "Salle", salleCombo);
        UiKit.addFormRow(form, 2, "Date", dateSpinner);

        JButton ajouter = UiKit.primaryButton("Ajouter");
        JButton modifier = UiKit.accentButton("Modifier");
        JButton supprimer = UiKit.dangerButton("Supprimer");
        JButton vider = UiKit.ghostButton("Vider le formulaire");
        JButton actualiser = UiKit.ghostButton("Actualiser");

        ajouter.addActionListener(e -> ajouter());
        modifier.addActionListener(e -> modifier());
        supprimer.addActionListener(e -> supprimer());
        vider.addActionListener(e -> viderFormulaire());
        actualiser.addActionListener(e -> recharger());

        JPanel north = new JPanel(new BorderLayout(0, 10));
        north.setOpaque(false);
        north.add(form, BorderLayout.CENTER);
        north.add(UiKit.buttonBar(ajouter, modifier, supprimer, vider, actualiser), BorderLayout.SOUTH);

        content.add(north, BorderLayout.NORTH);
        content.add(UiKit.tableScroll(table), BorderLayout.CENTER);
        card.add(content, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    public void recharger() {
        try {
            chargerCombos();
            afficher(api.listerOccupations());
        } catch (Exception ex) {
            UiKit.error(this, "Impossible de charger les occupations.\n" + ex.getMessage());
        }
    }

    private void chargerCombos() throws Exception {
        Prof selectedProf = (Prof) profCombo.getSelectedItem();
        Salle selectedSalle = (Salle) salleCombo.getSelectedItem();
        profCombo.removeAllItems();
        salleCombo.removeAllItems();
        for (Prof p : api.listerProfs()) {
            profCombo.addItem(p);
        }
        for (Salle s : api.listerSalles()) {
            salleCombo.addItem(s);
        }
        if (selectedProf != null) {
            selectProf(selectedProf.getCodeprof());
        }
        if (selectedSalle != null) {
            selectSalle(selectedSalle.getCodesal());
        }
    }

    private void afficher(List<Occupant> occupants) {
        model.setRowCount(0);
        for (Occupant o : occupants) {
            model.addRow(new Object[]{
                    o.getId(),
                    o.getCodeprof(),
                    o.getNomProf() + " " + o.getPrenomProf(),
                    o.getCodesal(),
                    o.getDesignationSalle(),
                    o.getDate()
            });
        }
    }

    private void ajouter() {
        try {
            api.creerOccupation(lireFormulaire());
            UiKit.info(this, "Occupation ajoutée.");
            recharger();
            viderFormulaire();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private void modifier() {
        Long id = idSelection();
        if (id == null) {
            UiKit.error(this, "Sélectionnez une occupation à modifier.");
            return;
        }
        try {
            api.modifierOccupation(id, lireFormulaire());
            UiKit.info(this, "Occupation modifiée.");
            recharger();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private void supprimer() {
        Long id = idSelection();
        if (id == null) {
            UiKit.error(this, "Sélectionnez une occupation à supprimer.");
            return;
        }
        if (!UiKit.confirm(this, "Supprimer cette occupation ?")) {
            return;
        }
        try {
            api.supprimerOccupation(id);
            recharger();
            viderFormulaire();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private Occupant lireFormulaire() {
        Prof prof = (Prof) profCombo.getSelectedItem();
        Salle salle = (Salle) salleCombo.getSelectedItem();
        if (prof == null || salle == null) {
            throw new IllegalArgumentException("Choisissez un professeur et une salle.");
        }
        Occupant o = new Occupant();
        o.setCodeprof(prof.getCodeprof());
        o.setCodesal(salle.getCodesal());
        o.setDate(toLocalDate((Date) dateSpinner.getValue()));
        return o;
    }

    private void remplirDepuisSelection() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        selectProf(String.valueOf(model.getValueAt(row, 1)));
        selectSalle(String.valueOf(model.getValueAt(row, 3)));
        Object dateValue = model.getValueAt(row, 5);
        if (dateValue instanceof LocalDate date) {
            dateSpinner.setValue(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
    }

    private void viderFormulaire() {
        table.clearSelection();
        if (profCombo.getItemCount() > 0) {
            profCombo.setSelectedIndex(0);
        }
        if (salleCombo.getItemCount() > 0) {
            salleCombo.setSelectedIndex(0);
        }
        dateSpinner.setValue(new Date());
    }

    private Long idSelection() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return null;
        }
        return ((Number) model.getValueAt(row, 0)).longValue();
    }

    private void selectProf(String code) {
        for (int i = 0; i < profCombo.getItemCount(); i++) {
            if (code.equals(profCombo.getItemAt(i).getCodeprof())) {
                profCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selectSalle(String code) {
        for (int i = 0; i < salleCombo.getItemCount(); i++) {
            if (code.equals(salleCombo.getItemAt(i).getCodesal())) {
                salleCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
