package events;

import core.VSProcess;

abstract public class VSProcessEvent implements VSEvent {
    abstract public void onStart(VSProcess process);
}
