/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package prefs.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import prefs.*;
import utils.*;

// TODO: Auto-generated Javadoc
/**
 * The class VSAbstractBetterEditor.
 */
public abstract class VSAbstractBetterEditor extends VSAbstractEditor {
    private static final long serialVersionUID = 1L;

    /** The content pane. */
    private Container contentPane;

    /** The info area. */
    private VSInfoArea infoArea;

    /** The title. */
    private String title;

    /**
     * Instantiates a new lang.process.removebetter editor.
     *
     * @param prefs the prefs
     * @param prefsToEdit the prefs to edit
     * @param title the title
     */
    public VSAbstractBetterEditor(VSPrefs prefs, VSPrefs prefsToEdit, String title) {
        super(prefs, prefsToEdit);
        this.title = title;
        this.contentPane = createContentPane();
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the content pane.
     *
     * @return the content pane
     */
    public Container getContentPane() {
        contentPane.setBackground(Color.WHITE);
        return contentPane;
    }

    /**
     * Creates the content pane.
     *
     * @return the j panel
     */
    private JPanel createContentPane() {
        JPanel panel  = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        infoArea = new VSInfoArea();
        JPanel editPanel = getEditPanel();
        JPanel buttonPanel = getButtonPanel();

        JScrollPane scrollPane = new JScrollPane(editPanel);
        panel.add(editPanel);
        //panel.add(infoArea);
        panel.add(buttonPanel);

        return panel;
    }

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractEditor#addToButtonPanelFront(javax.swing.JPanel)
     */
    protected void addToButtonPanelFront(JPanel buttonPanel) { }

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractEditor#addToButtonPanelLast(javax.swing.JPanel)
     */
    protected void addToButtonPanelLast(JPanel buttonPanel) { }

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractEditor#addToEditTableLast()
     */
    protected void addToEditTableLast() { }

    /* (non-Javadoc)
     * @see prefs.editors.VSAbstractEditor#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        //String actionCommand = e.getActionCommand();

        /* More action in the super class!!! */
        super.actionPerformed(e);
    }

    /**
     * Gets the info area.
     *
     * @return the info area
     */
    protected VSInfoArea getInfoArea() {
        return infoArea;
    }
}
