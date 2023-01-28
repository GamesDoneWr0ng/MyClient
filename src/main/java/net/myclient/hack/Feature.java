package net.myclient.hack;

import net.myclient.Main;
import net.myclient.event.EventManager;

public abstract class Feature
{
    protected static final Main MAIN = Main.INSTANCE;
    protected static final EventManager EVENTS = MAIN.getEventManager();
    public boolean isEnabled() {return false;}
}