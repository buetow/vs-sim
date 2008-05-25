package prefs.editors;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;

import prefs.*;

public class VSEditorTable extends JTable {
    private static final int MIN_ROWS = 20;
    private VSPrefs prefs;
    private ArrayList<VSNode> nodes;
    private VSEditorTableModel model;

    private class VSNode {
        private String key;
        private Component comp;

        public VSNode(String key) {
            this.key = key;
        }

        public VSNode(String key, Component comp) {
            this.key = key;
            this.comp = comp;
        }

        public String getKey() {
            return key;
        }

        public Component getComponent() {
            return comp;
        }

        public Component getRendererComponent() {
            return comp;
        }

        public boolean isSeparator() {
            return comp == null;
        }
    }

    private class VSEditorTableModel extends AbstractTableModel implements TableCellRenderer {
        public VSEditorTableModel() {
        }

        public String getColumnName(int col) {
            return "";
        }

        public int getRowCount() {
            return nodes.size();
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int row, int col) {
            VSNode node = nodes.get(row);

            if (node.isSeparator()) {
                if (col == 1)
                    return "";

                return node.getKey();
            }

            if (col == 0)
                return node.getKey();

            return node.getComponent();
        }

        public boolean isCellEditable(int row, int col) {
            if (col == 0)
                return false;

            if (nodes.get(row).isSeparator())
                return false;

            return true;
        }

        public void setValueAt(Object value, int row, int col) {
        }

        public Component getTableCellRendererComponent(JTable table,
                Object object, boolean isSelected, boolean hasFocus, int
                row, int col) {

            VSNode node = nodes.get(row);

            if (node.isSeparator()) {
                JTextPane pane = new JTextPane();
                if (col == 0) {
                    pane.setText(node.getKey());
                    Style style = pane.addStyle("Bold", null);
                    StyleConstants.setBold(style, true);
                }
                pane.setBackground(new Color(0xCF, 0xCF, 0XCF));
                return pane;
            }

            if (col == 0) {
                JTextField field = new JTextField(" "+node.getKey()+":");
                field.setBorder(null);
                field.setEditable(false);
                field.setBackground(Color.WHITE);
                return field;
            }

            return node.getRendererComponent();
        }
    }

    private class VSTableCellEditor extends AbstractCellEditor implements TableCellEditor  {

        public Component getTableCellEditorComponent(JTable table, Object object,
                boolean isSelected, int row, int col) {
            return nodes.get(row).getComponent();
        }

        public Object getCellEditorValue() {
            return new String("");
        }
    }

    public VSEditorTable(VSPrefs prefs) {
        this.prefs = prefs;
        this.nodes = new ArrayList<VSNode>();
        this.model = new VSEditorTableModel();
        setModel(model);
        setDefaultRenderer(Object.class, model);
        setDefaultEditor(Object.class, new VSTableCellEditor());
        setIntercellSpacing(new Dimension(5, 5));
        setRowHeight(25);
        setBackground(Color.WHITE);
        getTableHeader().setVisible(false);
        TableColumn col = getColumnModel().getColumn(1);
        col.setMinWidth(100);
        col.setMaxWidth(100);
        col.setResizable(false);

        col = getColumnModel().getColumn(0);
        col.sizeWidthToFit();
    }

    public void addVariable(String key, Component comp) {
        nodes.add(new VSNode(key, comp));
    }

    public void addSeparator(String text) {
        nodes.add(new VSNode(text));
    }

    public void fireTableDataChanged() {
        model.fireTableDataChanged();
    }
}
