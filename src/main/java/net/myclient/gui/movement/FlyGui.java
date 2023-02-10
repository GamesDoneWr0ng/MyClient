package net.myclient.gui.movement;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.myclient.gui.Gui;
import net.myclient.hacks.Fly;

public class FlyGui extends Gui {
    public FlyGui(boolean showMenu, Fly hack, Screen parent) {
        super(showMenu, hack);
        this.parent = parent;
    }

    @Override
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(1);
        Fly fly = (Fly) hack;

        addToggle(adder, fly);
        addSlider(adder, fly.speed);
        addSlider(adder, fly.sprintMultiplier);

        gridWidget.recalculateDimensions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.25f);
        this.addDrawableChild(gridWidget);
    }
}
