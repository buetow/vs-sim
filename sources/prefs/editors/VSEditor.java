package prefs.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.util.*;
import java.io.File;

import simulator.*;
import utils.*;
import prefs.VSPrefs;

public abstract class VSEditor implements ActionListener {
    protected static final int VALUE_FIELD_COLS = 9;
    protected static final int MIN_UNIT_LENGTH = 5;
    private HashMap<String,JComboBox> integerFields;
    private HashMap<String,JTextField> colorFields;
    private HashMap<String,JTextField> floatFields;
    private HashMap<String,JTextField> longFields;
    private HashMap<String,JCheckBox> booleanFields;
    private HashMap<String,JTextField> stringFields;
    private ArrayList<String> colorKeys;
    private ArrayList<String> floatKeys;
    private ArrayList<String> integerKeys;
    private ArrayList<String> longKeys;
    private ArrayList<String> booleanKeys;
    private ArrayList<String> stringKeys;
    private JPanel buttonPanel;
    private JPanel editPanel;
    protected VSPrefs prefs;
    protected VSPrefs prefsToEdit;
    public static final int ALL_PREFERENCES = 0;
    public static final int SIMULATION_PREFERENCES = 1;
    private VSFrame frame;
    protected VSEditorTable editTable;

    public VSEditor(VSPrefs prefs, VSPrefs prefsToEdit) {
        init(prefs, prefsToEdit);
    }

    public void setPrefs(VSPrefs prefs) {
        this.prefs = prefs;
    }

    public void setPrefsToEdit(VSPrefs prefsToEdit) {
        this.prefsToEdit = prefsToEdit;
    }

    public void setFrame(VSFrame frame) {
        this.frame = frame;
    }

    public VSFrame getFrame() {
        return frame;
    }

    protected void disposeFrameIfExists() {
        if (frame != null)
            frame.dispose();
    }

    protected void disposeFrameWithParentIfExists() {
        if (frame != null)
            frame.disposeWithParent();
    }

    private void init(VSPrefs prefs, VSPrefs prefsToEdit) {
        this.prefs = prefs;
        this.prefsToEdit = prefsToEdit;

        editPanel = createEditPanel();
        buttonPanel = createButtonPanel();

        colorKeys = filterKeys(prefsToEdit.getColorKeySet());
        floatKeys = filterKeys(prefsToEdit.getFloatKeySet());
        integerKeys = filterKeys(prefsToEdit.getIntegerKeySet());
        longKeys = filterKeys(prefsToEdit.getLongKeySet());
        booleanKeys = filterKeys(prefsToEdit.getBooleanKeySet());
        stringKeys = filterKeys(prefsToEdit.getStringKeySet());
        colorFields = new HashMap<String,JTextField>();
        floatFields = new HashMap<String,JTextField>();
        integerFields = new HashMap<String,JComboBox>();
        longFields = new HashMap<String,JTextField>();
        booleanFields = new HashMap<String,JCheckBox>();
        stringFields = new HashMap<String,JTextField>();

        fillEditPanel(editPanel, editTable);
    }

    private ArrayList<String> filterKeys(Set<String> set) {
        ArrayList<String> filtered = new ArrayList<String>();

        for (String elem : set)
            if (!elem.startsWith("lang.") && !elem.startsWith("keyevent"))
                filtered.add(elem);

        return filtered;
    }

    abstract protected void addToButtonPanelFront(JPanel buttonPanel);
    abstract protected void addToButtonPanelLast(JPanel buttonPanel);

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        addToButtonPanelFront(buttonPanel);

