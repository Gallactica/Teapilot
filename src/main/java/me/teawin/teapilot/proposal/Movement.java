package me.teawin.teapilot.proposal;

import me.teawin.teapilot.mixin.KeyBindingAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Movement {

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static Vec2f vec = new Vec2f(0, 0);
    public static boolean sneaking = false;
    public static boolean jumping = false;
    public static boolean sprinting = false;

    private static ScheduledFuture<?> scheduledFuture = null;

    public static void setMovement(float x, float y, boolean sneak, boolean jump, boolean sprint, int ms) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }

        if (ms == 0) {
            reset();
            return;
        }

        float dx = MathHelper.clamp(x, -1, 1);
        float dy = MathHelper.clamp(y, -1, 1);

        vec = new Vec2f(dx, dy);

        sneaking = sneak;
        jumping = jump;
        sprinting = sprint;

        KeyBinding.setKeyPressed(((KeyBindingAccessor) MinecraftClient.getInstance().options.sprintKey).getBoundKey(), sprint);

        scheduledFuture = scheduler.schedule(Movement::reset, ms, TimeUnit.MILLISECONDS);
    }

    public static void reset() {
        vec = Vec2f.ZERO;
        sneaking = false;
        jumping = false;
        sprinting = false;
    }
}
