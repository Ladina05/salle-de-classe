package com.gestion.salles.desktop.ui;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.SwingConstants;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public final class UiKit {

    // Palette "professionnelle" : bleu marine profond + accents neutres
    public static final Color PRIMARY = new Color(30, 58, 95);
    public static final Color PRIMARY_DARK = new Color(18, 38, 63);
    public static final Color PRIMARY_LIGHT = new Color(226, 233, 244);
    public static final Color ACCENT = new Color(184, 138, 47);
    public static final Color BG = new Color(243, 245, 248);
    public static final Color CARD = Color.WHITE;
    public static final Color BORDER = new Color(226, 230, 236);
    public static final Color DANGER = new Color(190, 60, 60);
    public static final Color DANGER_LIGHT = new Color(252, 228, 228);
    public static final Color TEXT = new Color(28, 33, 43);
    public static final Color MUTED = new Color(110, 118, 132);

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
        return new RoundedButton(text, null, PRIMARY, Color.WHITE, 8);
    }

    public static JButton primaryButton(String text, Icon icon) {
        RoundedButton b = new RoundedButton(text, icon, PRIMARY, Color.WHITE, 8);
        b.setIconTextGap(8);
        return b;
    }

    public static JButton accentButton(String text) {
        return new RoundedButton(text, null, ACCENT, Color.WHITE, 8);
    }

    public static JButton dangerButton(String text) {
        return new RoundedButton(text, null, DANGER, Color.WHITE, 8);
    }

    public static JButton ghostButton(String text) {
        JButton button = new JButton(text);
        button.setFont(font(13, Font.BOLD));
        button.setForeground(PRIMARY_DARK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        return button;
    }

    public static JButton ghostButton(String text, Icon icon) {
        JButton button = ghostButton(text);
        button.setIcon(icon);
        button.setIconTextGap(8);
        return button;
    }

    /** Bouton de la barre de navigation (occupe toute la largeur disponible via GridLayout parent). */
    public static JButton navButton(String text) {
        JButton button = new JButton(text);
        button.setFont(font(15, Font.BOLD));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(18, 12, 18, 12));
        button.putClientProperty("selected", Boolean.FALSE);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!Boolean.TRUE.equals(button.getClientProperty("selected"))) {
                    button.setBackground(PRIMARY_LIGHT);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!Boolean.TRUE.equals(button.getClientProperty("selected"))) {
                    button.setBackground(CARD);
                }
            }
        });
        setNavSelected(button, false);
        return button;
    }

    public static void setNavSelected(JButton button, boolean selected) {
        button.putClientProperty("selected", selected);
        if (selected) {
            button.setBackground(PRIMARY);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(CARD);
            button.setForeground(MUTED);
        }
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
        table.setRowHeight(38);
        table.setFont(font(13, Font.PLAIN));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(BORDER);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new HeaderCellRenderer());

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        table.setDefaultRenderer(Object.class, renderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.setPreferredSize(new Dimension(720, 320));
        return scroll;
    }

    /** Renderer d'en-tête : texte centré, gras, et ligne de séparation en bas. */
    private static class HeaderCellRenderer extends DefaultTableCellRenderer {
        HeaderCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(font(13, Font.BOLD));
            setForeground(PRIMARY_DARK);
            setBackground(CARD);
            setOpaque(true);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    /**
     * Transforme la dernière colonne d'un JTable en colonne "Actions" avec
     * une icône Modifier (crayon) et une icône Supprimer (corbeille) par ligne.
     */
    public static void addActionsColumn(JTable table, Consumer<Integer> onEdit, Consumer<Integer> onDelete) {
        int lastColumn = table.getColumnCount() - 1;
        TableColumn column = table.getColumnModel().getColumn(lastColumn);
        column.setCellRenderer(new ActionsCell());
        column.setCellEditor(new ActionsCellEditor(onEdit, onDelete));
        column.setPreferredWidth(110);
        column.setMinWidth(110);
        column.setMaxWidth(110);
    }

    private static JButton iconActionButton(Icon icon, Color bg, Color fg, String tooltip) {
        RoundedButton button = new RoundedButton(null, icon, bg, fg, 8);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(32, 32));
        button.setBorder(BorderFactory.createEmptyBorder());
        return button;
    }

    private static class ActionsCell extends JPanel implements TableCellRenderer {
        ActionsCell() {
            super(new FlowLayout(FlowLayout.CENTER, 6, 2));
            setOpaque(true);
            add(iconActionButton(new PencilIcon(PRIMARY, 16), PRIMARY_LIGHT, PRIMARY, "Modifier"));
            add(iconActionButton(new TrashIcon(DANGER, 16), DANGER_LIGHT, DANGER, "Supprimer"));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setBackground(isSelected ? new Color(230, 236, 244) : Color.WHITE);
            return this;
        }
    }

    private static class ActionsCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        private int currentRow;

        ActionsCellEditor(Consumer<Integer> onEdit, Consumer<Integer> onDelete) {
            panel.setOpaque(true);
            JButton edit = iconActionButton(new PencilIcon(PRIMARY, 16), PRIMARY_LIGHT, PRIMARY, "Modifier");
            JButton delete = iconActionButton(new TrashIcon(DANGER, 16), DANGER_LIGHT, DANGER, "Supprimer");
            edit.addActionListener(e -> {
                fireEditingStopped();
                onEdit.accept(currentRow);
            });
            delete.addActionListener(e -> {
                fireEditingStopped();
                onDelete.accept(currentRow);
            });
            panel.add(edit);
            panel.add(delete);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int column) {
            currentRow = row;
            panel.setBackground(new Color(230, 236, 244));
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    /** Bouton avec fond arrondi peint manuellement (texte et/ou icône). */
    private static class RoundedButton extends JButton {
        private final int radius;

        RoundedButton(String text, Icon icon, Color bg, Color fg, int radius) {
            super(text, icon);
            this.radius = radius;
            setForeground(fg);
            setBackground(bg);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFont(font(13, Font.BOLD));
            if (text != null) {
                setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
                setHorizontalAlignment(LEFT);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg = getBackground();
            if (getModel().isPressed()) {
                bg = bg.darker();
            } else if (getModel().isRollover()) {
                bg = brighten(bg, 0.08f);
            }
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }

        private Color brighten(Color c, float amount) {
            int r = Math.min(255, (int) (c.getRed() + 255 * amount));
            int g = Math.min(255, (int) (c.getGreen() + 255 * amount));
            int b = Math.min(255, (int) (c.getBlue() + 255 * amount));
            return new Color(r, g, b);
        }
    }

    /** Icône "+" pour les boutons Ajouter. */
    public static class PlusIcon implements Icon {
        private final Color color;
        private final int size;

        public PlusIcon(Color color, int size) {
            this.color = color;
            this.size = size;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(size * 0.16f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int m = (int) (size * 0.12);
            int mid = size / 2;
            g2.drawLine(mid, m, mid, size - m);
            g2.drawLine(m, mid, size - m, mid);
            g2.dispose();
        }
    }

    /** Icône loupe pour les boutons Rechercher. */
    public static class SearchIcon implements Icon {
        private final Color color;
        private final int size;

        public SearchIcon(Color color, int size) {
            this.color = color;
            this.size = size;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            g2.setColor(color);
            float stroke = Math.max(1.6f, size * 0.14f);
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int lensSize = (int) (size * 0.62);
            g2.draw(new Ellipse2D.Float(0, 0, lensSize, lensSize));
            int handleStart = (int) (lensSize * 0.88);
            g2.drawLine(handleStart, handleStart, size - 1, size - 1);
            g2.dispose();
        }
    }

    /** Icône crayon claire : corps rectangulaire incliné + pointe triangulaire, entièrement remplis. */
    private static class PencilIcon implements Icon {
        private final Color color;
        private final int size;

        PencilIcon(Color color, int size) {
            this.color = color;
            this.size = size;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            g2.rotate(Math.toRadians(45), size / 2.0, size / 2.0);

            float bodyWidth = size * 0.24f;
            float bodyLength = size * 0.62f;
            float startX = (size - bodyWidth) / 2f;
            float startY = size * 0.08f;

            // Corps du crayon
            Path2D body = new Path2D.Float();
            body.moveTo(startX, startY + bodyLength * 0.22f);
            body.lineTo(startX + bodyWidth, startY + bodyLength * 0.22f);
            body.lineTo(startX + bodyWidth, startY + bodyLength);
            body.lineTo(startX, startY + bodyLength);
            body.closePath();
            g2.setColor(color);
            g2.fill(body);

            // Pointe (triangle)
            Path2D tip = new Path2D.Float();
            tip.moveTo(startX, startY + bodyLength * 0.22f);
            tip.lineTo(startX + bodyWidth, startY + bodyLength * 0.22f);
            tip.lineTo(startX + bodyWidth / 2f, startY);
            tip.closePath();
            g2.fill(tip);

            // Gomme (petit carré au sommet)
            g2.fillRoundRect((int) startX, (int) (startY + bodyLength), (int) bodyWidth, (int) (size * 0.09f), 2, 2);

            g2.dispose();
        }
    }

    /** Icône corbeille claire : couvercle plein + corps trapézoïdal plein avec rainures claires. */
    private static class TrashIcon implements Icon {
        private final Color color;
        private final int size;

        TrashIcon(Color color, int size) {
            this.color = color;
            this.size = size;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            g2.setColor(color);

            float margin = size * 0.12f;
            float lidY = size * 0.26f;
            float lidHeight = size * 0.09f;

            // Couvercle
            RoundRectangle2D lid = new RoundRectangle2D.Float(margin, lidY, size - 2 * margin, lidHeight, 2, 2);
            g2.fill(lid);

            // Poignée
            float handleW = size * 0.34f;
            float handleH = size * 0.10f;
            float handleX = (size - handleW) / 2f;
            RoundRectangle2D handle = new RoundRectangle2D.Float(handleX, lidY - handleH + 1, handleW, handleH, 2, 2);
            g2.fill(handle);

            // Corps (trapèze plein)
            float bodyTop = lidY + lidHeight + size * 0.03f;
            float bodyBottom = size - margin * 0.6f;
            float topW = size - 2 * margin - size * 0.06f;
            float bottomW = topW * 0.78f;
            float topX = (size - topW) / 2f;
            float bottomX = (size - bottomW) / 2f;

            Path2D bodyShape = new Path2D.Float();
            bodyShape.moveTo(topX, bodyTop);
            bodyShape.lineTo(topX + topW, bodyTop);
            bodyShape.lineTo(bottomX + bottomW, bodyBottom);
            bodyShape.lineTo(bottomX, bodyBottom);
            bodyShape.closePath();
            g2.fill(bodyShape);

            // Rainures verticales (en creux, couleur blanche pour contraster sur le fond plein)
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(Math.max(1.2f, size * 0.07f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            float grooveTop = bodyTop + (bodyBottom - bodyTop) * 0.18f;
            float grooveBottom = bodyBottom - (bodyBottom - bodyTop) * 0.16f;
            float mid = size / 2f;
            float offset = topW * 0.2f;
            g2.draw(new java.awt.geom.Line2D.Float(mid, grooveTop, mid, grooveBottom));
            g2.draw(new java.awt.geom.Line2D.Float(mid - offset, grooveTop, mid - offset * 0.82f, grooveBottom));
            g2.draw(new java.awt.geom.Line2D.Float(mid + offset, grooveTop, mid + offset * 0.82f, grooveBottom));

            g2.dispose();
        }
    }

    public static JDialog formDialog(Window owner, String title, JPanel form, BooleanSupplier onSave) {
        JDialog dialog = new JDialog(owner, title, Dialog.ModalityType.APPLICATION_MODAL);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(CARD);
        content.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JButton save = primaryButton("Enregistrer");
        JButton cancel = ghostButton("Annuler");
        save.addActionListener(e -> {
            if (onSave.getAsBoolean()) {
                dialog.dispose();
            }
        });
        cancel.addActionListener(e -> dialog.dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(cancel);
        buttons.add(save);

        content.add(form, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(380, dialog.getHeight()));
        dialog.setLocationRelativeTo(owner);
        dialog.setResizable(false);
        return dialog;
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