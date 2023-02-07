package net.myclient.gui;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;

public class MyMenu extends Gui {
    public MyMenu(boolean showMenu) {
        super(showMenu);
    }

    @Override
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(1);

        adder.add(ButtonWidget.builder(Text.literal("Movement"), button -> this.client.setScreen(new Movement(showMenu, this))).width(200).build());
        adder.add(ButtonWidget.builder(Text.literal("Combat"), button -> this.client.setScreen(new Combat(showMenu, this))).width(200).build());
        adder.add(ButtonWidget.builder(Text.literal("Farm"), button -> this.client.setScreen(new Farm(showMenu, this))).width(200).build());
        adder.add(ButtonWidget.builder(Text.literal("World"), button -> this.client.setScreen(new World(showMenu, this))).width(200).build());
        adder.add(ButtonWidget.builder(Text.literal("Misc"), button -> this.client.setScreen(new Misc(showMenu, this))).width(200).build());

        gridWidget.recalculateDimensions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.25f);
        this.addDrawableChild(gridWidget);
    }
}
