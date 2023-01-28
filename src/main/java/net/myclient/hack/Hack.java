package net.myclient.hack;

public abstract class Hack extends Feature {
    private boolean enabled;
    @Override
    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled)
    {
        if(this.enabled == enabled)
            return;
        this.enabled = enabled;

        if(enabled)
            onEnable();
        else
            onDisable();
    }
    public final void swich(){
        this.setEnabled(!this.isEnabled());
    }
    
    protected void onEnable() {}
    protected void onDisable() {}
}