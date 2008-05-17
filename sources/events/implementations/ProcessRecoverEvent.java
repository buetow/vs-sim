package events.implementations;

import core.VSProcess;

public class ProcessRecoverEvent extends VSProcessEvent {
    public void onStart(VSProcess process) {
        process.logg("Recovered");
        process.isCrashed(false);
    }
}
