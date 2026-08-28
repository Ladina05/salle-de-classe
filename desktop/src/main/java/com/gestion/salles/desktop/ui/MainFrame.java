package com.gestion.salles.desktop.ui;

import com.gestion.salles.desktop.api.ApiClient;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public class MainFrame extends JFrame {

    private static final String CARD_PROFS = "profs";
    private static final String CARD_SALLES = "salles";
    private static final String CARD_OCCUPATIONS = "occupations";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    private JButton navProfs;
    private JButton navSalles;
    private JButton navOccupations;

    private ProfPanel profPanel;
    private SallePanel sallePanel;
    private OccupantPanel occupantPanel;

    public MainFrame() {
        super("Gestion des salles de classe");
        ApiClient api = new ApiClient();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiKit.BG);
        root.add(header(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 16));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        center.add(navBar(), BorderLayout.NORTH);

        profPanel = new ProfPanel(api);
        sallePanel = new SallePanel(api);
        occupantPanel = new OccupantPanel(api);

        cards.setOpaque(false);
        cards.add(profPanel, CARD_PROFS);
        cards.add(sallePanel, CARD_SALLES);
        cards.add(occupantPanel, CARD_OCCUPATIONS);

        center.add(cards, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        setContentPane(root);

        selectTab(CARD_PROFS);
    }

    private JPanel navBar() {
        JPanel bar = new JPanel(new GridLayout(1, 3, 0, 0));
        bar.setBackground(UiKit.CARD);
        bar.setBorder(BorderFactory.createLineBorder(UiKit.BORDER));

        navProfs = UiKit.navButton("Professeurs");
        navSalles = UiKit.navButton("Salles");
        navOccupations = UiKit.navButton("Occupations");

        navProfs.addActionListener(e -> selectTab(CARD_PROFS));
        navSalles.addActionListener(e -> selectTab(CARD_SALLES));
        navOccupations.addActionListener(e -> selectTab(CARD_OCCUPATIONS));

        bar.add(navProfs);
        bar.add(navSalles);
        bar.add(navOccupations);
        return bar;
    }

    private void selectTab(String card) {
        cardLayout.show(cards, card);
        UiKit.setNavSelected(navProfs, CARD_PROFS.equals(card));
        UiKit.setNavSelected(navSalles, CARD_SALLES.equals(card));
        UiKit.setNavSelected(navOccupations, CARD_OCCUPATIONS.equals(card));

        if (CARD_PROFS.equals(card)) {
            profPanel.recharger();
        } else if (CARD_SALLES.equals(card)) {
            sallePanel.recharger();
        } else {
            occupantPanel.recharger();
        }
    }

    private JPanel header() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiKit.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        JLabel title = new JLabel("Gestion des salles de classe");
        title.setForeground(Color.WHITE);
        title.setFont(UiKit.font(23, Font.BOLD));

        JPanel texts = new JPanel(new BorderLayout(0, 5));
        texts.setOpaque(false);
        texts.add(title, BorderLayout.NORTH);
        header.add(texts, BorderLayout.WEST);
        return header;
    }
}