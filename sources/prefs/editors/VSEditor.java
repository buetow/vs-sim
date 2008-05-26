/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package prefs.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import utils.*;
import prefs.VSPrefs;

// TODO: Auto-generated Javadoc
/**
 * The Class VSEditor.
 */
public abstract class VSEditor implements ActionListener {
    private static final long serialVersionUID = 1L;

    /** The boolean keys. */
    private ArrayList<String> booleanKeys;

    /** The color keys. */
    private ArrayList<String> colorKeys;

    /** The float keys. */
    private ArrayList<String> floatKeys;

    /** The integer keys. */
    private ArrayList<String> integerKeys;

    /** The vector keys. */
    private ArrayList<String> vectorKeys;

    /** The long keys. */
    private ArrayList<String> longKeys;

    /** The string keys. */
    private ArrayList<String> stringKeys;

    /** The boolean fields. */
    private HashMap<String,JCheckBox> booleanFields;

    /** The integer fields. */
    private HashMap<String,JComboBox> integerFields;

    /** The vector fields. */
    private HashMap<String,JTextField> vectorFields;

    /** The color fields. */
    private HashMap<String,JTextField> colorFields;

    /** The float fields. */
    private HashMap<String,JTextField> floatFields;

    /** The long fields. */
    private HashMap<String,JTextField> longFields;

    /** The string fields. */
    private HashMap<String,JTextField> stringFields;

    /** The prefs to edit map. */
    private HashMap<String,VSPrefs> prefsToEditMap;

    /** The button panel. */
    private JPanel buttonPanel;

    /** The edit panel. */
    private JPanel editPanel;

    /** The edit table. */
    private VSEditorTable editTable;

    /** The frame. */
    private VSFrame frame;

    /** The expert mode changed. */
    private boolean expertModeChanged;

    /** The prefs. */
    protected VSPrefs prefs;

    /** The prefs to edit. */
    protected VSPrefs prefsToEdit;

    /** The Constant MIN_UNIT_LENGTH. */
    protected static final int MIN_UNIT_LENGTH = 5;

    /** The Constant VALUE_FIELD_COLS. */
    protected static final int VALUE_FIELD_COLS = 9;

    /** The Constant ALL_PREFERENCES. */
    public static final int ALL_PREFERENCES = 0;

    /** The Constant SIMULATION_PREFERENCES. */
    public static final int SIMULATION_PREFERENCES = 1;

    /**
     * Instantiates a new lang.process.removeeditor.
     *
     * @param prefs the prefs
     * @param prefsToEdit the prefs to edit
     */
    public VSEditor(VSPrefs prefs, VSPrefs prefsToEdit) {
        init(prefs, prefsToEdit);
    }

    /**
     * Adds the to button panel front.
     *
     * @param buttonPanel the button panel
     */
    abstract protected void addToButtonPanelFront(JPanel buttonPanel);

    /**
     * Adds the to button panel last.
     *
     * @param buttonPanel the button panel
     */
    abstract protected void addToButtonPanelLast(JPanel buttonPanel);

    /**
     * Adds the to edit table last.
     */
    abstract protected void addToEditTableLast();

    /**
     * Sets the prefs.
     *
     * @param prefs the new prefs
     */
    public void setPrefs(VSPrefs prefs) {
        this.prefs = prefs;
    }

    /**
     * Sets the prefs to edit.
     *
     * @param prefsToEdit the new prefs to edit
     */
    public void setPrefsToEdit(VSPrefs prefsToEdit) {
        this.prefsToEdit = prefsToEdit;
    }

    /**
     * Sets the frame.
     *
     * @param frame the new frame
     */
    public void setFrame(VSFrame frame) {
        this.frame = frame;
    }

    /**
     * Gets the frame.
     *
     * @return the frame
     */
    public VSFrame getFrame() {
        return frame;
    }

    /**
     * Dispose frame if exists.
     */
    protected void disposeFrameIfExists() {
        if (frame != null)
            frame.dispose();
    }

    /**
     * Dispose frame with parent if exists.
     */
    protected void disposeFrameWithParentIfExists() {
        if (frame != null)
            frame.disposeWithParent();
    }

