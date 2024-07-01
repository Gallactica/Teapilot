package me.teawin.teapilot.proposal;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class KeyOverride {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> scheduledFuture = null;
    private boolean pressed = false;

    public final InputUtil.Key keyBinding;

    public KeyOverride(InputUtil.Key keyBinding) {
        this.keyBinding = keyBinding;
    }

    public void press(int duration) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }

        if (duration < 0) {
            reset();
            return;
        }

        boolean previous_pressed = pressed;
        pressed = true;

        KeyBinding.setKeyPressed(keyBinding, true);

        if (!previous_pressed || duration == 0) {
            KeyBinding.onKeyPressed(keyBinding);
        }

        if (duration <= 0) reset();
        else scheduledFuture = scheduler.schedule(this::reset, duration, TimeUnit.MILLISECONDS);
    }

    public void reset() {
        pressed = false;
        KeyBinding.setKeyPressed(keyBinding, false);
    }

    public boolean isPressed() {
        return pressed;
    }
}
