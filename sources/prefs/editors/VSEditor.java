package prefs.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import utils.*;
import prefs.VSPrefs;

public abstract class VSEditor implements ActionListener {
    private ArrayList<String> booleanKeys;
    private ArrayList<String> colorKeys;
    private ArrayList<String> floatKeys;
    private ArrayList<String> integerKeys;
    private ArrayList<String> longKeys;
    private ArrayList<String> stringKeys;
    private HashMap<String,JCheckBox> booleanFields;
    private HashMap<String,JComboBox> integerFields;
    private HashMap<String,JTextField> colorFields;
    private HashMap<String,JTextField> floatFields;
    private HashMap<String,JTextField> longFields;
    private HashMap<String,JTextField> stringFields;
    private HashMap<String,VSPrefs> prefsToEditMap;
    private JPanel buttonPanel;
    private JPanel editPanel;
    private VSEditorTable editTable;
    private VSFrame frame;
    private boolean expertModeChanged;
    protected VSPrefs prefs;
    protected VSPrefs prefsToEdit;
    protected static final int MIN_UNIT_LENGTH = 5;
    protected static final int VALUE_FIELD_COLS = 9;
    public static final int ALL_PREFERENCES = 0;
    public static final int SIMULATION_PREFERENCES = 1;

    public VSEditor(VSPrefs prefs, VSPrefs prefsToEdit) {
        init(prefs, prefsToEdit);
    }

    abstract protected void addToButtonPanelFront(JPanel buttonPanel);
    abstract protected void addToButtonPanelLast(JPanel buttonPanel);
    abstract protected void addToEditTableLast();

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

        prefsToEditMap = new HashMap<String,VSPrefs>();

        colorFields = new HashMap<String,JTextField>();
        floatFields = new HashMap<String,JTextField>();
        integerFields = new HashMap<String,JComboBox>();
        longFields = new HashMap<String,JTextField>();
        booleanFields = new HashMap<String,JCheckBox>();
        stringFields = new HashMap<String,JTextField>();

        colorKeys = filterKeys(prefsToEdit.getColorKeySet());
        floatKeys = filterKeys(prefsToEdit.getFloatKeySet());
        integerKeys = filterKeys(prefsToEdit.getIntegerKeySet());
        longKeys = filterKeys(prefsToEdit.getLongKeySet());
        booleanKeys = filterKeys(prefsToEdit.getBooleanKeySet());
        stringKeys = filterKeys(prefsToEdit.getStringKeySet());