    /**
     * Inits the.
     *
     * @param prefs the prefs
     * @param prefsToEdit the prefs to edit
     */
    private void init(VSPrefs prefs, VSPrefs prefsToEdit) {
        this.prefs = prefs;
        this.prefsToEdit = prefsToEdit;

        editPanel = createEditPanel();
        buttonPanel = createButtonPanel();

        prefsToEditMap = new HashMap<String,VSPrefs>();

        colorFields = new HashMap<String,JTextField>();
        floatFields = new HashMap<String,JTextField>();
        integerFields = new HashMap<String,JComboBox>();
        vectorFields = new HashMap<String,JTextField>();
        longFields = new HashMap<String,JTextField>();
        booleanFields = new HashMap<String,JCheckBox>();
        stringFields = new HashMap<String,JTextField>();

        colorKeys = filterKeys(prefsToEdit.getColorKeySet());
        floatKeys = filterKeys(prefsToEdit.getFloatKeySet());
        integerKeys = filterKeys(prefsToEdit.getIntegerKeySet());
        vectorKeys = filterKeys(prefsToEdit.getVectorKeySet());
        longKeys = filterKeys(prefsToEdit.getLongKeySet());
        booleanKeys = filterKeys(prefsToEdit.getBooleanKeySet());
        stringKeys = filterKeys(prefsToEdit.getStringKeySet());

        fillEditPanel(prefsToEdit);
    }

    /**
     * Filter keys.
     *
     * @param set the set
     *
     * @return the array list< string>
     */
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

    /**
     * Creates the button panel.
     *
     * @return the j panel
     */
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

    /**
     * Creates the unit panel.
     *
     * @param comp the comp
     * @param key the key
     *
     * @return the j panel
     */
    private JPanel createUnitPanel(VSPrefs prefsToEdit, Component comp, String fullKey) {
        JPanel unitPanel = new JPanel(new GridBagLayout());
        unitPanel.setBackground(Color.WHITE);
        unitPanel.setBorder(null);

        String unitText = prefsToEdit.getUnit(fullKey);
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

    /**
     * Creates the edit panel.
     *
     * @return the j panel
     */
    private JPanel createEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setBackground(Color.WHITE);

        editTable = new VSEditorTable(prefs);
        JScrollPane scrollPane = new JScrollPane(editTable);
        editPanel.add(scrollPane);

        return editPanel;
    }

    /**
     * Creates the integer component.
     *
     * @param fullKey the full key
     * @param key the key
     * @param prefsToEdit the prefs to edit
     *
     * @return the lang.process.removetupel< string, component, j combo box>
     */
    protected VSTupel<String,Component,JComboBox> createIntegerComponent(String fullKey, String key, VSPrefs prefsToEdit) {
        String descr = prefsToEdit.getDescription(fullKey);
        String label = descr == null ? fullKey : descr;
        Integer integer = prefsToEdit.getInteger(key);
        Integer initialSelection[] = { integer };
        JComboBox valComboBox = new JComboBox(initialSelection);
        VSPrefs.VSPrefRestriction settingRestriction = prefsToEdit.getRestriction(fullKey);

        int minValue, maxValue;
        if (settingRestriction != null) {
            VSPrefs.VSIntegerPrefRestriction integerVSPrefRestriction =
                (VSPrefs.VSIntegerPrefRestriction) settingRestriction;
            minValue = integerVSPrefRestriction.getMinValue();
            maxValue = integerVSPrefRestriction.getMaxValue();

        } else {
            minValue = 0;
            maxValue = 100;
        }

        for (int i = minValue; i <= maxValue; ++i)
            valComboBox.addItem(new Integer(i));
        valComboBox.setBorder(null);

        return new VSTupel<String,Component,JComboBox>(label,
                createUnitPanel(prefsToEdit, valComboBox, fullKey), valComboBox);
    }

    /**
     * Creates the vector component.
     *
     * @param fullKey the full key
     * @param key the key
     * @param prefsToEdit the prefs to edit
     */
    protected VSTupel<String,Component,JTextField> createVectorComponent(String fullKey, String key, VSPrefs prefsToEdit) {
        String descr = prefsToEdit.getDescription(fullKey);
        String label = descr == null ? fullKey : descr;
        Vector<Integer> vec = prefsToEdit.getVector(key);

        JTextField valField = new JTextField();
        valField.setBorder(null);

        StringBuffer buffer = new StringBuffer();
        buffer.append("[");

        synchronized (vec) {
            for (Integer integer : vec) {
                buffer.append(integer + ",");
            }
        }

        try {
            valField.setText(vec.toString());
            //valField.setText(buffer.toString().substring(0, buffer.length()-1)+"]");
        } catch (StringIndexOutOfBoundsException e) {
            valField.setText("[]");
        }

        return new VSTupel<String,Component,JTextField>(label,
                createUnitPanel(prefsToEdit, valField, fullKey), valField);
    }

