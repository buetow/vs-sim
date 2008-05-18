package events.implementations;

import core.VSProcess;
import events.VSProcessEvent;

public class ProcessRecoverEvent extends VSProcessEvent {
    public void onStart() {
        process.isCrashed(false);
        logg(prefs.getString("lang.recovered"));
    }
}
