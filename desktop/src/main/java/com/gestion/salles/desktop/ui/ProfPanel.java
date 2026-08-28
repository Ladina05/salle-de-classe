package com.gestion.salles.desktop.ui;

import com.gestion.salles.desktop.api.ApiClient;
import com.gestion.salles.desktop.model.Prof;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;
import java.awt.Color;

public class ProfPanel extends JPanel {

    private final ApiClient api;
    private final JTextField searchField = UiKit.field(22);
    private final DefaultTableModel model;
    private final JTable table;

    public ProfPanel(ApiClient api) {
        super(new BorderLayout(12, 12));
        this.api = api;
        setOpaque(false);

        model = new DefaultTableModel(new Object[]{"Code", "Nom", "Prénom", "Grade", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        table = new JTable(model);

        JPanel card = UiKit.card("Liste des professeurs");
        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setOpaque(false);

        // Bouton Ajouter : au-dessus de la barre de recherche, aligné à droite
        JButton ajouter = UiKit.primaryButton("Ajouter", new UiKit.PlusIcon(Color.WHITE, 13));
        ajouter.addActionListener(e -> ouvrirFormulaire(null));
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.add(ajouter, BorderLayout.EAST);

        // Barre de recherche
        JButton rechercher = UiKit.primaryButton("Rechercher", new UiKit.SearchIcon(Color.WHITE, 13));
        JButton tous = UiKit.ghostButton("Tous");
        rechercher.addActionListener(e -> rechercher());
        tous.addActionListener(e -> recharger());
        searchField.addActionListener(e -> rechercher());

        JPanel searchRow = UiKit.formPanel();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 8);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        searchRow.add(UiKit.label("Recherche (code ou nom)"), c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        searchRow.add(searchField, c);
        c.gridx = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        searchRow.add(rechercher, c);
        c.gridx = 3;
        searchRow.add(tous, c);

        JPanel north = new JPanel(new BorderLayout(0, 10));
        north.setOpaque(false);
        north.add(topBar, BorderLayout.NORTH);
        north.add(searchRow, BorderLayout.SOUTH);

        content.add(north, BorderLayout.NORTH);
        content.add(UiKit.tableScroll(table), BorderLayout.CENTER);
        card.add(content, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        UiKit.addActionsColumn(table, this::ouvrirModification, this::supprimer);
    }

    public void recharger() {
        try {
            afficher(api.listerProfs());
        } catch (Exception ex) {
            UiKit.error(this, "Impossible de charger les professeurs.\n" + ex.getMessage()
                    + "\nVérifiez que l'API Spring Boot est démarrée (port 8080).");
        }
    }

    private void rechercher() {
        String terme = searchField.getText().trim();
        try {
            afficher(terme.isEmpty() ? api.listerProfs() : api.rechercherProfs(terme));
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private void afficher(List<Prof> profs) {
        model.setRowCount(0);
        for (Prof p : profs) {
            model.addRow(new Object[]{p.getCodeprof(), p.getNom(), p.getPrenom(), p.getGrade(), ""});
        }
    }

    private void ouvrirFormulaire(Prof existant) {
        JTextField codeField = UiKit.field(16);
        JTextField nomField = UiKit.field(16);
        JTextField prenomField = UiKit.field(16);
        JTextField gradeField = UiKit.field(16);

        boolean modification = existant != null;
        if (modification) {
            codeField.setText(existant.getCodeprof());
            codeField.setEditable(false);
            nomField.setText(existant.getNom());
            prenomField.setText(existant.getPrenom());
            gradeField.setText(existant.getGrade());
        }

        JPanel form = UiKit.formPanel();
        UiKit.addFormRow(form, 0, "Code prof", codeField);
        UiKit.addFormRow(form, 1, "Nom", nomField);
        UiKit.addFormRow(form, 2, "Prénom", prenomField);
        UiKit.addFormRow(form, 3, "Grade", gradeField);

        Window owner = UiKit.windowOf(this);
        JDialog dialog = UiKit.formDialog(owner,
                modification ? "Modifier le professeur" : "Ajouter un professeur", form, () -> {
                    String code = codeField.getText().trim();
                    String nom = nomField.getText().trim();
                    String prenom = prenomField.getText().trim();
                    String grade = gradeField.getText().trim();
                    if (code.isEmpty() || nom.isEmpty() || prenom.isEmpty() || grade.isEmpty()) {
                        UiKit.error(this, "Tous les champs du professeur sont obligatoires.");
                        return false;
                    }
                    try {
                        Prof prof = new Prof(code, nom, prenom, grade);
                        if (modification) {
                            api.modifierProf(code, prof);
                            UiKit.info(this, "Professeur modifié.");
                        } else {
                            api.creerProf(prof);
                            UiKit.info(this, "Professeur ajouté.");
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
        Prof prof = new Prof(
                String.valueOf(model.getValueAt(row, 0)),
                String.valueOf(model.getValueAt(row, 1)),
                String.valueOf(model.getValueAt(row, 2)),
                String.valueOf(model.getValueAt(row, 3))
        );
        ouvrirFormulaire(prof);
    }

    private void supprimer(int row) {
        String code = String.valueOf(model.getValueAt(row, 0));
        if (!UiKit.confirm(this, "Supprimer le professeur " + code + " ?")) {
            return;
        }
        try {
            api.supprimerProf(code);
            recharger();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }
}