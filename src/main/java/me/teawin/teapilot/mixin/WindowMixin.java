package me.teawin.teapilot.mixin;

import com.google.gson.JsonObject;
import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.TeapilotEvents;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {
    @Inject(method = "onWindowFocusChanged", at = @At(value = "HEAD"))
    public void onWindowFocusChanged(long window, boolean focused, CallbackInfo ci) {
        JsonObject event = TeapilotEvents.createEvent(focused ? TeapilotEvents.WINDOW_FOCUS : TeapilotEvents.WINDOW_BLUR);
        Teapilot.teapilotServer.broadcast(event);
    }
}
