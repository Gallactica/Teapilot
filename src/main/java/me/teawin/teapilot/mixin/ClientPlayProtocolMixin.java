package me.teawin.teapilot.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.ParseResults;
import me.teawin.teapilot.Teapilot;
import me.teawin.teapilot.JsonUtils;
import me.teawin.teapilot.TeapilotEvents;
import me.teawin.teapilot.proposal.BossBarConsumer;
import me.teawin.teapilot.proposal.ControlLook;
import me.teawin.teapilot.proposal.ParticleManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.particle.ParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayProtocolMixin {

    @Shadow
    protected abstract ParseResults<CommandSource> parse(String command);

    @Inject(method = "onPlayerPositionLook", at = @At("HEAD"))
    public void onPlayerPositionLook(PlayerPositionLookS2CPacket packet, CallbackInfo ci) {
        ControlLook.abort();
    }

    @Inject(method = "onEntityTrackerUpdate(Lnet/minecraft/network/packet/s2c/play/EntityTrackerUpdateS2CPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;writeUpdatedEntries(Ljava/util/List;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet, CallbackInfo ci, Entity entity) {
        Teapilot.broadcastEntityUpdate(entity);
    }

    @Inject(method = "onEntityAttributes(Lnet/minecraft/network/packet/s2c/play/EntityAttributesS2CPacket;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onEntityAttributes(EntityAttributesS2CPacket packet, CallbackInfo ci, Entity entity) {
//        System.out.println("onEntityAttributes " + entity.toString() + packet.toString());
        Teapilot.broadcastEntityUpdate(entity);
    }

    @Inject(method = "onEntityEquipmentUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/EntityEquipmentUpdateS2CPacket;getEquipmentList()Ljava/util/List;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onEntityEquipmentUpdate(EntityEquipmentUpdateS2CPacket packet, CallbackInfo ci, Entity entity) {
//        System.out.println("onEntityEquipmentUpdate " + entity.toString() + packet.toString());
        Teapilot.broadcastEntityUpdate(entity);
    }

    @Inject(method = "onEntityPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;setPosition(Lnet/minecraft/entity/player/PlayerPosition;Ljava/util/Set;Lnet/minecraft/entity/Entity;Z)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onEntityPosition(EntityPositionS2CPacket packet, CallbackInfo ci, Entity entity) {
//        System.out.println("onEntityPosition " + entity.toString() + packet.toString());
        Teapilot.broadcastEntityUpdate(entity);
    }

    @Inject(method = "onEntityVelocityUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocityClient(DDD)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onEntityVelocityUpdate(EntityVelocityUpdateS2CPacket packet, CallbackInfo ci, Entity entity) {
//        System.out.println("onEntityVelocityUpdate " + entity.toString() + packet.toString());
        Teapilot.broadcastEntityUpdate(entity);
    }

    @Inject(method = "onScreenHandlerSlotUpdate", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo ci, PlayerEntity playerEntity, ItemStack itemStack, int i) {
        if (Teapilot.flags.isDisabled("PACKET_CONTAINER")) return;

        var event = TeapilotEvents.createEvent(TeapilotEvents.CONTAINER_UPDATE_SLOT);

        event.add("slot", new JsonPrimitive(i));
        event.add("item", JsonUtils.fromItemStack(itemStack));

        Teapilot.teapilotServer.broadcast(event);
    }

    @Inject(method = "onInventory", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onInventory(InventoryS2CPacket packet, CallbackInfo ci, PlayerEntity playerEntity) {
        if (Teapilot.flags.isDisabled("PACKET_CONTAINER")) return;

        var event = TeapilotEvents.createEvent(TeapilotEvents.CONTAINER_UPDATE);

        event.add("cursor", JsonUtils.fromItemStack(packet.getCursorStack()));
        JsonArray items = new JsonArray();
        for (int i = 0; i < packet.getContents()
                .size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("slot", new JsonPrimitive(i));
            jsonObject.add("item", JsonUtils.fromItemStack(packet.getContents()
                    .get(i)));
            items.add(jsonObject);
        }
//        for (ItemStack content : packet.getContents()) {
//            items.add(JsonUtils.fromItemStack(content));
//        }
        event.add("items", items);

        Teapilot.teapilotServer.broadcast(event);
    }

    @Inject(method = "onScoreboardObjectiveUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/ScoreboardObjective;setDisplayName(Lnet/minecraft/text/Text;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onScoreboardObjectiveUpdate(ScoreboardObjectiveUpdateS2CPacket packet, CallbackInfo ci) {
    }

    @Inject(method = "onScoreboardScoreUpdate", at = @At("TAIL"))
    public void onScoreboardScoreUpdate(ScoreboardScoreUpdateS2CPacket packet, CallbackInfo ci) {
    }

    @Inject(method = "onCloseScreen", at = @At("TAIL"))
    public void onCloseScreen(CloseScreenS2CPacket packet, CallbackInfo ci) {
        if (Teapilot.flags.isDisabled("PACKET_CONTAINER")) return;
        var event = TeapilotEvents.createEvent(TeapilotEvents.CONTAINER_CLOSE);
        event.addProperty("side", "server");
        Teapilot.teapilotServer.broadcast(event);
    }

    @Inject(method = "onBlockUpdate", at = @At("TAIL"))
    public void onBlockUpdate(BlockUpdateS2CPacket packet, CallbackInfo ci) {
        if (Teapilot.flags.isDisabled("PACKET_BLOCK_UPDATE")) return;

        var event = TeapilotEvents.createEvent(TeapilotEvents.BLOCK_UPDATE);
        JsonArray blocks = new JsonArray();

        JsonObject block = new JsonObject();
        block.add("pos", JsonUtils.fromPosition(packet.getPos()));
        block.add("block", JsonUtils.fromBlockState(packet.getState()));
        blocks.add(block);

        event.add("blocks", blocks);
        Teapilot.teapilotServer.broadcast(event);
    }

    @Inject(method = "onParticle", at = @At("HEAD"))
    public void onParticle(ParticleS2CPacket packet, CallbackInfo ci) {
        if (Teapilot.flags.isDisabled("PACKET_PARTICLE")) return;

        ParticleType<?> particleType = packet.getParameters()
                .getType();
        String type = ParticleManager.typeOf(particleType);

        JsonObject event = TeapilotEvents.createEvent(TeapilotEvents.PARTICLE);

        if (type == null) type = "UNKNOWN";

        event.addProperty("type", type);
        event.addProperty("count", packet.getCount());
        event.addProperty("speed", packet.getSpeed());
        event.add("position", JsonUtils.fromPosition(packet.getX(), packet.getY(), packet.getZ()));
        event.add("offset", JsonUtils.fromPosition(packet.getOffsetX(), packet.getOffsetY(), packet.getOffsetZ()));

        Teapilot.teapilotServer.broadcast(event);
    }

    @Inject(method = "onChunkDeltaUpdate", at = @At("TAIL"))
    public void onChunkDeltaUpdate(ChunkDeltaUpdateS2CPacket packet, CallbackInfo ci) {
        if (Teapilot.flags.isDisabled("PACKET_BLOCK_UPDATE")) return;

        var event = TeapilotEvents.createEvent(TeapilotEvents.BLOCK_UPDATE);
        JsonArray blocks = new JsonArray();

        packet.visitUpdates((blockPos, state) -> {
            JsonObject block = new JsonObject();
            block.add("pos", JsonUtils.fromPosition(blockPos));
            block.add("block", JsonUtils.fromBlockState(state));
            blocks.add(block);
        });

        event.add("blocks", blocks);
        Teapilot.teapilotServer.broadcast(event);
    }

    @Inject(at = @At("TAIL"), method = "onBossBar")
    public void onBossBar(BossBarS2CPacket packet, CallbackInfo info) {
        if (Teapilot.flags.isEnabled("PACKET_BOSSBAR")) packet.accept(new BossBarConsumer());
    }
}
