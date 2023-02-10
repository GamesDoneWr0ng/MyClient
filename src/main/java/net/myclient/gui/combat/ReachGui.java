package net.myclient.gui.combat;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.myclient.gui.Gui;
import net.myclient.hacks.Reach;

public class ReachGui extends Gui {
    public ReachGui(boolean showMenu, Reach hack, Screen parent) {
        super(showMenu, hack);
        this.parent = parent;
    }

    @Override
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(1);
        Reach reach = (Reach) hack;

        addToggle(adder, reach);
        addSlider(adder, reach.aimAssist);
        addSlider(adder, reach.maxTpDistance);
        addCycle(adder, reach.target);
        addCycle(adder, reach.mobTarget);

        gridWidget.recalculateDimensions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.25f);
        this.addDrawableChild(gridWidget);
    }
}
