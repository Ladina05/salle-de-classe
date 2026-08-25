package com.gestion.salles.desktop.ui;

import com.gestion.salles.desktop.api.ApiClient;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Gestion des salles de classe");
        ApiClient api = new ApiClient();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 680));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiKit.BG);
        root.add(header(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UiKit.font(14, Font.BOLD));
        tabs.setBackground(UiKit.BG);
        tabs.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));

        ProfPanel profPanel = new ProfPanel(api);
        SallePanel sallePanel = new SallePanel(api);
        OccupantPanel occupantPanel = new OccupantPanel(api);

        tabs.addTab("Professeurs", profPanel);
        tabs.addTab("Salles", sallePanel);
        tabs.addTab("Occupations", occupantPanel);

        tabs.addChangeListener(e -> {
            int index = tabs.getSelectedIndex();
            if (index == 0) {
                profPanel.recharger();
            } else if (index == 1) {
                sallePanel.recharger();
            } else if (index == 2) {
                occupantPanel.recharger();
            }
        });

        root.add(tabs, BorderLayout.CENTER);
        setContentPane(root);
        profPanel.recharger();
    }

    private JPanel header() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiKit.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel("Gestion des salles de classe");
        title.setForeground(Color.WHITE);
        title.setFont(UiKit.font(22, Font.BOLD));

        JLabel subtitle = new JLabel("Professeurs  ·  Salles  ·  Occupations    |    API Spring Boot  ·  PostgreSQL");
        subtitle.setForeground(new Color(210, 232, 230));
        subtitle.setFont(UiKit.font(12, Font.PLAIN));

        JPanel texts = new JPanel(new BorderLayout(0, 4));
        texts.setOpaque(false);
        texts.add(title, BorderLayout.NORTH);
        texts.add(subtitle, BorderLayout.SOUTH);
        header.add(texts, BorderLayout.WEST);
        return header;
    }
}
