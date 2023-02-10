package net.myclient.gui.combat;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.myclient.gui.Gui;
import net.myclient.hacks.KillAura;

public class KillAuraGui extends Gui {
    public KillAuraGui(boolean showMenu, KillAura hack, Screen parent) {
        super(showMenu, hack);
        this.parent = parent;
    }

    @Override
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(1);
        KillAura killAura = (KillAura) hack;

        addToggle(adder, killAura);
        addCycle(adder, killAura.target);
        addCycle(adder, killAura.mobTarget);

        gridWidget.recalculateDimensions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.25f);
        this.addDrawableChild(gridWidget);
    }
}
