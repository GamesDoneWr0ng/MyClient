package net.myclient;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.myclient.event.EventManager;
import net.myclient.gui.MyMenu;
import net.myclient.hack.HackManager;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Main {
	INSTANCE;
	private Logger LOGGER;
	private KeyBinding menuKey;
	private EventManager eventManager;
	private HackManager hackManager;

	public void initialize() {
		LOGGER = LoggerFactory.getLogger("myclient");

		eventManager = new EventManager(this);

		hackManager = new HackManager();

		// right shift hacks menu
		menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.examplemod.menu", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_RIGHT_SHIFT, // The keycode of the key
				"category.MyClient.test" // The translation key of the keybinding's category.
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (menuKey.wasPressed()) {
				MinecraftClient.getInstance().setScreen(new MyMenu(true));
			}
		});

		LOGGER.info("Hello Fabric world!");
	}

	public EventManager getEventManager() {
		return eventManager;
	}
	public HackManager getHackManager() {return hackManager;}
}
