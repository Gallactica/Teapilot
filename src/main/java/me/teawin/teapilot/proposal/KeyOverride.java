package me.teawin.teapilot.proposal;

import me.teawin.teapilot.Teapilot;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class KeyOverride {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledFuture = null;
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

        if (Teapilot.flags.isEnabled("DEBUG_LOGGER"))
            Teapilot.LOGGER.info("Key press {}", this.keyBinding.toString());

        KeyBinding.setKeyPressed(keyBinding, true);

        if (!previous_pressed || duration == 0) {
            KeyBinding.onKeyPressed(keyBinding);
        }

        if (duration <= 0) reset();
        else scheduledFuture = scheduler.schedule(this::reset, duration, TimeUnit.MILLISECONDS);
    }

    public void reset() {
        pressed = false;

        if (Teapilot.flags.isEnabled("DEBUG_LOGGER"))
            Teapilot.LOGGER.info("Key reset {}", this.keyBinding.toString());

        KeyBinding.setKeyPressed(keyBinding, false);
    }

    public boolean isPressed() {
        return pressed;
    }
}