        JButton resetButton = new JButton(
            prefs.getString("lang.reset"));
        resetButton.setMnemonic(prefs.getInteger("keyevent.reset"));
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);

        addToButtonPanelLast(buttonPanel);

        return buttonPanel;
    }

    private JPanel createUnitPanel(Component comp, String key) {
        JPanel unitPanel = new JPanel(new GridBagLayout());
        unitPanel.setBackground(Color.WHITE);
        unitPanel.setBorder(null);

        String unitText = prefs.getUnit(key);
        if (unitText == null)
            unitText = "";

        unitText = " " + unitText;
        while (unitText.length() < MIN_UNIT_LENGTH)
            unitText = unitText + " ";
        JLabel unitLabel = new JLabel(unitText);

        unitPanel.setLayout(new BoxLayout(unitPanel, BoxLayout.X_AXIS));
        unitPanel.add(comp);
        unitPanel.add(unitLabel);

        return unitPanel;
    }

    private JPanel createEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setBackground(Color.WHITE);

        editTable = new VSEditorTable(prefs);
        JScrollPane scrollPane = new JScrollPane(editTable);
        editPanel.add(scrollPane);

        return editPanel;
    }

    private void fillEditPanel(JPanel editPanel, VSEditorTable editTable) {
        HashMap<String,Component> components = new HashMap<String,Component>();
        HashMap<String,String> labels = new HashMap<String,String>();

        for (String key : integerKeys) {
            String fullKey = VSPrefs.INTEGER_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);
            String label = descr == null ? fullKey : descr;
            Integer integer = prefsToEdit.getInteger(key);
            Integer initialSelection[] = { integer };
            JComboBox valComboBox = new JComboBox(initialSelection);
            VSPrefs.SettingRestriction settingRestriction = prefsToEdit.getRestriction(fullKey);

            int minValue, maxValue;
            if (settingRestriction != null) {
                VSPrefs.IntegerSettingRestriction integerSettingRestriction =
                    (VSPrefs.IntegerSettingRestriction) settingRestriction;
                minValue = integerSettingRestriction.getMinValue();
                maxValue = integerSettingRestriction.getMaxValue();
            } else {
                minValue = 0;
                maxValue = 100;
            }

            for (int i = minValue; i <= maxValue; ++i)
                valComboBox.addItem(new Integer(i));

            integerFields.put(key, valComboBox);
            valComboBox.setBorder(null);

            labels.put(fullKey, label);
            components.put(fullKey, createUnitPanel(valComboBox, fullKey));
        }

        final String activated = prefs.getString("lang.activated");
        for (String key : booleanKeys) {
            String fullKey = VSPrefs.BOOLEAN_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);
            String label = descr == null ? fullKey : descr;
            JCheckBox valField = new JCheckBox(activated, prefsToEdit.getBoolean(key));
            valField.setBackground(Color.WHITE);
            booleanFields.put(key, valField);
            valField.setBorder(null);

            labels.put(fullKey, label);
            components.put(fullKey, createUnitPanel(valField, fullKey));
        }

        for (String key : longKeys) {
            String fullKey = VSPrefs.LONG_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);
            String label = descr == null ? fullKey : descr;
            JTextField valField = new JTextField(VALUE_FIELD_COLS);
            valField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    JTextField valField = (JTextField)e.getSource();
                    if (valField.getText().length() >= valField.getColumns() + 10)
                        e.consume();
                }
            });
            valField.setText(""+prefsToEdit.getLong(key));
            longFields.put(key, valField);
            valField.setBorder(null);

            labels.put(fullKey, label);
            components.put(fullKey, createUnitPanel(valField, fullKey));
        }


        for (String key : floatKeys) {
            String fullKey = VSPrefs.FLOAT_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);
            String label = descr == null ? fullKey : descr;
            JTextField valField = new JTextField(VALUE_FIELD_COLS);
            valField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    JTextField valField = (JTextField)e.getSource();
                    if (valField.getText().length() >= valField.getColumns() + 10)
                        e.consume();
                }
            });
            valField.setText(""+prefsToEdit.getFloat(key));
            floatFields.put(key, valField);
            valField.setBorder(null);
            labels.put(fullKey, label);
            components.put(fullKey, createUnitPanel(valField, fullKey));
        }


        for (String key : colorKeys) {
            String fullKey = VSPrefs.COLOR_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);
            String label = descr == null ? fullKey : descr;
            final JTextField valField = new JTextField(VALUE_FIELD_COLS);
            Color color = prefsToEdit.getColor(key);
            valField.setBackground(color);
            valField.setEditable(false);
            valField.addMouseListener(new MouseListener() {
                public void mouseExited(MouseEvent e) { }
                public void mouseReleased(MouseEvent e) { }
                public void mouseEntered(MouseEvent e) { }
                public void mousePressed(MouseEvent e) { }
                public void mouseClicked(MouseEvent e) {
                    JFrame parentFrame = getFrame();
                    JFrame frame = new VSFrame(
                        prefs.getString("lang.name") + " - " +
                        prefs.getString(
                            "lang.colorchooser"),parentFrame);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    JComponent colorChooserPane = new VSColorChooser(prefs, valField);
                    colorChooserPane.setOpaque(true);

                    frame.setContentPane(colorChooserPane);
                    frame.pack();
                    frame.setVisible(true);
                }
            });
            colorFields.put(key, valField);
            valField.setBorder(null);
            labels.put(fullKey, label);
            components.put(fullKey, valField);
        }

        for (String key : stringKeys) {
            String fullKey = VSPrefs.STRING_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);
            String label = descr == null ? fullKey : descr;
            JTextField valField = new JTextField(VALUE_FIELD_COLS);
            valField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    JTextField valField = (JTextField)e.getSource();
                    if (valField.getText().length() >= valField.getColumns() + 10)
                        e.consume();
                }
            });
            valField.setText(prefsToEdit.getString(key));
            stringFields.put(key, valField);
            valField.setBorder(null);
            labels.put(fullKey, label);
            components.put(fullKey, createUnitPanel(valField, fullKey));
        }

        ArrayList<String> fullKeys = new ArrayList<String>();
        fullKeys.addAll(components.keySet());
        Collections.sort(fullKeys);

        boolean flag = false;
        for (String fullKey : fullKeys) {
            String key = fullKey.substring(fullKey.indexOf(' ')+1);
            if (key.startsWith("sim.")) {
                if (!flag) {
                    flag = true;
                    editTable.addSeparator(prefs.getString("lang.prefs.simulation"));
                }
                editTable.addVariable(labels.get(fullKey), components.get(fullKey));
            }
        }

        flag = false;
        for (String fullKey : fullKeys) {
            String key = fullKey.substring(fullKey.indexOf(' ')+1);
            if (key.startsWith("process.")) {
                if (!flag) {
                    flag = true;
                    editTable.addSeparator(prefs.getString("lang.prefs.process"));
                }
                editTable.addVariable(labels.get(fullKey), components.get(fullKey));
            }
        }

        flag = false;
        for (String fullKey : fullKeys) {
            String key = fullKey.substring(fullKey.indexOf(' ')+1);
            if (key.startsWith("message.")) {
                if (!flag) {
                    flag = true;
                    editTable.addSeparator(prefs.getString("lang.prefs.message"));
                }
                editTable.addVariable(labels.get(fullKey), components.get(fullKey));
            }
        }

        flag = false;
        for (String fullKey : fullKeys) {
            String key = fullKey.substring(fullKey.indexOf(' ')+1);
            if (key.startsWith("col.")) {
                if (!flag) {
                    flag = true;
                    editTable.addSeparator(prefs.getString("lang.prefs.color"));
                }
                editTable.addVariable(labels.get(fullKey), components.get(fullKey));
            }
        }

        flag = false;
        for (String fullKey : fullKeys) {
            String key = fullKey.substring(fullKey.indexOf(' ')+1);
            if (key.startsWith("div.")) {
                if (!flag) {
                    flag = true;
                    editTable.addSeparator(prefs.getString("lang.prefs.diverse"));
                }
                editTable.addVariable(labels.get(fullKey), components.get(fullKey));
            }
        }

        editTable.fireTableDataChanged();
    }

    protected void resetEditPanel() {
        for (String key : integerKeys) {
            JComboBox valComboBox = integerFields.get(key);
            valComboBox.setSelectedIndex(0);
        }

        for (String key : booleanKeys) {
            JCheckBox valField = booleanFields.get(key);
            valField.setSelected(prefsToEdit.getBoolean(key));
        }

        for (String key : floatKeys) {
            JTextField valField = floatFields.get(key);
            valField.setText(""+prefsToEdit.getFloat(key));
        }

        for (String key : longKeys) {
            JTextField valField = longFields.get(key);
            valField.setText(""+prefsToEdit.getLong(key));
        }

        for (String key : colorKeys) {
            JTextField valField = colorFields.get(key);
            valField.setBackground(prefsToEdit.getColor(key));
        }

        for (String key : stringKeys) {
            JTextField valField = stringFields.get(key);
            valField.setText(prefsToEdit.getString(key));
        }
    }

    protected void savePrefs() {
        int i = 0;
        for (String key : integerKeys) {
            JComboBox valComboBox = integerFields.get(key);
            prefsToEdit.setInteger(key, (Integer) valComboBox.getSelectedItem());
        }

        for (String key : booleanKeys) {
            JCheckBox valField = booleanFields.get(key);
            prefsToEdit.setBoolean(key, valField.isSelected());
        }

        for (String key : floatKeys) {
            JTextField valField = floatFields.get(key);

            try {
                Float val = Float.valueOf(valField.getText());
                prefsToEdit.setFloat(key, val);

            } catch (NumberFormatException e) {
                valField.setText("0.0");
            }
        }

        for (String key : longKeys) {
            JTextField valField = longFields.get(key);

            try {
                Long val = Long.valueOf(valField.getText());
                prefsToEdit.setLong(key, val);

            } catch (NumberFormatException e) {
                valField.setText("0");
            }
        }

        for (String key : colorKeys) {
            JTextField valField = colorFields.get(key);
            prefsToEdit.setColor(key, valField.getBackground());
        }

        for (String key : stringKeys) {
            JTextField valField = stringFields.get(key);
            prefsToEdit.setString(key, valField.getText());
        }
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.takeover"))) {
            savePrefs();

        } else if (actionCommand.equals(prefs.getString("lang.reset"))) {
            resetEditPanel();
        }
    }

    public JPanel getEditPanel() {
        return editPanel;
    }

    public VSEditorTable getEditTable() {
        return editTable;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }
}
