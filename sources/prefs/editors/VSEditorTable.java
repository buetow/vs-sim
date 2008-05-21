package prefs.editors;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import prefs.*;

public class VSEditorTable extends JTable {
    private VSPrefs prefs;
    private ArrayList<VSNode> nodes;
    private VSEditorTableModel model;

    private class VSNode {
        private String key;
        private Component comp;

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
    }

    private class VSEditorTableModel extends AbstractTableModel {
        public VSEditorTableModel() {
        }

        public String getColumnName(int col) {
            if (col == 0)
                return prefs.getString("lang.variable");

            return prefs.getString("lang.value");
        }

        public int getRowCount() {
            return nodes.size();
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int row, int col) {
            VSNode node = nodes.get(row);

            if (col == 0)
                return node.getKey();
            else
                return node.getComponent();
        }

        public boolean isCellEditable(int row, int col) {
            if (col == 0)
                return false;

            return true;
        }

        public void setValueAt(Object value, int row, int col) {
        }
    }

    public VSEditorTable(VSPrefs prefs) {
        this.prefs = prefs;
        this.nodes = new ArrayList<VSNode>();
        this.model = new VSEditorTableModel();
        setModel(model);
    }

    public void addVariable(String key, Component comp) {
        nodes.add(new VSNode(key, comp));
        model.fireTableDataChanged();
    }
}
