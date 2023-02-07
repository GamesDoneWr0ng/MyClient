package net.myclient.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.myclient.Main;
import net.myclient.hack.HackManager;

public abstract class Gui extends Screen {
    public final boolean showMenu;
    public Screen parent = null;
    public final HackManager hackManager = Main.INSTANCE.getHackManager();

    public Gui(boolean showMenu) {
        super((showMenu ? Text.literal("Game") : Text.literal("Paused")));
        this.showMenu = showMenu;
    }

    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    protected void init() {
        if (this.showMenu) {
            this.initWidgets();
        }
    }

    protected abstract void initWidgets();
}