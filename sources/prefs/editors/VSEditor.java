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

abstract class VSEditor implements ActionListener {
    protected static final int LABEL_FIELD_COLS = 18;
    protected static final int VALUE_FIELD_COLS = 7;
    protected int prefsCategory;
    private HashMap<String,JComboBox> integerFields;
    private HashMap<String,JTextField> colorFields;
    private HashMap<String,JTextField> floatFields;
    private HashMap<String,JTextField> longFields;
    private HashMap<String,JCheckBox> booleanFields;
    private HashMap<String,JTextField> stringFields;
    private Vector<String> colorKeys;
    private Vector<String> floatKeys;
    private Vector<String> integerKeys;
    private Vector<String> longKeys;
    private Vector<String> booleanKeys;
    private Vector<String> stringKeys;
    protected JPanel buttonPanel;
    protected JPanel editPanel;
    protected VSPrefs prefs;
    protected VSPrefs prefsToEdit;
    public static final int ALL_PREFERENCES = 0;
    public static final int SIMULATION_PREFERENCES = 1;
    protected GridBagConstraints editPanelConstraints;
    protected int editPanelRow;
    //protected Insets insetsTopSpaceing = new Insets(15, 0, 0, 0);
    protected Insets insetsTopSpaceing = new Insets(0, 0, 0, 0);
    protected Insets insets = new Insets(0, 0, 0, 0);
    private VSFrame frame;

    public VSEditor(VSPrefs prefs, VSPrefs prefsToEdit) {
        init(prefs, prefsToEdit, SIMULATION_PREFERENCES);
    }

    public VSEditor(VSPrefs prefs, VSPrefs prefsToEdit, int prefsCategory) {
        init(prefs, prefsToEdit, prefsCategory);
    }

    public int getPrefsCategory() {
        return prefsCategory;
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

    private void init(VSPrefs prefs, VSPrefs prefsToEdit, int prefsCategory) {
        this.prefs = prefs;
        this.prefsToEdit = prefsToEdit;
        this.prefsCategory = prefsCategory;

        final String keyStartsWith = "sim.";
        boolean reversed;

        switch (prefsCategory) {
        case SIMULATION_PREFERENCES:
            reversed = false;
            break;
        default:
            reversed = true;
        }

        colorKeys = setToSortedVector(prefsToEdit.getColorKeySet(), keyStartsWith, reversed);
        floatKeys = setToSortedVector(prefsToEdit.getFloatKeySet(), keyStartsWith, reversed);
        integerKeys = setToSortedVector(prefsToEdit.getIntegerKeySet(), keyStartsWith, reversed);
        longKeys = setToSortedVector(prefsToEdit.getLongKeySet(), keyStartsWith, reversed);
        booleanKeys = setToSortedVector(prefsToEdit.getBooleanKeySet(), keyStartsWith, reversed);
        stringKeys = setToSortedVector(prefsToEdit.getStringKeySet(), keyStartsWith, reversed);

        colorFields = new HashMap<String,JTextField>();
        floatFields = new HashMap<String,JTextField>();
        integerFields = new HashMap<String,JComboBox>();
        longFields = new HashMap<String,JTextField>();
        booleanFields = new HashMap<String,JCheckBox>();
        stringFields = new HashMap<String,JTextField>();

        editPanel = createEditPanel();
        buttonPanel = createButtonPanel();
    }

    private Vector<String> setToSortedVector(Set<String> set, String startsWith, boolean reversed) {
        Vector<String> vector = new Vector<String>();

        if (reversed) {
            for (String elem : set)
                if (!elem.startsWith(startsWith) && !elem.endsWith("!") && !elem.startsWith("keyevent"))
                    vector.add(elem);
        } else {
            for (String elem : set)
                if (elem.startsWith(startsWith) && !elem.endsWith("!") && !elem.startsWith("keyevent"))
                    vector.add(elem);
        }

        Collections.sort(vector);

        return vector;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton saveButton = new JButton(
            prefs.getString("lang.ok"));
        saveButton.setMnemonic(prefs.getInteger("keyevent.ok"));
        saveButton.addActionListener(this);
        buttonPanel.add(saveButton);

        JButton resetButton = new JButton(
            prefs.getString("lang.reset"));
        resetButton.setMnemonic(prefs.getInteger("keyevent.reset"));
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);

        return buttonPanel;
    }

