package net.myclient.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;

public class Movement extends Gui{
    public Movement(boolean showMenu, Screen parent) {
        super(showMenu);
        this.parent = parent;
    }

    @Override
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(2);

        adder.add(ButtonWidget.builder(Text.literal("Fly"), button -> this.hackManager.fly.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("NoFall"), button -> this.hackManager.noFall.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("AntiVelocity"), button -> this.hackManager.anitVelocity.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("AntiHunger"), button -> this.hackManager.antiHunger.swich()).width(100).build());
        adder.add(ButtonWidget.builder(Text.literal("FreeCam"), button -> this.hackManager.freeCam.swich()).width(100).build());

        gridWidget.recalculateDimensions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.25f);
        this.addDrawableChild(gridWidget);
    }
}
