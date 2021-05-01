/*
 * Copyright (c) 2008 Paul C. Buetow, vs-sim@dev.buetow.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * All icons of the icons/ folder are 	under a Creative Commons
 * Attribution-Noncommercial-Share Alike License a CC-by-nc-sa.
 *
 * The icon's homepage is http://code.google.com/p/ultimate-gnome/
 */

package prefs.editors;

import java.util.*;

import java.awt.*;
//import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;

import prefs.*;

/**
 * The class VSEditorTable, each VSAbstractEditor uses an object of this class
 * for displaying all editable items!
 *
 * @author Paul C. Buetow
 */
public class VSEditorTable extends JTable {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The Constant MIN_ROWS. */
    //private static final int MIN_ROWS = 20;

    /** The prefs. */
    //private VSPrefs prefs;

    /** The nodes. */
    private ArrayList<VSNode> nodes;

    /** The model. */
    private VSEditorTableModel model;

    /**
     * The class VSNode.
     */
    private class VSNode {
        /** The serial version uid */
        private static final long serialVersionUID = 1L;

        /** The key. */
        private String key;

        /** The comp. */
        private Component comp;

        /**
         * Instantiates a new VSNode object.
         *
         * @param key the key
         */
        public VSNode(String key) {
            this.key = key;
        }

        /**
         * Instantiates a new VSNode object.
         *
         * @param key the key
         * @param comp the comp
         */
        public VSNode(String key, Component comp) {
            this.key = key;
            this.comp = comp;
        }

        /**
         * Gets the key.
         *
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * Gets the component.
         *
         * @return the component
         */
        public Component getComponent() {
            return comp;
        }

        /**
         * Gets the renderer component.
         *
         * @return the renderer component
         */
        public Component getRendererComponent() {
            return comp;
        }

        /**
         * Checks if it is a separator.
         *
         * @return true, if is separator
         */
        public boolean isSeparator() {
            return comp == null;
        }
    }

    /**
     * The class VSEditorTableModel, it is the model of a VSEditorTable.
     */
    private class VSEditorTableModel extends AbstractTableModel
                implements TableCellRenderer {
        /** The serial version uid */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new lang.process.removeeditor table model.
         */
        public VSEditorTableModel() {
        }

        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#getColumnName(int)
         */
        public String getColumnName(int col) {
            return "";
        }

        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return nodes.size();
        }

        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return 2;
        }

        /* (non-Javadoc)
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
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

        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
         */
        public boolean isCellEditable(int row, int col) {
            if (col == 0)
                return false;

            if (nodes.get(row).isSeparator())
                return false;

            return true;
        }

        /* (non-Javadoc)
         * @see javax.swing.table.AbstractTableModel#setValueAt(
         *	java.lang.Object, int, int)
         */
        public void setValueAt(Object value, int row, int col) {
        }

        /* (non-Javadoc)
         * @see
         *	javax.swing.table.TableCellRenderer#getTableCellRendererComponent(
         *	javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
         */
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

    /**
     * The class VSTableCellEditor, is the editor of the VSEditorTable
     */
    private class VSTableCellEditor extends AbstractCellEditor
                implements TableCellEditor  {
        /** The serial version uid */
        private static final long serialVersionUID = 1L;

        /* (non-Javadoc)
         * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(
         *	javax.swing.JTable, java.lang.Object, boolean, int, int)
         */
        public Component getTableCellEditorComponent(JTable table,
                Object object,
                boolean isSelected,
                int row,
                int col) {
            return nodes.get(row).getComponent();
        }

        /* (non-Javadoc)
         * @see javax.swing.CellEditor#getCellEditorValue()
         */
        public Object getCellEditorValue() {
            return new String("");
        }
    }

    /**
     * Instantiates a new VSEditorTable object.
     *
     * @param prefs the prefs
     */
    public VSEditorTable(VSPrefs prefs) {
        //this.prefs = prefs;
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

    /**
     * Adds the variable.
     *
     * @param key the key
     * @param comp the comp
     */
    public void addVariable(String key, Component comp) {
        nodes.add(new VSNode(key, comp));
    }

    /**
     * Adds the separator.
     *
     * @param text the text
     */
    public void addSeparator(String text) {
        nodes.add(new VSNode(text));
    }

    /**
     * Fires that the table data has changed.
     */
    public void fireTableDataChanged() {
        model.fireTableDataChanged();
    }
}
