package me.teawin.teapilot.proposal;

import com.google.gson.JsonObject;
import me.teawin.teapilot.TeapilotEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.MathHelper;

import static me.teawin.teapilot.Teapilot.teapilotServer;

public class ControlLook {
    public static float pitch;
    public static float yaw;
    public static float spread_pitch = 0;
    public static float spread_yaw = 0;

    public static int ticks = 0;
    private static boolean notify = false;

    public static float lerp(float start, float end, float factor) {
        return start + factor * (end - start);
    }

    public static float lerpAngle(float current, float target, float factor) {
        float diff = target - current;
        while (diff < -180F) diff += 360F;
        while (diff >= 180F) diff -= 360F;
        return current + diff * factor;
    }

    public static void abort() {
        if (ticks > 0) {
            sendAbort("Force");
        }
        ticks = 0;
        spread_yaw = 0;
        spread_pitch = 0;
        notify = false;
    }

    public static void sendAbort(String reason) {
        JsonObject event = TeapilotEvents.createEvent(TeapilotEvents.LOOK_ABORTED);
        event.addProperty("reason", reason);
        teapilotServer.broadcast(event);
    }

    public static void tick() {
        if (ControlLook.ticks <= 0) {
            if (notify) {
                notify = false;
                JsonObject event = TeapilotEvents.createEvent(TeapilotEvents.LOOK_REACHED);
                teapilotServer.broadcast(event);
            }
            return;
        }
        ControlLook.ticks--;
    }

    public static void look(float yaw, float pitch) {
        if (ticks > 0) {
            sendAbort("Override");
        }
        ControlLook.yaw = yaw;
        ControlLook.pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
    }

    public static void look(float yaw, float pitch, int duration) {
        assert duration > 0;
        notify = true;
        look(yaw, pitch);
        ControlLook.ticks = duration;
    }

    public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        if (ticks <= 0) return;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;

        float currentYaw = player.getYaw();
        float currentPitch = player.getPitch();
        float desiredYaw = ControlLook.yaw;
        float desiredPitch = ControlLook.pitch;

        float delta = 1.0F / ControlLook.ticks;

        float newYaw = lerpAngle(currentYaw, desiredYaw, delta);
        float newPitch = MathHelper.clamp(lerp(currentPitch, desiredPitch, delta), -90.0F, 90.0F);

        player.setPitch(newPitch + spread_pitch);
        player.setYaw(newYaw + spread_yaw);
        player.setHeadYaw(player.getYaw());
        player.setPitch(player.getPitch());
    }
}
