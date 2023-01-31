package net.myclient.gui;

import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;
import net.myclient.Main;
import net.myclient.hack.HackManager;

public class MyMenu extends Screen {
    private final boolean showMenu;
    private final HackManager hackManager = Main.INSTANCE.getHackManager();

    public MyMenu(boolean showMenu) {
        super((showMenu ? Text.literal("Game") : Text.literal("Paused")));
        this.showMenu = showMenu;
    }

    @Override
    protected void init() {
        if (this.showMenu) {
            this.initWidgets();
        }
    }

    private void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(2);

        adder.add(ButtonWidget.builder(Text.literal("PacketLogger"), button -> this.hackManager.packetLogger.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("AutoFish"), button -> this.hackManager.autoFish.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("Fly"), button -> this.hackManager.fly.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("NoFall"), button -> this.hackManager.noFall.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("AntiVelocity"), button -> this.hackManager.anitVelocity.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("FastBreak"), button -> this.hackManager.fastBreak.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("AntiHunger"), button -> this.hackManager.antiHunger.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("AntiParticle"), button -> this.hackManager.antiParticle.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("FreeCam"), button -> this.hackManager.freeCam.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("Reach"), button -> this.hackManager.reach.swich()).width(100).build());

        gridWidget.recalculateDimensions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.25f);
        this.addDrawableChild(gridWidget);
    }
}
