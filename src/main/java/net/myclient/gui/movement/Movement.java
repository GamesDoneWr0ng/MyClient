package net.myclient.gui.movement;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.myclient.gui.Gui;

public class Movement extends Gui {
    public Movement(boolean showMenu, Screen parent) {
        super(showMenu, null);
        this.parent = parent;
    }

    @Override
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(2);

        setScreen(adder, new FlyGui(showMenu, this.hackManager.fly, this));
        addToggle(adder, this.hackManager.noFall);
        addToggle(adder, this.hackManager.antiVelocity);
        addToggle(adder, this.hackManager.antiHunger);
        setScreen(adder, new FreeCamGui(showMenu, this.hackManager.freeCam, this));

        gridWidget.recalculateDimensions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.25f);
        this.addDrawableChild(gridWidget);
    }
}
