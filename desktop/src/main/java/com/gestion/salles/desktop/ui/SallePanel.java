package com.gestion.salles.desktop.ui;

import com.gestion.salles.desktop.api.ApiClient;
import com.gestion.salles.desktop.model.Occupant;
import com.gestion.salles.desktop.model.Salle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.List;

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

        UiKit.addSalleActionsColumn(table, this::voirOccupations, this::ouvrirModification, this::supprimer);
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

    private void voirOccupations(int row) {
        String code = String.valueOf(model.getValueAt(row, 0));
        String designation = String.valueOf(model.getValueAt(row, 1));
        try {
            List<Occupant> filtres = api.listerOccupations().stream()
                    .filter(o -> code.equals(o.getCodesal()))
                    .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                    .toList();
            afficherOccupationsDialog(designation, filtres);
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private void afficherOccupationsDialog(String designation, List<Occupant> occupations) {
        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(UiKit.CARD);
        content.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Occupations — " + designation);
        title.setFont(UiKit.font(15, Font.BOLD));
        title.setForeground(UiKit.PRIMARY_DARK);
        content.add(title, BorderLayout.NORTH);

        if (occupations.isEmpty()) {
            JLabel empty = new JLabel("Aucune occupation enregistrée pour cette salle.");
            empty.setFont(UiKit.font(13, Font.PLAIN));
            empty.setForeground(UiKit.MUTED);
            empty.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));
            content.add(empty, BorderLayout.CENTER);
        } else {
            DefaultTableModel occModel = new DefaultTableModel(new Object[]{"Professeur", "Date"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (Occupant o : occupations) {
                occModel.addRow(new Object[]{o.getNomProf() + " " + o.getPrenomProf(), o.getDate()});
            }
            JTable occTable = new JTable(occModel);
            content.add(UiKit.tableScroll(occTable), BorderLayout.CENTER);
        }

        JButton fermer = UiKit.ghostButton("Fermer");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(fermer);
        content.add(buttons, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(UiKit.windowOf(this), "Occupations de la salle", Dialog.ModalityType.APPLICATION_MODAL);
        fermer.addActionListener(e -> dialog.dispose());
        dialog.setContentPane(content);
        dialog.setSize(440, occupations.isEmpty() ? 220 : 420);
        dialog.setLocationRelativeTo(UiKit.windowOf(this));
        dialog.setResizable(false);
        dialog.setVisible(true);
    }
}