    abstract protected void addToEditPanelFront(JPanel editPanel);

    abstract protected void addToEditPanelLast(JPanel editPanel);

    private JPanel createUnitPanel(Component comp, String key) {
        JPanel unitPanel = new JPanel(new GridBagLayout());
        unitPanel.setBackground(Color.WHITE);

        String unitText = prefs.getUnit(key);
        if (unitText == null)
            unitText = "";

        JLabel unitLabel = new JLabel(" " + unitText);

        unitPanel.setLayout(new BoxLayout(unitPanel, BoxLayout.X_AXIS));
        unitPanel.add(comp);
        unitPanel.add(unitLabel);

        return unitPanel;
    }

    private JPanel createEditPanel() {
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBackground(Color.WHITE);

        editPanelConstraints = new GridBagConstraints();
        editPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        editPanelConstraints.ipady = 10;//15;
        editPanelConstraints.ipadx = 10;//15;
        editPanelRow = 0;

        addToEditPanelFront(editPanel);

        for (String key : integerKeys) {
            String fullKey = VSPrefs.INTEGER_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);

            JTextField keyLabel = new JTextField(LABEL_FIELD_COLS);;
            keyLabel.setEditable(false);
            keyLabel.setBackground(Color.WHITE);

            if (descr == null)
                keyLabel.setText(fullKey);
            else
                keyLabel.setText(descr);

            editPanelConstraints.insets = insetsTopSpaceing;
            editPanelConstraints.gridy = editPanelRow++;
            editPanel.add(keyLabel, editPanelConstraints);
            editPanelConstraints.insets = insets;

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

            valComboBox.repaint();

            //JPanel pane = new JPanel(new BorderLayout());
            //pane.setBackground(Color.WHITE);
            //pane.add(createUnitPanel(valComboBox, fullKey), BorderLayout.WEST);

            editPanelConstraints.insets = insets;
            editPanelConstraints.gridx = 1;
            editPanel.add(createUnitPanel(valComboBox, fullKey), editPanelConstraints);
            integerFields.put(key, valComboBox);
            editPanelConstraints.gridy = editPanelRow++;
            editPanelConstraints.gridx = 0;
        }

        final String activated = prefs.getString("lang.activated");
        for (String key : booleanKeys) {
            String fullKey = VSPrefs.BOOLEAN_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);

            JTextField keyLabel = new JTextField(LABEL_FIELD_COLS);
            keyLabel.setEditable(false);
            keyLabel.setBackground(Color.WHITE);
            if (descr == null)
                keyLabel.setText(fullKey);
            else
                keyLabel.setText(descr);

            editPanelConstraints.insets = insetsTopSpaceing;
            editPanelConstraints.gridy = editPanelRow++;
            editPanel.add(keyLabel, editPanelConstraints);

            JCheckBox valField = new JCheckBox(activated, prefsToEdit.getBoolean(key));
            valField.setBackground(Color.WHITE);

            JPanel pane = new JPanel(new BorderLayout());
            pane.setBackground(Color.WHITE);
            pane.add(createUnitPanel(valField, fullKey), BorderLayout.WEST);

