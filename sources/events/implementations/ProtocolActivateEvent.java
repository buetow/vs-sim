package events.implementations;

import core.VSProcess;
import events.VSProcessEvent;

public class ProcessRecoverEvent extends VSProcessEvent {
    public void onStart() {
        process.logg(prefs.getString("lang.recovered"));
        process.isCrashed(false);
    }
}
