package prefs.editors;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import prefs.*;

public class VSEditorTable extends JTable {
	private static final int MIN_ROWS = 20;
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

		public Component getRendererComponent() {
			return comp;
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

        public Component getTableCellRendererComponent(JTable table,
                Object object, boolean isSelected, boolean hasFocus, int
                row, int col) {

            VSNode node = nodes.get(row);

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
        col.setMaxWidth(90);
        col.setResizable(false);

        col = getColumnModel().getColumn(0);
        col.sizeWidthToFit();
    }

    public void addVariable(String key, Component comp) {
        nodes.add(new VSNode(key, comp));
        model.fireTableDataChanged();
    }
}
