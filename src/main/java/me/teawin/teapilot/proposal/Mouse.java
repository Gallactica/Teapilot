package me.teawin.teapilot.proposal;

import net.minecraft.client.MinecraftClient;

public class Mouse {
    public static KeyPress attackKey = new KeyPress(MinecraftClient.getInstance().options.attackKey);
    public static KeyPress useKey = new KeyPress(MinecraftClient.getInstance().options.useKey);
}
