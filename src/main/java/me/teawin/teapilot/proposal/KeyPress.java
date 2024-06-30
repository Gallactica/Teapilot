package me.teawin.teapilot.proposal;

import me.teawin.teapilot.mixin.accessor.KeyBindingAccessor;
import net.minecraft.client.option.KeyBinding;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class KeyPress {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> scheduledFuture = null;
    private boolean pressed = false;

    public final KeyBinding keyBinding;

    public KeyPress(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    public void press(int duration) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }

        if (duration == 0) {
            reset();
            return;
        }

        boolean previous_pressed = pressed;

        pressed = true;
        KeyBinding.setKeyPressed(((KeyBindingAccessor) keyBinding).getBoundKey(), pressed);
        if (!previous_pressed)
            KeyBinding.onKeyPressed(((KeyBindingAccessor) keyBinding).getBoundKey());

        scheduledFuture = scheduler.schedule(this::reset, duration, TimeUnit.MILLISECONDS);
    }

    public void reset() {
        pressed = false;
        KeyBinding.setKeyPressed(((KeyBindingAccessor) keyBinding).getBoundKey(), pressed);
    }

    public boolean isPressed() {
        return pressed;
    }
}
