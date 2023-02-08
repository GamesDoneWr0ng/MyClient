package net.myclient.gui.movement;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.myclient.gui.Gui;
import net.myclient.hack.Hack;

public class FreeCamGui extends Gui {
    public FreeCamGui(boolean showMenu, Hack hack, Screen parent) {
        super(showMenu, hack);
        this.parent = parent;
    }

    @Override
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(1);

        addToggle(adder, this.hackManager.freeCam);
        addSlider(adder, this.hackManager.freeCam.speed);
        addSlider(adder, this.hackManager.freeCam.sprintMultiplier);

        gridWidget.recalculateDimensions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.25f);
        this.addDrawableChild(gridWidget);
    }
}
