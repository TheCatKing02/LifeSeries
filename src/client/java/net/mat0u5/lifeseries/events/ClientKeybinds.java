package net.mat0u5.lifeseries.events;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.mat0u5.lifeseries.network.NetworkHandlerClient;
import net.mat0u5.lifeseries.utils.versions.VersionControl;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class ClientKeybinds {
    public static KeyBinding superpower;
    public static KeyBinding openConfig;

    public static KeyBinding runCommand;

    public static void tick() {
        while (superpower != null && superpower.wasPressed()) {
            NetworkHandlerClient.pressSuperpowerKey();
        }
        while (runCommand != null && runCommand.wasPressed() && VersionControl.isDevVersion()) {
            NetworkHandlerClient.pressRunCommandKey();
        }
        while (openConfig != null && openConfig.wasPressed()) {
            NetworkHandlerClient.pressOpenConfigKey();
        }
    }
    public static void registerKeybinds() {
        superpower = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lifeseries.superpower",
                InputUtil.Type.KEYSYM,
                //? if <= 1.21.5 {
                GLFW.GLFW_KEY_G,
                //?} else {
                /*GLFW.GLFW_KEY_R,
                 *///?}

                //? if <= 1.21.6 {
                "key.categories.lifeseries"
                //?} else {
                /*new KeyBinding.Category(Identifier.of("lifeseries","key.categories.lifeseries"))
                *///?}
        ));
        openConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lifeseries.openconfig",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,

                //? if <= 1.21.6 {
                "key.categories.lifeseries"
                 //?} else {
                /*new KeyBinding.Category(Identifier.of("lifeseries","key.categories.lifeseries"))
                *///?}
        ));
        if (VersionControl.isDevVersion()) {
            runCommand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.lifeseries.runcommand",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_RIGHT_ALT,

                    //? if <= 1.21.6 {
                    "key.categories.lifeseries"
                     //?} else {
                    /*new KeyBinding.Category(Identifier.of("lifeseries","key.categories.lifeseries"))
                    *///?}
            ));
        }
    }
}
