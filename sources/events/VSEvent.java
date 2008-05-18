package events;

import core.VSProcess;

public interface VSEvent {
    public void init(VSProcess process);
    public void logg(String message);
}