            editPanelConstraints.insets = insets;
            editPanelConstraints.gridx = 1;
            editPanel.add(pane, editPanelConstraints);
            editPanelConstraints.gridx = 0;
            editPanelConstraints.gridy = editPanelRow++;
            booleanFields.put(key, valField);
        }

        for (String key : longKeys) {
            String fullKey = VSPrefs.LONG_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);

            JTextField keyLabel = new JTextField(LABEL_FIELD_COLS);
            keyLabel.setEditable(false);
            keyLabel.setBackground(Color.WHITE);
            if (descr == null)
                keyLabel.setText(fullKey);
            else
                keyLabel.setText(descr);

            editPanelConstraints.insets = insetsTopSpaceing;
            editPanelConstraints.gridy = editPanelRow++;
            editPanel.add(keyLabel, editPanelConstraints);

            JTextField valField = new JTextField(VALUE_FIELD_COLS);
            valField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    JTextField valField = (JTextField)e.getSource();
                    if (valField.getText().length() >= valField.getColumns() + 10)
                        e.consume();
                }
            });
            valField.setText(""+prefsToEdit.getLong(key));

            JPanel pane = new JPanel(new BorderLayout());
            pane.setBackground(Color.WHITE);
            pane.add(createUnitPanel(valField, fullKey), BorderLayout.WEST);

            editPanelConstraints.insets = insets;
            editPanelConstraints.gridx = 1;
            editPanel.add(pane, editPanelConstraints);
            editPanelConstraints.gridx = 0;
            editPanelConstraints.gridy = editPanelRow++;
            longFields.put(key, valField);
        }


        for (String key : floatKeys) {
            String fullKey = VSPrefs.FLOAT_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);

            JTextField keyLabel = new JTextField(LABEL_FIELD_COLS);
            keyLabel.setEditable(false);
            keyLabel.setBackground(Color.WHITE);
            if (descr == null)
                keyLabel.setText(fullKey);
            else
                keyLabel.setText(descr);

            editPanelConstraints.insets = insetsTopSpaceing;
            editPanelConstraints.gridy = editPanelRow++;
            editPanel.add(keyLabel, editPanelConstraints);

            JTextField valField = new JTextField(VALUE_FIELD_COLS);
            valField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    JTextField valField = (JTextField)e.getSource();
                    if (valField.getText().length() >= valField.getColumns() + 10)
                        e.consume();
                }
            });
            valField.setText(""+prefsToEdit.getFloat(key));

            JPanel pane = new JPanel(new BorderLayout());
            pane.setBackground(Color.WHITE);
            pane.add(createUnitPanel(valField, fullKey), BorderLayout.WEST);

            editPanelConstraints.insets = insets;
            editPanelConstraints.gridx = 1;
            editPanel.add(pane, editPanelConstraints);
            editPanelConstraints.gridx = 0;
            editPanelConstraints.gridy = editPanelRow++;
            floatFields.put(key, valField);
        }


        for (String key : colorKeys) {
            String fullKey = VSPrefs.COLOR_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);

            JTextField keyLabel = new JTextField(LABEL_FIELD_COLS);
            keyLabel.setEditable(false);
            keyLabel.setBackground(Color.WHITE);
            if (descr == null)
                keyLabel.setText(fullKey);
            else
                keyLabel.setText(descr);

            editPanelConstraints.insets = insetsTopSpaceing;
            editPanelConstraints.gridy = editPanelRow++;
            editPanel.add(keyLabel, editPanelConstraints);

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
                        prefs.getString("name") + " - " +
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

            editPanelConstraints.insets = insets;
            editPanelConstraints.gridx = 1;
            editPanel.add(valField, editPanelConstraints);
            editPanelConstraints.gridx = 0;
            editPanelConstraints.gridy = editPanelRow++;
            colorFields.put(key, valField);
        }

        for (String key : stringKeys) {
            String fullKey = VSPrefs.STRING_PREFIX + key;
            String descr = prefsToEdit.getDescription(fullKey);

            JTextField keyLabel = new JTextField(LABEL_FIELD_COLS);
            keyLabel.setEditable(false);
            keyLabel.setBackground(Color.WHITE);
            if (descr == null)
                keyLabel.setText(fullKey);
            else
                keyLabel.setText(descr);

            editPanelConstraints.insets = insetsTopSpaceing;
            editPanelConstraints.gridy = editPanelRow++;
            editPanel.add(keyLabel, editPanelConstraints);

            JTextField valField = new JTextField(VALUE_FIELD_COLS);
            valField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    JTextField valField = (JTextField)e.getSource();
                    if (valField.getText().length() >= valField.getColumns() + 10)
                        e.consume();
                }
            });
            valField.setText(prefsToEdit.getString(key));

            editPanelConstraints.insets = insets;
            editPanelConstraints.gridx = 1;
            editPanel.add(createUnitPanel(valField, fullKey), editPanelConstraints);
            editPanelConstraints.gridx = 0;
            editPanelConstraints.gridy = editPanelRow++;
            stringFields.put(key, valField);
        }

        addToEditPanelLast(editPanel);

        return editPanel;
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

        if (actionCommand.equals(prefs.getString("lang.ok"))) {
            savePrefs();

        } else if (actionCommand.equals(prefs.getString("lang.save"))) {
            savePrefs();
            prefs.saveFile();

        } else if (actionCommand.equals(prefs.getString("lang.reset"))) {
            resetEditPanel();

        } else if (actionCommand.equals(prefs.getString("lang.default"))) {
            prefs.fillWithDefaults();
            resetEditPanel();
        }
    }

    public JPanel getEditPanel() {
        return editPanel;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }
}
