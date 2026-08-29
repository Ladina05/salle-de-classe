package com.gestion.salles.desktop.ui;

import com.gestion.salles.desktop.api.ApiClient;
import com.gestion.salles.desktop.model.Occupant;
import com.gestion.salles.desktop.model.Prof;
import com.gestion.salles.desktop.model.Salle;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

public class OccupantPanel extends JPanel {

    private final ApiClient api;
    private final DefaultTableModel model;
    private final JTable table;
    private List<Occupant> occupationsCourantes = new ArrayList<>();

    public OccupantPanel(ApiClient api) {
        super(new BorderLayout(12, 12));
        this.api = api;
        setOpaque(false);

        model = new DefaultTableModel(
                new Object[]{"N°", "Professeur", "Salle", "Date", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        table = new JTable(model);

        JPanel card = UiKit.card("Liste des occupations");
        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setOpaque(false);

        JButton ajouter = UiKit.primaryButton("Ajouter", new UiKit.PlusIcon(Color.WHITE, 13));
        JButton actualiser = UiKit.ghostButton("Actualiser");
        ajouter.addActionListener(e -> ouvrirFormulaire(null));
        actualiser.addActionListener(e -> recharger());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        toolbar.setOpaque(false);
        toolbar.add(actualiser);
        toolbar.add(ajouter);

        content.add(toolbar, BorderLayout.NORTH);
        content.add(UiKit.tableScroll(table), BorderLayout.CENTER);
        card.add(content, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        UiKit.addActionsColumn(table, this::ouvrirModification, this::supprimer);
    }

    public void recharger() {
        try {
            occupationsCourantes = api.listerOccupations();
            afficher(occupationsCourantes);
        } catch (Exception ex) {
            UiKit.error(this, "Impossible de charger les occupations.\n" + ex.getMessage());
        }
    }

    private void afficher(List<Occupant> occupants) {
        model.setRowCount(0);
        for (Occupant o : occupants) {
            model.addRow(new Object[]{
                    o.getId(),
                    o.getNomProf() + " " + o.getPrenomProf(),
                    o.getDesignationSalle(),
                    o.getDate(),
                    ""
            });
        }
    }

    private void ouvrirFormulaire(Occupant existant) {
        JComboBox<Prof> profCombo = new JComboBox<>();
        JComboBox<Salle> salleCombo = new JComboBox<>();
        DatePicker datePicker = UiKit.datePicker();

        try {
            for (Prof p : api.listerProfs()) {
                profCombo.addItem(p);
            }
            for (Salle s : api.listerSalles()) {
                salleCombo.addItem(s);
            }
        } catch (Exception ex) {
            UiKit.error(this, "Impossible de charger les professeurs/salles.\n" + ex.getMessage());
            return;
        }

        boolean modification = existant != null;
        if (modification) {
            selectProf(profCombo, existant.getCodeprof());
            selectSalle(salleCombo, existant.getCodesal());
            datePicker.setDate(existant.getDate());
        } else {
            profCombo.setSelectedIndex(-1);
            salleCombo.setSelectedIndex(-1);
        }

        JPanel form = UiKit.formPanel();
        UiKit.addFormRow(form, 0, "Professeur", profCombo);
        UiKit.addFormRow(form, 1, "Salle", salleCombo);
        UiKit.addFormRow(form, 2, "Date", datePicker);

        Window owner = UiKit.windowOf(this);
        JDialog dialog = UiKit.formDialog(owner,
                modification ? "Modifier l'occupation" : "Ajouter une occupation", form, () -> {
                    Prof prof = (Prof) profCombo.getSelectedItem();
                    Salle salle = (Salle) salleCombo.getSelectedItem();
                    if (prof == null || salle == null || datePicker.getDate() == null) {
                        UiKit.error(this, "Choisissez un professeur, une salle et une date.");
                        return false;
                    }
                    Occupant occupant = new Occupant();
                    occupant.setCodeprof(prof.getCodeprof());
                    occupant.setCodesal(salle.getCodesal());
                    occupant.setDate(datePicker.getDate());
                    try {
                        if (modification) {
                            api.modifierOccupation(existant.getId(), occupant);
                            UiKit.info(this, "Occupation modifiée.");
                        } else {
                            api.creerOccupation(occupant);
                            UiKit.info(this, "Occupation ajoutée.");
                        }
                        recharger();
                        return true;
                    } catch (Exception ex) {
                        UiKit.error(this, ex.getMessage());
                        return false;
                    }
                });
        dialog.setVisible(true);
    }

    private void ouvrirModification(int row) {
        if (row < 0 || row >= occupationsCourantes.size()) {
            return;
        }
        ouvrirFormulaire(occupationsCourantes.get(row));
    }

    private void supprimer(int row) {
        if (row < 0 || row >= occupationsCourantes.size()) {
            return;
        }
        Long id = occupationsCourantes.get(row).getId();
        if (!UiKit.confirm(this, "Supprimer cette occupation ?")) {
            return;
        }
        try {
            api.supprimerOccupation(id);
            recharger();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private void selectProf(JComboBox<Prof> combo, String code) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (code.equals(combo.getItemAt(i).getCodeprof())) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selectSalle(JComboBox<Salle> combo, String code) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (code.equals(combo.getItemAt(i).getCodesal())) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }
}