package events.implementations;

import core.VSProcess;
import events.VSProcessEvent;

public class ProcessCrashEvent extends VSProcessEvent {
    public void onStart() {
        process.logg(prefs.getString("lang.crashed"));
        process.isCrashed(true);
    }
}
