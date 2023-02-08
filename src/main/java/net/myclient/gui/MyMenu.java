package net.myclient.gui;

import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.myclient.gui.combat.Combat;
import net.myclient.gui.farm.Farm;
import net.myclient.gui.misc.Misc;
import net.myclient.gui.movement.Movement;
import net.myclient.gui.world.World;

public class MyMenu extends Gui {
    public MyMenu(boolean showMenu) {
        super(showMenu, null);
    }

    @Override
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(1);

        setScreen(adder, new Movement(showMenu, this));
        setScreen(adder, new Combat(showMenu, this));
        setScreen(adder, new Farm(showMenu, this));
        setScreen(adder, new World(showMenu, this));
        setScreen(adder, new Misc(showMenu, this));

        gridWidget.recalculateDimensions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.25f);
        this.addDrawableChild(gridWidget);
    }
}
