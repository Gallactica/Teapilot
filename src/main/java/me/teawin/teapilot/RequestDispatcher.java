package me.teawin.teapilot;

import me.teawin.teapilot.protocol.Replayable;
import me.teawin.teapilot.protocol.Request;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.request.ChatRequest;
import me.teawin.teapilot.protocol.request.ClientRequest;
import me.teawin.teapilot.protocol.request.TeapilotRequest;
import me.teawin.teapilot.protocol.request.TooltipRequest;
import me.teawin.teapilot.protocol.request.player.PlayerChatRequest;
import me.teawin.teapilot.protocol.request.player.PlayerInfoRequest;
import me.teawin.teapilot.protocol.request.player.PlayerRaycastRequest;
import me.teawin.teapilot.protocol.request.player.PlayerRotateRequest;
import me.teawin.teapilot.protocol.request.container.ContainerInfoRequest;
import me.teawin.teapilot.protocol.request.container.ContainerRequest;
import me.teawin.teapilot.protocol.request.container.ContainerSlotClickRequest;
import me.teawin.teapilot.protocol.request.player.interact.PlayerAttackRequest;
import me.teawin.teapilot.protocol.request.player.interact.PlayerAttackPressRequest;
import me.teawin.teapilot.protocol.request.player.interact.PlayerUsePressRequest;
import me.teawin.teapilot.protocol.request.player.interact.PlayerUseRequest;
import me.teawin.teapilot.protocol.request.player.inventory.PlayerInventoryCloseRequest;
import me.teawin.teapilot.protocol.request.player.inventory.PlayerInventoryOpenRequest;
import me.teawin.teapilot.protocol.request.player.inventory.PlayerInventoryRequest;
import me.teawin.teapilot.protocol.request.player.inventory.PlayerInventorySlotRequest;
import me.teawin.teapilot.protocol.request.player.inventory.hand.PlayerInventoryHandDropRequest;
import me.teawin.teapilot.protocol.request.player.inventory.hand.PlayerInventoryHandRequest;
import me.teawin.teapilot.protocol.request.player.inventory.hand.PlayerInventoryHandSelectRequest;
import me.teawin.teapilot.protocol.request.player.look.PlayerLookRequest;
import me.teawin.teapilot.protocol.request.player.look.PlayerLookTargetRequest;
import me.teawin.teapilot.protocol.request.player.movement.PlayerMovementRequest;
import me.teawin.teapilot.protocol.request.scoreboard.SidebarRequest;
import me.teawin.teapilot.protocol.request.sign.SignEditRequest;
import me.teawin.teapilot.protocol.request.sign.SignFinishRequest;
import me.teawin.teapilot.protocol.request.teapilot.FlagsRequest;
import me.teawin.teapilot.protocol.request.teapilot.FlagsSetRequest;
import me.teawin.teapilot.protocol.request.visual.ItemToastRequest;
import me.teawin.teapilot.protocol.request.world.WorldRaycastRequest;
import me.teawin.teapilot.protocol.request.world.block.WorldBlockAreaRequest;
import me.teawin.teapilot.protocol.request.world.block.WorldBlockGetRequest;
import me.teawin.teapilot.protocol.request.world.entity.WorldEntityAreaRequest;
import me.teawin.teapilot.protocol.request.world.entity.WorldEntityGetRequest;
import me.teawin.teapilot.protocol.request.world.block.WorldBlockRaycastRequest;
import me.teawin.teapilot.protocol.request.world.entity.WorldEntityRaycastRequest;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RequestDispatcher {
    public HashMap<String, Class<? extends Replayable>> listeners = new HashMap<>();

    public Set<String> getMethods() {
        return listeners.keySet();
    }

    public void register(String name, Class<? extends Request> requestClass) {
        listeners.put(name, requestClass);
    }

    public CompletableFuture<Response> dispatch(Request runnable) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return runnable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public RequestDispatcher() {
        register("teapilot", TeapilotRequest.class);
        register("flags", FlagsRequest.class);
        register("flags.set", FlagsSetRequest.class);

        register("sidebar", SidebarRequest.class);
        register("client", ClientRequest.class);

        register("chat", ChatRequest.class);
        register("tooltip", TooltipRequest.class);

        register("player.info", PlayerInfoRequest.class);
        register("player.raycast", PlayerRaycastRequest.class);
        register("player.chat", PlayerChatRequest.class);

        register("player.interact.use", PlayerUsePressRequest.class);
        register("player.interact.use.manual", PlayerUseRequest.class);
        register("player.interact.attack", PlayerAttackPressRequest.class);
        register("player.interact.attack.manual", PlayerAttackRequest.class);
        register("player.movement", PlayerMovementRequest.class);
        register("player.rotate", PlayerRotateRequest.class);
        register("player.look", PlayerLookRequest.class);
        register("player.look.target", PlayerLookTargetRequest.class);

        register("player.inventory", PlayerInventoryRequest.class);
        register("player.inventory.hand", PlayerInventoryHandRequest.class);
        register("player.inventory.slot", PlayerInventorySlotRequest.class);
        register("player.inventory.open", PlayerInventoryOpenRequest.class);
        register("player.inventory.close", PlayerInventoryCloseRequest.class);
        register("player.inventory.hand.select", PlayerInventoryHandSelectRequest.class);
        register("player.inventory.hand.drop", PlayerInventoryHandDropRequest.class);

        register("container", ContainerRequest.class);
        register("container.info", ContainerInfoRequest.class);
        register("container.slot.click", ContainerSlotClickRequest.class);

        register("world.entity.get", WorldEntityGetRequest.class);
        register("world.entity.area", WorldEntityAreaRequest.class);
        register("world.entity.raycast", WorldEntityRaycastRequest.class);

        register("world.block.get", WorldBlockGetRequest.class);
        register("world.block.area", WorldBlockAreaRequest.class);
        register("world.block.raycast", WorldBlockRaycastRequest.class);

        register("world.raycast", WorldRaycastRequest.class);

        register("visual.toast", ItemToastRequest.class);

        register("sign.edit", SignEditRequest.class);
        register("sign.finish", SignFinishRequest.class);
    }
}
