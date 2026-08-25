package com.gestion.salles.desktop.ui;

import com.gestion.salles.desktop.api.ApiClient;
import com.gestion.salles.desktop.model.Salle;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

public class SallePanel extends JPanel {

    private final ApiClient api;
    private final JTextField codeField = UiKit.field(12);
    private final JTextField designationField = UiKit.field(24);
    private final DefaultTableModel model;
    private final JTable table;

    public SallePanel(ApiClient api) {
        super(new BorderLayout(12, 12));
        this.api = api;
        setOpaque(false);

        model = new DefaultTableModel(new Object[]{"Code salle", "Désignation"}, 0) {
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

        JPanel card = UiKit.card("CRUD salles");
        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setOpaque(false);

        JPanel form = UiKit.formPanel();
        UiKit.addFormRow(form, 0, "Code salle", codeField);
        UiKit.addFormRow(form, 1, "Désignation", designationField);

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
            afficher(api.listerSalles());
        } catch (Exception ex) {
            UiKit.error(this, "Impossible de charger les salles.\n" + ex.getMessage());
        }
    }

    private void afficher(List<Salle> salles) {
        model.setRowCount(0);
        for (Salle s : salles) {
            model.addRow(new Object[]{s.getCodesal(), s.getDesignation()});
        }
    }

    private void ajouter() {
        try {
            api.creerSalle(lireFormulaire(true));
            UiKit.info(this, "Salle ajoutée.");
            recharger();
            viderFormulaire();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private void modifier() {
        if (table.getSelectedRow() < 0) {
            UiKit.error(this, "Sélectionnez une salle à modifier.");
            return;
        }
        try {
            String code = String.valueOf(model.getValueAt(table.getSelectedRow(), 0));
            Salle salle = lireFormulaire(false);
            salle.setCodesal(code);
            api.modifierSalle(code, salle);
            UiKit.info(this, "Salle modifiée.");
            recharger();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private void supprimer() {
        if (table.getSelectedRow() < 0) {
            UiKit.error(this, "Sélectionnez une salle à supprimer.");
            return;
        }
        String code = String.valueOf(model.getValueAt(table.getSelectedRow(), 0));
        if (!UiKit.confirm(this, "Supprimer la salle " + code + " ?")) {
            return;
        }
        try {
            api.supprimerSalle(code);
            recharger();
            viderFormulaire();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private Salle lireFormulaire(boolean avecCode) {
        String code = codeField.getText().trim();
        String designation = designationField.getText().trim();
        if ((avecCode && code.isEmpty()) || designation.isEmpty()) {
            throw new IllegalArgumentException("Le code et la désignation sont obligatoires.");
        }
        return new Salle(code, designation);
    }

    private void remplirDepuisSelection() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        codeField.setText(String.valueOf(model.getValueAt(row, 0)));
        designationField.setText(String.valueOf(model.getValueAt(row, 1)));
        codeField.setEditable(false);
    }

    private void viderFormulaire() {
        table.clearSelection();
        codeField.setText("");
        designationField.setText("");
        codeField.setEditable(true);
    }
}
