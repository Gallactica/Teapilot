package me.teawin.teapilot.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import me.teawin.teapilot.JsonUtils;
import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.TeapilotEvents;
import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "clickSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void clickSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci, ScreenHandler screenHandler, DefaultedList defaultedList, int i, List list, Int2ObjectMap<ItemStack> int2ObjectMap) {
        if (Teapilot.flagsManager.isDisabled("PACKET_CONTAINER")) return;

        ItemStack cursor = screenHandler.getCursorStack().copy();

        JsonObject jsonObject = TeapilotEvents.createEvent(TeapilotEvents.CONTAINER_UPDATE_TRANSACTION);
        JsonArray jsonArray = new JsonArray();

        int2ObjectMap.forEach((o, o2) -> {
            jsonArray.add(JsonUtils.fromSlotItem(new SlotItem(o, o2)));
        });

        jsonObject.add("syncId", new JsonPrimitive(syncId));
        jsonObject.add("slot", new JsonPrimitive(slotId));
        jsonObject.add("button", new JsonPrimitive(button));
        jsonObject.add("action", new JsonPrimitive(actionType.name()));
        jsonObject.add("client", new JsonPrimitive(true));
        jsonObject.add("cursor", JsonUtils.fromItemStack(cursor));
        jsonObject.add("slots", jsonArray);

        Teapilot.teapilotServer.broadcast(jsonObject);
    }
}
