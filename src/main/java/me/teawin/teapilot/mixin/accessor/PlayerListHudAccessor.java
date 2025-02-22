package me.teawin.teapilot.mixin.accessor;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerListHud.class)
public interface PlayerListHudAccessor {
    @Accessor("footer")
    @Nullable Text getFooter();

    @Accessor("header")
    @Nullable Text getHeader();
}
