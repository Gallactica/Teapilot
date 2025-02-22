package me.teawin.teapilot.proposal;

import me.teawin.teapilot.JsonUtils;
import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.TeapilotEvents;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.text.Text;

import java.util.UUID;

public class BossBarConsumer implements BossBarS2CPacket.Consumer {
    public void add(UUID uuid, Text name, float percent, BossBar.Color color, BossBar.Style style, boolean darkenSky, boolean dragonMusic, boolean thickenFog) {
        var event = TeapilotEvents.createEvent(TeapilotEvents.BOSSBAR_ADD);
        event.addProperty("uuid", uuid.toString());
        event.add("text", JsonUtils.fromText(name));
        event.addProperty("progress", percent);
        event.addProperty("color", color.toString());
        event.addProperty("style", style.toString());
        Teapilot.teapilotServer.broadcast(event);
    }

    public void remove(UUID uuid) {
        var event = TeapilotEvents.createEvent(TeapilotEvents.BOSSBAR_REMOVE);
        event.addProperty("uuid", uuid.toString());
        Teapilot.teapilotServer.broadcast(event);
    }

    public void updateProgress(UUID uuid, float percent) {
        var event = TeapilotEvents.createEvent(TeapilotEvents.BOSSBAR_UPDATE);
        event.addProperty("uuid", uuid.toString());
        event.addProperty("progress", percent);
        Teapilot.teapilotServer.broadcast(event);
    }

    public void updateName(UUID uuid, Text name) {
        var event = TeapilotEvents.createEvent(TeapilotEvents.BOSSBAR_UPDATE);
        event.addProperty("uuid", uuid.toString());
        event.add("text", JsonUtils.fromText(name));
        Teapilot.teapilotServer.broadcast(event);
    }

    public void updateStyle(UUID uuid, BossBar.Color color, BossBar.Style style) {
        var event = TeapilotEvents.createEvent(TeapilotEvents.BOSSBAR_UPDATE);
        event.addProperty("uuid", uuid.toString());
        event.addProperty("color", color.toString());
        event.addProperty("style", style.toString());
        Teapilot.teapilotServer.broadcast(event);
    }
}
