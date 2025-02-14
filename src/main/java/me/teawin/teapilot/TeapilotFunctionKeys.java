package me.teawin.teapilot;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class TeapilotFunctionKeys {
    private static final int FUNCTION_KEY_COUNT = 9;
    public static final KeyBinding[] functionKeyBindings = new KeyBinding[FUNCTION_KEY_COUNT];

    public static void register() {
        final String keyPrefix = "key.teapilot.function_";
        final String category = "category.teapilot.general";
        int[] defaultKeys = new int[]{GLFW.GLFW_KEY_R, GLFW.GLFW_KEY_UNKNOWN, GLFW.GLFW_KEY_UNKNOWN, GLFW.GLFW_KEY_UNKNOWN, GLFW.GLFW_KEY_UNKNOWN, GLFW.GLFW_KEY_UNKNOWN, GLFW.GLFW_KEY_UNKNOWN, GLFW.GLFW_KEY_UNKNOWN, GLFW.GLFW_KEY_UNKNOWN}; // Регистрируем биндинги в цикле

        for (int i = 0; i < FUNCTION_KEY_COUNT; i++) {
            String keyName = keyPrefix + (i + 1);
            functionKeyBindings[i] = KeyBindingHelper.registerKeyBinding(
                    new KeyBinding(keyName, InputUtil.Type.KEYSYM, defaultKeys[i], category));
        }
    }

    static void sendKeyPress(int i) {
        var key = TeapilotEvents.createEvent(TeapilotEvents.FUNCTION_KEY);
        key.addProperty("key", i);
        Teapilot.teapilotServer.broadcast(key);
    }
}
