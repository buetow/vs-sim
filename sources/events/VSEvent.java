package events;

import core.VSProcess;
import prefs.VSPrefs;

abstract public class VSEvent extends VSPrefs {
    protected VSPrefs prefs;
    protected VSProcess process;
    private String eventClassname;

    public void init(VSProcess process) {
        this.process = process;
        this.prefs = process.getPrefs();
    }

    protected final void setClassname(String eventClassname) {
        if (eventClassname.startsWith("class "))
            eventClassname = eventClassname.substring(6);

        this.eventClassname = eventClassname;
    }

    public final String getClassname() {
        return eventClassname;
    }

    public String getName() {
        return VSRegisteredEvents.getName(eventClassname);
    }

    public final String getShortname() {
        return VSRegisteredEvents.getShortname(eventClassname);
    }

    public final VSProcess getProcess() {
        return process;
    }

    public void logg(String message) {
        process.logg(toString() + "; " + message);
    }

    public boolean equals(VSEvent event) {
        return super.getID() == event.getID();
    }

    abstract protected void onInit();
    abstract public void onStart();
}
