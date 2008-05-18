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

    public void logg(String message) {
        process.logg(message);
    }

    abstract public void onStart();
}
