package events.implementations;

import events.VSEvent;

public class ProcessRecoverEvent extends VSEvent {
    protected void onInit() {
        setClassname(getClass().toString());
    }

    public void onStart() {
        if (process.isCrashed()) {
            process.isCrashed(false);
            logg(prefs.getString("lang.recovered"));
        }
    }
}
