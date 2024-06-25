package me.teawin.soulkeeper;

import java.util.HashMap;

public class FlagsManager {
    public final HashMap<String, Boolean> flags = new HashMap<>();

    public void set(String name, Boolean state) {
        flags.put(name, state);
    }

    public Boolean get(String name) {
        return get(name, false);
    }

    public Boolean get(String name, Boolean defaultValue) {
        return flags.getOrDefault(name, defaultValue);
    }

    public void enable(String name) {
        this.set(name, true);
    }

    public void disable(String name) {
        this.set(name, false);
    }

    public boolean isEnabled(String name) {
        return this.get(name, false);
    }

    public boolean isDisabled(String name) {
        return !this.get(name, false);
    }

    public boolean toggle(String name) {
        boolean newValue = !this.get(name, false);
        this.set(name, newValue);
        return newValue;
    }
}
