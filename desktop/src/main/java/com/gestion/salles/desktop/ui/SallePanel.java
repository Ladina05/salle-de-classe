package com.gestion.salles.desktop.ui;

import com.gestion.salles.desktop.api.ApiClient;
import com.gestion.salles.desktop.model.Salle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.List;
import java.awt.Color;

public class SallePanel extends JPanel {

    private final ApiClient api;
    private final DefaultTableModel model;
    private final JTable table;

    public SallePanel(ApiClient api) {
        super(new BorderLayout(12, 12));
        this.api = api;
        setOpaque(false);

        model = new DefaultTableModel(new Object[]{"Code salle", "Désignation", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        table = new JTable(model);

        JPanel card = UiKit.card("Liste des salles");
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
            afficher(api.listerSalles());
        } catch (Exception ex) {
            UiKit.error(this, "Impossible de charger les salles.\n" + ex.getMessage());
        }
    }

    private void afficher(List<Salle> salles) {
        model.setRowCount(0);
        for (Salle s : salles) {
            model.addRow(new Object[]{s.getCodesal(), s.getDesignation(), ""});
        }
    }

    private void ouvrirFormulaire(Salle existant) {
        JTextField codeField = UiKit.field(16);
        JTextField designationField = UiKit.field(24);

        boolean modification = existant != null;
        if (modification) {
            codeField.setText(existant.getCodesal());
            codeField.setEditable(false);
            designationField.setText(existant.getDesignation());
        }

        JPanel form = UiKit.formPanel();
        UiKit.addFormRow(form, 0, "Code salle", codeField);
        UiKit.addFormRow(form, 1, "Désignation", designationField);

        Window owner = UiKit.windowOf(this);
        JDialog dialog = UiKit.formDialog(owner,
                modification ? "Modifier la salle" : "Ajouter une salle", form, () -> {
                    String code = codeField.getText().trim();
                    String designation = designationField.getText().trim();
                    if (code.isEmpty() || designation.isEmpty()) {
                        UiKit.error(this, "Le code et la désignation sont obligatoires.");
                        return false;
                    }
                    try {
                        Salle salle = new Salle(code, designation);
                        if (modification) {
                            api.modifierSalle(code, salle);
                            UiKit.info(this, "Salle modifiée.");
                        } else {
                            api.creerSalle(salle);
                            UiKit.info(this, "Salle ajoutée.");
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
        Salle salle = new Salle(
                String.valueOf(model.getValueAt(row, 0)),
                String.valueOf(model.getValueAt(row, 1))
        );
        ouvrirFormulaire(salle);
    }

    private void supprimer(int row) {
        String code = String.valueOf(model.getValueAt(row, 0));
        if (!UiKit.confirm(this, "Supprimer la salle " + code + " ?")) {
            return;
        }
        try {
            api.supprimerSalle(code);
            recharger();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }
}