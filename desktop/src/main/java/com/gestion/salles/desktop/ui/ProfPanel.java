package com.gestion.salles.desktop.ui;

import com.gestion.salles.desktop.api.ApiClient;
import com.gestion.salles.desktop.model.Prof;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;

public class ProfPanel extends JPanel {

    private final ApiClient api;
    private final JTextField codeField = UiKit.field(12);
    private final JTextField nomField = UiKit.field(16);
    private final JTextField prenomField = UiKit.field(16);
    private final JTextField gradeField = UiKit.field(16);
    private final JTextField searchField = UiKit.field(22);
    private final DefaultTableModel model;
    private final JTable table;

    public ProfPanel(ApiClient api) {
        super(new BorderLayout(12, 12));
        this.api = api;
        setOpaque(false);

        model = new DefaultTableModel(new Object[]{"Code", "Nom", "Prénom", "Grade"}, 0) {
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

        JPanel card = UiKit.card("CRUD professeurs  —  recherche par code ou nom");
        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setOpaque(false);

        JPanel form = UiKit.formPanel();
        UiKit.addFormRow(form, 0, "Code prof", codeField);
        UiKit.addFormRow(form, 1, "Nom", nomField);
        UiKit.addFormRow(form, 2, "Prénom", prenomField);
        UiKit.addFormRow(form, 3, "Grade", gradeField);

        JButton ajouter = UiKit.primaryButton("Ajouter");
        JButton modifier = UiKit.accentButton("Modifier");
        JButton supprimer = UiKit.dangerButton("Supprimer");
        JButton vider = UiKit.ghostButton("Vider le formulaire");
        JButton rechercher = UiKit.primaryButton("Rechercher");
        JButton tous = UiKit.ghostButton("Tous");

        ajouter.addActionListener(e -> ajouter());
        modifier.addActionListener(e -> modifier());
        supprimer.addActionListener(e -> supprimer());
        vider.addActionListener(e -> viderFormulaire());
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
        north.add(form, BorderLayout.CENTER);
        north.add(UiKit.buttonBar(ajouter, modifier, supprimer, vider), BorderLayout.SOUTH);

        content.add(north, BorderLayout.NORTH);
        content.add(UiKit.tableScroll(table), BorderLayout.CENTER);
        content.add(searchRow, BorderLayout.SOUTH);
        card.add(content, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
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
            model.addRow(new Object[]{p.getCodeprof(), p.getNom(), p.getPrenom(), p.getGrade()});
        }
    }

    private void ajouter() {
        try {
            api.creerProf(lireFormulaire(true));
            UiKit.info(this, "Professeur ajouté.");
            recharger();
            viderFormulaire();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private void modifier() {
        if (table.getSelectedRow() < 0) {
            UiKit.error(this, "Sélectionnez un professeur à modifier.");
            return;
        }
        try {
            String code = String.valueOf(model.getValueAt(table.getSelectedRow(), 0));
            Prof prof = lireFormulaire(false);
            prof.setCodeprof(code);
            api.modifierProf(code, prof);
            UiKit.info(this, "Professeur modifié.");
            recharger();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private void supprimer() {
        if (table.getSelectedRow() < 0) {
            UiKit.error(this, "Sélectionnez un professeur à supprimer.");
            return;
        }
        String code = String.valueOf(model.getValueAt(table.getSelectedRow(), 0));
        if (!UiKit.confirm(this, "Supprimer le professeur " + code + " ?")) {
            return;
        }
        try {
            api.supprimerProf(code);
            recharger();
            viderFormulaire();
        } catch (Exception ex) {
            UiKit.error(this, ex.getMessage());
        }
    }

    private Prof lireFormulaire(boolean avecCode) {
        String code = codeField.getText().trim();
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String grade = gradeField.getText().trim();
        if ((avecCode && code.isEmpty()) || nom.isEmpty() || prenom.isEmpty() || grade.isEmpty()) {
            throw new IllegalArgumentException("Tous les champs du professeur sont obligatoires.");
        }
        return new Prof(code, nom, prenom, grade);
    }

    private void remplirDepuisSelection() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        codeField.setText(String.valueOf(model.getValueAt(row, 0)));
        nomField.setText(String.valueOf(model.getValueAt(row, 1)));
        prenomField.setText(String.valueOf(model.getValueAt(row, 2)));
        gradeField.setText(String.valueOf(model.getValueAt(row, 3)));
        codeField.setEditable(false);
    }

    private void viderFormulaire() {
        table.clearSelection();
        codeField.setText("");
        nomField.setText("");
        prenomField.setText("");
        gradeField.setText("");
        codeField.setEditable(true);
    }
}
