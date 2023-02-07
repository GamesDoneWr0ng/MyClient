package net.myclient.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import net.myclient.Main;
import net.myclient.hack.Hack;
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
        assert this.client != null;
        this.client.setScreen(this.parent);
    }

    public void add(GridWidget.Adder adder, Hack hack) {
        adder.add(ButtonWidget.builder(Text.literal(hack.getClass()+ (hack.isEnabled() ? ": On" : ": Off")), button -> hack.swich()).width(100).build());
    }

    public void setScreen(GridWidget.Adder adder, Screen screen) {
        assert this.client != null;
        adder.add(ButtonWidget.builder(Text.literal(screen.getClass().toString()), button -> this.client.setScreen(screen)).width(200).build());
    }

    @Override
    protected void init() {
        if (this.showMenu) {
            this.initWidgets();
        }
    }

    protected abstract void initWidgets();
}