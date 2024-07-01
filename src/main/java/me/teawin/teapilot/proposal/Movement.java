package me.teawin.teapilot.proposal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Movement {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static Vec2f vec = new Vec2f(0, 0);

    private static ScheduledFuture<?> scheduledFuture = null;

    public static void setMovement(float x, float y, boolean sneak, boolean jump, boolean sprint, int duration) {
        setMovement(x, y, duration);

        if (duration <= 0) {
            resetKeys();
            return;
        }

        if (jump)
            ControlOverride.press(MinecraftClient.getInstance().options.jumpKey, duration);
        if (sneak)
            ControlOverride.press(MinecraftClient.getInstance().options.sneakKey, duration);
        if (sprint)
            ControlOverride.press(MinecraftClient.getInstance().options.sprintKey, duration);
    }

    public static void setMovement(float x, float y, int duration) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }

        if (duration <= 0) {
            reset();
            return;
        }

        float dx = MathHelper.clamp(x, -1, 1);
        float dy = MathHelper.clamp(y, -1, 1);

        vec = new Vec2f(dx, dy);

        scheduledFuture = scheduler.schedule(Movement::reset, duration, TimeUnit.MILLISECONDS);
    }

    public static void reset() {
        vec = Vec2f.ZERO;
    }

    public static void resetKeys() {
        ControlOverride.press(MinecraftClient.getInstance().options.jumpKey, -1);
        ControlOverride.press(MinecraftClient.getInstance().options.sneakKey, -1);
        ControlOverride.press(MinecraftClient.getInstance().options.sprintKey, -1);
    }
}