    /**
     * Creates the boolean component.
     *
     * @param fullKey the full key
     * @param key the key
     * @param prefsToEdit the prefs to edit
     */
    protected VSTupel<String,Component,JCheckBox> createBooleanComponent(String fullKey, String key, VSPrefs prefsToEdit) {
        final String activated = prefs.getString("lang.activated");
        String descr = prefsToEdit.getDescription(fullKey);
        String label = descr == null ? fullKey : descr;
        JCheckBox valField = new JCheckBox(activated, prefsToEdit.getBoolean(key));
        valField.setBackground(Color.WHITE);
        valField.setBorder(null);
        return new VSTupel<String,Component,JCheckBox>(label,
                createUnitPanel(prefsToEdit, valField, fullKey), valField);
    }

    /**
     * Creates the long component.
     *
     * @param fullKey the full key
     * @param key the key
     * @param prefsToEdit the prefs to edit
     *
     * @return the lang.process.removetupel< string, component, j text field>
     */
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
        return new VSTupel<String,Component,JTextField>(label,
                createUnitPanel(prefsToEdit, valField, fullKey), valField);
    }

    /**
     * Creates the float component.
     *
     * @param fullKey the full key
     * @param key the key
     * @param prefsToEdit the prefs to edit
     *
     * @return the lang.process.removetupel< string, component, j text field>
     */
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
        return new VSTupel<String,Component,JTextField>(label,
                createUnitPanel(prefsToEdit, valField, fullKey), valField);
    }

    /**
     * Creates the color component.
     *
     * @param fullKey the full key
     * @param key the key
     * @param prefsToEdit the prefs to edit
     *
     * @return the lang.process.removetupel< string, component, j text field>
     */
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
        return new VSTupel<String,Component,JTextField>(label,
                createUnitPanel(prefsToEdit, valField, fullKey), valField);
    }

    /**
     * Creates the string component.
     *
     * @param fullKey the full key
     * @param key the key
     * @param prefsToEdit the prefs to edit
     *
     * @return the lang.process.removetupel< string, component, j text field>
     */
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
        return new VSTupel<String,Component,JTextField>(label,
                createUnitPanel(prefsToEdit, valField, fullKey), valField);
    }

    /**
     * Fill edit panel.
     *
     * @param prefsToEdit the prefs to edit
     */
    private void fillEditPanel(VSPrefs prefsToEdit) {
        HashMap<String,Component> components = new HashMap<String,Component>();
        HashMap<String,String> labels = new HashMap<String,String>();

        for (String key : integerKeys) {
            String fullKey = VSPrefs.INTEGER_PREFIX + key;
            VSTupel<String,Component,JComboBox> tupel =
                createIntegerComponent(fullKey, key, prefsToEdit);
            labels.put(fullKey, tupel.getA());
            components.put(fullKey, tupel.getB());
            integerFields.put(key, tupel.getC());
        }

        for (String key : vectorKeys) {
            String fullKey = VSPrefs.VECTOR_PREFIX + key;
            VSTupel<String,Component,JTextField> tupel =
                createVectorComponent(fullKey, key, prefsToEdit);
            labels.put(fullKey, tupel.getA());
            components.put(fullKey, tupel.getB());
            vectorFields.put(key, tupel.getC());
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

    /**
     * Adds the to editor.
     *
     * @param label the label
     * @param prefsKey the prefs key
     * @param prefsToAdd the prefs to add
     */
    protected void addToEditor(String label, String prefsKey, VSPrefs prefsToAdd) {
        addSeparator(label);
        prefsKey = "(" + prefsKey + ")";

        ArrayList<String> fullKeys = new ArrayList<String>();

        Set<String> integerKeys = prefsToAdd.getIntegerKeySet();
        Set<String> vectorKeys = prefsToAdd.getVectorKeySet();
        Set<String> floatKeys = prefsToAdd.getFloatKeySet();
        Set<String> longKeys = prefsToAdd.getLongKeySet();
        Set<String> booleanKeys = prefsToAdd.getBooleanKeySet();
        Set<String> stringKeys = prefsToAdd.getStringKeySet();

        for (String key : integerKeys) fullKeys.add(VSPrefs.INTEGER_PREFIX + key);
        for (String key : vectorKeys) fullKeys.add(VSPrefs.VECTOR_PREFIX + key);
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

            } else if (fullKey.startsWith(VSPrefs.VECTOR_PREFIX)) {
                VSTupel<String,Component,JTextField> tupel =
                    createVectorComponent(fullKey, key, prefsToAdd);
                this.vectorKeys.add(prefsKey+key);
                this.vectorFields.put(prefsKey+key, tupel.getC());
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

    /**
     * Adds the separator.
     *
     * @param label the label
     */
    private void addSeparator(String label) {
        editTable.addSeparator(label);
    }

    /**
     * Adds the variable.
     *
     * @param label the label
     * @param component the component
     * @param prefs the prefs
     */
    private void addVariable(String label, Component component, VSPrefs prefs) {
        addVariable("", label, component, prefs);
    }

    /**
     * Adds the variable.
     *
     * @param prefsKey the prefs key
     * @param label the label
     * @param component the component
     * @param prefs the prefs
     */
    private void addVariable(String prefsKey, String label, Component component, VSPrefs prefs) {
        prefsToEditMap.put(prefsKey, prefs);
        editTable.addVariable(label, component);
    }

    /**
     * Reset edit panel.
     */
    protected void resetPrefs() {
        for (String key : integerKeys) {
            JComboBox valComboBox = integerFields.get(key);
            valComboBox.setSelectedIndex(0);
        }

        for (String key : booleanKeys) {
            String keys[] = getKeys(key);
            JCheckBox valField = booleanFields.get(key);
            valField.setSelected(prefsToEditMap.get(keys[1]).getBoolean(keys[0]));
        }

        for (String key : vectorKeys) {
            String keys[] = getKeys(key);
            JTextField valField = vectorFields.get(key);
            valField.setText(""+prefsToEditMap.get(keys[1]).getVector(keys[0]));
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
     * Gets the keys.
     *
     * @param key the key
     *
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

    /**
     * Save prefs.
     */
    protected void savePrefs() {
        boolean expertMode = prefs.getBoolean("sim.mode.expert");

        for (String key : integerKeys) {
            String keys[] = getKeys(key);
            JComboBox valComboBox = integerFields.get(key);
            prefsToEditMap.get(keys[1]).setInteger(keys[0], (Integer) valComboBox.getSelectedItem());
        }

        for (String key : vectorKeys) {
            String keys[] = getKeys(key);
            JTextField valField = vectorFields.get(key);

            try {
                String val = valField.getText();
                Vector<Integer> vec = utils.VSTools.parseIntegerVector(val);
                prefsToEditMap.get(keys[1]).setVector(keys[0], vec);
            } catch (exceptions.ParseIntegerVectorException e) {
            }

            valField.setText(""+prefsToEditMap.get(keys[1]).getVector(keys[0]));
        }

        for (String key : booleanKeys) {
            String keys[] = getKeys(key);
            JCheckBox valField = booleanFields.get(key);
            prefsToEditMap.get(keys[1]).setBoolean(keys[0], valField.isSelected());
        }

        for (String key : floatKeys) {
            String keys[] = getKeys(key);
            JTextField valField = floatFields.get(key);

            try {
                Float val = Float.valueOf(valField.getText());
                prefsToEditMap.get(keys[1]).setFloat(keys[0], val);

            } catch (NumberFormatException e) {
                valField.setText(""+prefsToEditMap.get(keys[1]).getFloat(keys[0]));
            }
        }

        for (String key : longKeys) {
            String keys[] = getKeys(key);
            JTextField valField = longFields.get(key);

            try {
                Long val = Long.valueOf(valField.getText());
                prefsToEditMap.get(keys[1]).setLong(keys[0], val);

            } catch (NumberFormatException e) {
                valField.setText(""+prefsToEditMap.get(keys[1]).getLong(keys[0]));
            }
        }

        for (String key : colorKeys) {
            String keys[] = getKeys(key);
            JTextField valField = colorFields.get(key);
            prefsToEditMap.get(keys[1]).setColor(keys[0], valField.getBackground());
        }

        for (String key : stringKeys) {
            String keys[] = getKeys(key);
            JTextField valField = stringFields.get(key);
            prefsToEditMap.get(keys[1]).setString(keys[0], valField.getText());
        }

        expertModeChanged = expertMode != prefs.getBoolean("sim.mode.expert");
    }

    /**
     * Expert mode changed.
     *
     * @return true, if successful
     */
    public boolean expertModeChanged() {
        boolean ret = expertModeChanged;

        if (expertModeChanged)
            expertModeChanged = false;

        return ret;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(prefs.getString("lang.takeover"))) {
            savePrefs();

        } else if (actionCommand.equals(prefs.getString("lang.reset"))) {
            resetPrefs();
        }
    }

    /**
     * Gets the edits the panel.
     *
     * @return the edits the panel
     */
    public JPanel getEditPanel() {
        return editPanel;
    }

    /**
     * Gets the edits the table.
     *
     * @return the edits the table
     */
    public VSEditorTable getEditTable() {
        return editTable;
    }

    /**
     * Gets the button panel.
     *
     * @return the button panel
     */
    public JPanel getButtonPanel() {
        return buttonPanel;
    }
}
