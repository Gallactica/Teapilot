package me.teawin.teapilot.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin {
    @Unique
    protected boolean shouldPause() {
        return false;
    }
}
