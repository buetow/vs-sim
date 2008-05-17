package events;

import core.VSProcess;
import prefs.VSPrefs;

abstract public class VSProcessEvent implements VSEvent {
    protected VSProcess process;
    protected VSPrefs prefs;

    public void init(VSProcess process) {
        this.process = process;
        this.prefs = process.getPrefs();
    }

    abstract public void onStart();
}
