package me.teawin.teapilot.proposal;

import me.teawin.teapilot.mixin.accessor.InputUtilTypeAccessor;
import me.teawin.teapilot.mixin.accessor.KeyBindingAccessor;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ControlOverride {
    public static ConcurrentHashMap<InputUtil.Key, KeyOverride> keys = new ConcurrentHashMap<>();

    public static boolean isPressed(KeyBinding keyBinding) {
        return isPressed(((KeyBindingAccessor) keyBinding).getBoundKey());
    }

    public static boolean isPressed(InputUtil.Key key) {
        KeyOverride keyOverride = keys.getOrDefault(key, null);
        if (keyOverride == null) return false;
        return keyOverride.isPressed();
    }

    public static List<String> getKeys() {
        List<String> result = new ArrayList<>();
        keys.keys()
                .asIterator()
                .forEachRemaining(key -> result.add(key.getTranslationKey()));
        return result;
    }

    public static void reset(KeyBinding keyBinding) {
        reset(((KeyBindingAccessor) keyBinding).getBoundKey());
    }

    public static void reset(InputUtil.Key key) {
        KeyOverride keyOverride = keys.getOrDefault(key, null);
        if (keyOverride == null) return;
        keyOverride.press(-1);
    }

    public static void press(KeyBinding keyBinding, int duration) {
        press(((KeyBindingAccessor) keyBinding).getBoundKey(), duration);
    }

    public static void press(InputUtil.Key key, int duration) {
        KeyOverride keyOverride = keys.getOrDefault(key, null);
        if (keyOverride == null) return;
        keyOverride.press(duration);
    }

    static {
        for (InputUtil.Type value : InputUtil.Type.values()) {
            var map = ((InputUtilTypeAccessor) (Object) value).getMap();
            for (InputUtil.Key key : map.values()) {
                keys.put(key, new KeyOverride(key));
            }
        }
    }
}
