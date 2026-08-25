package com.gestion.salles.desktop;

import com.gestion.salles.desktop.ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class DesktopApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Nimbus / Metal en secours
            }
            new MainFrame().setVisible(true);
        });
    }
}
