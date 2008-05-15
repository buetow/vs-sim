package events;

import core.VSProcess;

public class ProcessCrashEvent extends VSProcessEvent {
    public void onStart(VSProcess process) {
        process.logg("Crashed");
        process.isCrashed(true);
    }
}