        fillEditPanel(prefsToEdit);
    }

    private ArrayList<String> filterKeys(Set<String> set) {
        ArrayList<String> filtered = new ArrayList<String>();
        boolean expertMode = prefs.getBoolean("sim.mode.expert");

        for (String elem : set) {
            if (!elem.startsWith("lang.") && !elem.startsWith("keyevent")) {
                if (expertMode)
                    filtered.add(elem);
                else if (!elem.startsWith("col.") && (!elem.startsWith("div.")))
                    filtered.add(elem);
            }
        }

        return filtered;
    }

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

    protected VSTupel<String,Component,JComboBox> createIntegerComponent(String fullKey, String key, VSPrefs prefsToEdit) {
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
        valComboBox.setBorder(null);

        return new VSTupel<String,Component,JComboBox>(label, createUnitPanel(valComboBox, fullKey), valComboBox);
    }

    protected VSTupel<String,Component,JCheckBox> createBooleanComponent(String fullKey, String key, VSPrefs prefsToEdit) {
        final String activated = prefs.getString("lang.activated");
        String descr = prefsToEdit.getDescription(fullKey);
        String label = descr == null ? fullKey : descr;
        JCheckBox valField = new JCheckBox(activated, prefsToEdit.getBoolean(key));
        valField.setBackground(Color.WHITE);
        valField.setBorder(null);
        return new VSTupel<String,Component,JCheckBox>(label, createUnitPanel(valField, fullKey), valField);
    }

    protected VSTupel<String,Component,JTextField> createLongComponent(String fullKey, String key, VSPrefs prefsToEdit) {
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
        valField.setBorder(null);
        return new VSTupel<String,Component,JTextField>(label, createUnitPanel(valField, fullKey), valField);
    }

    protected VSTupel<String,Component,JTextField> createFloatComponent(String fullKey, String key, VSPrefs prefsToEdit) {
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
        valField.setBorder(null);
        return new VSTupel<String,Component,JTextField>(label, createUnitPanel(valField, fullKey), valField);
    }

    protected VSTupel<String,Component,JTextField> createColorComponent(String fullKey, String key, final VSPrefs prefsToEdit) {
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
        valField.setBorder(null);
        return new VSTupel<String,Component,JTextField>(label, createUnitPanel(valField, fullKey), valField);
    }

    protected VSTupel<String,Component,JTextField> createStringComponent(String fullKey, String key, VSPrefs prefsToEdit) {
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
        valField.setBorder(null);
        return new VSTupel<String,Component,JTextField>(label, createUnitPanel(valField, fullKey), valField);
    }

    private void fillEditPanel(VSPrefs prefsToEdit) {
        HashMap<String,Component> components = new HashMap<String,Component>();
        HashMap<String,String> labels = new HashMap<String,String>();

        for (String key : integerKeys) {
            String fullKey = VSPrefs.INTEGER_PREFIX + key;
            VSTupel<String,Component,JComboBox> tupel = createIntegerComponent(fullKey, key, prefsToEdit);
            labels.put(fullKey, tupel.getA());
            components.put(fullKey, tupel.getB());
            integerFields.put(key, tupel.getC());
        }

        for (String key : booleanKeys) {
            String fullKey = VSPrefs.BOOLEAN_PREFIX + key;
            VSTupel<String,Component,JCheckBox> tupel = createBooleanComponent(fullKey, key, prefsToEdit);
            labels.put(fullKey, tupel.getA());
            components.put(fullKey, tupel.getB());
            booleanFields.put(key, tupel.getC());
        }

        for (String key : longKeys) {
            String fullKey = VSPrefs.LONG_PREFIX + key;
            VSTupel<String,Component,JTextField> tupel = createLongComponent(fullKey, key, prefsToEdit);
            labels.put(fullKey, tupel.getA());
            components.put(fullKey, tupel.getB());
            longFields.put(key, tupel.getC());
        }


        for (String key : floatKeys) {
            String fullKey = VSPrefs.FLOAT_PREFIX + key;
            VSTupel<String,Component,JTextField> tupel = createFloatComponent(fullKey, key, prefsToEdit);
            labels.put(fullKey, tupel.getA());
            components.put(fullKey, tupel.getB());
            floatFields.put(key, tupel.getC());
        }


        for (String key : colorKeys) {
            String fullKey = VSPrefs.COLOR_PREFIX + key;
            VSTupel<String,Component,JTextField> tupel = createColorComponent(fullKey, key, prefsToEdit);
            labels.put(fullKey, tupel.getA());
            components.put(fullKey, tupel.getB());
            colorFields.put(key, tupel.getC());
        }

        for (String key : stringKeys) {
            String fullKey = VSPrefs.STRING_PREFIX + key;
            VSTupel<String,Component,JTextField> tupel = createStringComponent(fullKey, key, prefsToEdit);
            labels.put(fullKey, tupel.getA());
            components.put(fullKey, tupel.getB());
            stringFields.put(key, tupel.getC());
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
                    addSeparator(prefs.getString("lang.prefs.simulation"));
                }
                addVariable(labels.get(fullKey), components.get(fullKey), prefsToEdit);
            }
        }

        flag = false;
        for (String fullKey : fullKeys) {
            String key = fullKey.substring(fullKey.indexOf(' ')+1);
            if (key.startsWith("process.")) {
                if (!flag) {
                    flag = true;
                    addSeparator(prefs.getString("lang.prefs.process"));
                }
                addVariable(labels.get(fullKey), components.get(fullKey), prefsToEdit);
            }
        }

        flag = false;
        for (String fullKey : fullKeys) {
            String key = fullKey.substring(fullKey.indexOf(' ')+1);
            if (key.startsWith("message.")) {
                if (!flag) {
                    flag = true;
                    addSeparator(prefs.getString("lang.prefs.message"));
                }
                addVariable(labels.get(fullKey), components.get(fullKey), prefsToEdit);
            }
        }

        flag = false;
        for (String fullKey : fullKeys) {
            String key = fullKey.substring(fullKey.indexOf(' ')+1);
            if (key.startsWith("col.")) {
                if (!flag) {
                    flag = true;
                    addSeparator(prefs.getString("lang.prefs.color"));
                }
                addVariable(labels.get(fullKey), components.get(fullKey), prefsToEdit);
            }
        }

        flag = false;
        for (String fullKey : fullKeys) {
            String key = fullKey.substring(fullKey.indexOf(' ')+1);
            if (key.startsWith("div.")) {
                if (!flag) {
                    flag = true;
                    addSeparator(prefs.getString("lang.prefs.diverse"));
                }
                addVariable(labels.get(fullKey), components.get(fullKey), prefsToEdit);
            }
        }

        addToEditTableLast();
        editTable.fireTableDataChanged();
    }

    protected void addToEditor(String label, String prefsKey, VSPrefs prefsToAdd) {
        addSeparator(label);
        prefsKey = "(" + prefsKey + ")";

        ArrayList<String> fullKeys = new ArrayList<String>();

        Set<String> integerKeys = prefsToAdd.getIntegerKeySet();
        Set<String> floatKeys = prefsToAdd.getFloatKeySet();
        Set<String> longKeys = prefsToAdd.getLongKeySet();
        Set<String> booleanKeys = prefsToAdd.getBooleanKeySet();
        Set<String> stringKeys = prefsToAdd.getStringKeySet();

        for (String key : integerKeys) fullKeys.add(VSPrefs.INTEGER_PREFIX + key);
        for (String key : floatKeys) fullKeys.add(VSPrefs.FLOAT_PREFIX + key);
        for (String key : longKeys) fullKeys.add(VSPrefs.LONG_PREFIX + key);
        for (String key : booleanKeys) fullKeys.add(VSPrefs.BOOLEAN_PREFIX + key);
        for (String key : stringKeys) fullKeys.add(VSPrefs.STRING_PREFIX + key);

        Collections.sort(fullKeys);

        for (String fullKey : fullKeys) {
            String key = fullKey.substring(fullKey.indexOf(": ") + 2);
            if (fullKey.startsWith(VSPrefs.INTEGER_PREFIX)) {
                VSTupel<String,Component,JComboBox> tupel =
                    createIntegerComponent(fullKey, key, prefsToAdd);
                this.integerKeys.add(prefsKey+key);
                this.integerFields.put(prefsKey+key, tupel.getC());
                addVariable(prefsKey, tupel.getA(), tupel.getB(), prefsToAdd);

            } else if (fullKey.startsWith(VSPrefs.BOOLEAN_PREFIX)) {
                VSTupel<String,Component,JCheckBox> tupel =
                    createBooleanComponent(fullKey, key, prefsToAdd);
                this.booleanKeys.add(prefsKey + key);
                this.booleanFields.put(prefsKey+key, tupel.getC());
                addVariable(prefsKey, tupel.getA(), tupel.getB(), prefsToAdd);

            } else if (fullKey.startsWith(VSPrefs.LONG_PREFIX)) {
                VSTupel<String,Component,JTextField> tupel =
                    createLongComponent(fullKey, key, prefsToAdd);
                this.longKeys.add(prefsKey+key);
                this.longFields.put(prefsKey+key, tupel.getC());
                addVariable(prefsKey, tupel.getA(), tupel.getB(), prefsToAdd);

            } else if (fullKey.startsWith(VSPrefs.FLOAT_PREFIX)) {
                VSTupel<String,Component,JTextField> tupel =
                    createFloatComponent(fullKey, key, prefsToAdd);
                this.floatKeys.add(prefsKey + key);
                this.floatFields.put(prefsKey+key, tupel.getC());
                addVariable(prefsKey, tupel.getA(), tupel.getB(), prefsToAdd);

            } else if (fullKey.startsWith(VSPrefs.STRING_PREFIX)) {
                VSTupel<String,Component,JTextField> tupel =
                    createStringComponent(fullKey, key, prefsToAdd);
                this.stringKeys.add(prefsKey + key);
                this.stringFields.put(prefsKey+key, tupel.getC());
                addVariable(prefsKey, tupel.getA(), tupel.getB(), prefsToAdd);
            }

        }
    }

    private void addSeparator(String label) {
        editTable.addSeparator(label);
    }

    private void addVariable(String label, Component component, VSPrefs prefs) {
        addVariable("", label, component, prefs);
    }

    private void addVariable(String prefsKey, String label, Component component, VSPrefs prefs) {
        prefsToEditMap.put(prefsKey, prefs);
        editTable.addVariable(label, component);
    }

    protected void resetEditPanel() {
        for (String key : integerKeys) {
            JComboBox valComboBox = integerFields.get(key);
            valComboBox.setSelectedIndex(0);
        }

        for (String key : booleanKeys) {
            String keys[] = getKeys(key);
            JCheckBox valField = booleanFields.get(key);
            valField.setSelected(prefsToEditMap.get(keys[1]).getBoolean(keys[0]));
        }

        for (String key : floatKeys) {
            String keys[] = getKeys(key);
            JTextField valField = floatFields.get(key);
            valField.setText(""+prefsToEditMap.get(keys[1]).getFloat(keys[0]));
        }

        for (String key : longKeys) {
            String keys[] = getKeys(key);
            JTextField valField = longFields.get(key);
            valField.setText(""+prefsToEditMap.get(keys[1]).getLong(keys[0]));
        }

        for (String key : colorKeys) {
            String keys[] = getKeys(key);
            JTextField valField = colorFields.get(key);
            valField.setBackground(prefsToEditMap.get(keys[1]).getColor(keys[0]));
        }

        for (String key : stringKeys) {
            String keys[] = getKeys(key);
            JTextField valField = stringFields.get(keys);
            valField.setText(prefsToEditMap.get(keys[1]).getString(keys[0]));
        }
    }

    /**
     * @return [0] := key, [1] := prefsKey
     */
    private String[] getKeys(String key) {
        String keys[] = { key, "" };

        if (key.startsWith("(")) {
            keys[1] = key.substring(0, key.indexOf(")") + 1);
            keys[0] = key.substring(key.indexOf(")")+1);
        }

        return keys;
    }

    protected void savePrefs() {
        boolean expertMode = prefs.getBoolean("sim.mode.expert");

        for (String key : integerKeys) {
            String keys[] = getKeys(key);
            //String fullKey = VSPrefs.INTEGER_PREFIX + keys[0];
            JComboBox valComboBox = integerFields.get(key);
            prefsToEditMap.get(keys[1]).setInteger(keys[0], (Integer) valComboBox.getSelectedItem());
        }

        for (String key : booleanKeys) {
            String keys[] = getKeys(key);
            //String fullKey = VSPrefs.BOOLEAN_PREFIX + keys[0];
            JCheckBox valField = booleanFields.get(key);
            prefsToEditMap.get(keys[1]).setBoolean(keys[0], valField.isSelected());
        }

        for (String key : floatKeys) {
            String keys[] = getKeys(key);
            JTextField valField = floatFields.get(key);

            try {
                //String fullKey = VSPrefs.FLOAT_PREFIX + keys[0];
                Float val = Float.valueOf(valField.getText());
                prefsToEditMap.get(keys[1]).setFloat(keys[0], val);

            } catch (NumberFormatException e) {
                valField.setText("0.0");
            }
        }

        for (String key : longKeys) {
            String keys[] = getKeys(key);
            JTextField valField = longFields.get(key);

            try {
                //String fullKey = VSPrefs.LONG_PREFIX + keys[0];
                Long val = Long.valueOf(valField.getText());
                prefsToEditMap.get(keys[1]).setLong(keys[0], val);

            } catch (NumberFormatException e) {
                valField.setText("0");
            }
        }

        for (String key : colorKeys) {
            String keys[] = getKeys(key);
            //String fullKey = VSPrefs.COLOR_PREFIX + keys[0];
            JTextField valField = colorFields.get(key);
            prefsToEditMap.get(keys[1]).setColor(keys[0], valField.getBackground());
        }

        for (String key : stringKeys) {
            String keys[] = getKeys(key);
            //String fullKey = VSPrefs.STRING_PREFIX + keys[0];
            JTextField valField = stringFields.get(key);
            prefsToEditMap.get(keys[1]).setString(keys[0], valField.getText());
        }

        expertModeChanged = expertMode != prefs.getBoolean("sim.mode.expert");
    }

    public boolean expertModeChanged() {
        boolean ret = expertModeChanged;

        if (expertModeChanged)
            expertModeChanged = false;

        return ret;
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
