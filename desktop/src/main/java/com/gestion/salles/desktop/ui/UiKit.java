package com.gestion.salles.desktop.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

public final class UiKit {

    public static final Color PRIMARY = new Color(15, 90, 94);
    public static final Color PRIMARY_DARK = new Color(10, 62, 66);
    public static final Color ACCENT = new Color(196, 123, 43);
    public static final Color BG = new Color(244, 247, 246);
    public static final Color CARD = Color.WHITE;
    public static final Color BORDER = new Color(214, 224, 222);
    public static final Color DANGER = new Color(176, 58, 58);
    public static final Color TEXT = new Color(32, 42, 45);
    public static final Color MUTED = new Color(90, 108, 110);

    private UiKit() {
    }

    public static Font font(int size, int style) {
        return new Font("Segoe UI", style, size);
    }

    public static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font(13, Font.PLAIN));
        label.setForeground(TEXT);
        return label;
    }

    public static JTextField field(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(font(13, Font.PLAIN));
        field.setMargin(new Insets(6, 8, 6, 8));
        return field;
    }

    public static JButton primaryButton(String text) {
        return coloredButton(text, PRIMARY, Color.WHITE);
    }

    public static JButton accentButton(String text) {
        return coloredButton(text, ACCENT, Color.WHITE);
    }

    public static JButton dangerButton(String text) {
        return coloredButton(text, DANGER, Color.WHITE);
    }

    public static JButton ghostButton(String text) {
        JButton button = new JButton(text);
        button.setFont(font(13, Font.BOLD));
        button.setForeground(PRIMARY_DARK);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private static JButton coloredButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(font(13, Font.BOLD));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        return button;
    }

    public static JPanel card(String title) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        JLabel heading = new JLabel(title);
        heading.setFont(font(16, Font.BOLD));
        heading.setForeground(PRIMARY_DARK);
        card.add(heading, BorderLayout.NORTH);
        return card;
    }

    public static void addFormRow(JPanel form, int row, String caption, Component field) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 8);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = row;
        form.add(label(caption), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        form.add(field, c);
    }

    public static JPanel formPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        return form;
    }

    public static JPanel buttonBar(JButton... buttons) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bar.setOpaque(false);
        for (JButton button : buttons) {
            bar.add(button);
        }
        return bar;
    }

    public static JScrollPane tableScroll(JTable table) {
        table.setRowHeight(28);
        table.setFont(font(13, Font.PLAIN));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(BORDER);
        table.setFillsViewportHeight(true);
        JTableHeader header = table.getTableHeader();
        header.setFont(font(13, Font.BOLD));
        header.setForeground(PRIMARY_DARK);
        header.setReorderingAllowed(false);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        table.setDefaultRenderer(Object.class, renderer);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.setPreferredSize(new Dimension(720, 280));
        return scroll;
    }

    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirmation",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static Window windowOf(Component c) {
        return javax.swing.SwingUtilities.getWindowAncestor(c);
    }
}
