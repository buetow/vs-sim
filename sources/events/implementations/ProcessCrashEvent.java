package events.implementations;

import core.VSProcess;
import events.VSEvent;

public class ProcessCrashEvent extends VSEvent {
    protected void onInit() {
        setClassname(getClass().toString());
    }

    public void onStart() {
        if (!process.isCrashed()) {
            process.isCrashed(true);
            logg(prefs.getString("lang.crashed"));
        }
    }
}
