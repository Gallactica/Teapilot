package me.teawin.teapilot.protocol.response;

import me.teawin.teapilot.protocol.Response;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Position;

public class PlayerInfoResponse extends Response {
    private final int id;
    private final String uuid;
    private final int hotbarSlot;
    private final double health;
    private final int food;
    private final float saturation;
    private final int exp;
    private final int totalExp;
    private final Entity fishHook;
    private final Position pos;
    private final double eyeY;

    public PlayerInfoResponse(int id, String uuid, int hotbarSlot, double health, int food, float saturation, int exp, int totalExp, Entity fishHook, Position pos, double eyeY) {
        this.id = id;
        this.uuid = uuid;
        this.hotbarSlot = hotbarSlot;
        this.health = health;
        this.food = food;
        this.saturation = saturation;
        this.exp = exp;
        this.totalExp = totalExp;
        this.fishHook = fishHook;
        this.pos = pos;
        this.eyeY = eyeY;
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public int getHotbarSlot() {
        return hotbarSlot;
    }

    public double getHealth() {
        return health;
    }

    public int getFood() {
        return food;
    }

    public float getSaturation() {
        return saturation;
    }

    public int getExp() {
        return exp;
    }

    public int getTotalExp() {
        return totalExp;
    }

    public Entity getFishHook() {
        return fishHook;
    }
}
