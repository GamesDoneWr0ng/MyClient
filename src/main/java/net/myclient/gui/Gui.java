package net.myclient.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.realms.gui.screen.RealmsSlotOptionsScreen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.myclient.Main;
import net.myclient.hack.Hack;
import net.myclient.hack.HackManager;
import net.myclient.settings.SliderSetting;

public abstract class Gui extends Screen {
    public final boolean showMenu;
    public Screen parent = null;
    public Hack hack;
    public final HackManager hackManager = Main.INSTANCE.getHackManager();

    public Gui(boolean showMenu, Hack hack) {
        super((showMenu ? Text.literal("Game") : Text.literal("Paused")));
        this.showMenu = showMenu;
        this.hack = hack;
    }

    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }

    public void addToggle(GridWidget.Adder adder, Hack hack) {
        String s = hack.getClass().toString();
        adder.add(CyclingButtonWidget.builder(
                Text::literal).values("On", "Off").initially(hack.isEnabled() ? "On" : "Off")
                .build(1,1,100,20,
                        Text.literal(s.substring(s.lastIndexOf(".") + 1).trim())
                        , (button, value) -> hack.swich()));
    }

    public void addSlider(GridWidget.Adder adder, SliderSetting setting) {
        adder.add(new SliderWidget(1, 1, 100, 20, Text.literal(setting.getName()), (setting.getValue() - setting.getMinimum()) / setting.getRange()) {
            @Override
            protected void updateMessage() {}
            @Override
            protected void applyValue() {
                setting.setValue((value + setting.getMinimum()) * setting.getRange());
            }
        });
    }

    public void setScreen(GridWidget.Adder adder, Gui screen) {
        assert this.client != null;
        String s = screen.getClass().toString();
        if (screen.hack != null) {
            s = s.substring(0, s.length() - 3) + (screen.hack.isEnabled() ? ": On" : ": Off");
        }
        adder.add(ButtonWidget.builder(Text.literal(s.substring(s.lastIndexOf(".") + 1)), button -> this.client.setScreen(screen)).width(100).build());
    }

    @Override
    protected void init() {
        if (this.showMenu) {
            this.initWidgets();
        }
    }

    protected abstract void initWidgets();
}