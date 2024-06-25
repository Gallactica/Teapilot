package me.teawin.soulkeeper;

import me.teawin.soulkeeper.protocol.Replayable;
import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import me.teawin.soulkeeper.protocol.request.ChatRequest;
import me.teawin.soulkeeper.protocol.request.ClientRequest;
import me.teawin.soulkeeper.protocol.request.SoulkeeperRequest;
import me.teawin.soulkeeper.protocol.request.TooltipRequest;
import me.teawin.soulkeeper.protocol.request.player.PlayerChatRequest;
import me.teawin.soulkeeper.protocol.request.player.PlayerInfoRequest;
import me.teawin.soulkeeper.protocol.request.player.PlayerRaycastRequest;
import me.teawin.soulkeeper.protocol.request.player.PlayerRotateRequest;
import me.teawin.soulkeeper.protocol.request.player.container.PlayerContainerInfoRequest;
import me.teawin.soulkeeper.protocol.request.player.container.PlayerContainerRequest;
import me.teawin.soulkeeper.protocol.request.player.interact.PlayerAttackRequest;
import me.teawin.soulkeeper.protocol.request.player.interact.PlayerUseRequest;
import me.teawin.soulkeeper.protocol.request.player.inventory.PlayerInventoryCloseRequest;
import me.teawin.soulkeeper.protocol.request.player.inventory.PlayerInventoryOpenRequest;
import me.teawin.soulkeeper.protocol.request.player.inventory.PlayerInventoryRequest;
import me.teawin.soulkeeper.protocol.request.player.inventory.PlayerInventorySlotRequest;
import me.teawin.soulkeeper.protocol.request.player.inventory.hand.PlayerInventoryHandDropRequest;
import me.teawin.soulkeeper.protocol.request.player.inventory.hand.PlayerInventoryHandRequest;
import me.teawin.soulkeeper.protocol.request.player.inventory.hand.PlayerInventoryHandSelectRequest;
import me.teawin.soulkeeper.protocol.request.player.look.PlayerLookRequest;
import me.teawin.soulkeeper.protocol.request.player.look.PlayerLookTargetRequest;
import me.teawin.soulkeeper.protocol.request.player.movement.PlayerJumpRequest;
import me.teawin.soulkeeper.protocol.request.scoreboard.SidebarRequest;
import me.teawin.soulkeeper.protocol.request.soulkeeper.FlagsRequest;
import me.teawin.soulkeeper.protocol.request.soulkeeper.FlagsSetRequest;
import me.teawin.soulkeeper.protocol.request.world.WorldBlockGetRequest;
import me.teawin.soulkeeper.protocol.request.world.WorldEntityGetRequest;

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
        register("client", ClientRequest.class);
        register("soulkeeper", SoulkeeperRequest.class);
        register("sidebar", SidebarRequest.class);
        register("chat", ChatRequest.class);
        register("tooltip", TooltipRequest.class);
        register("flags", FlagsRequest.class);
        register("flags.set", FlagsSetRequest.class);
        register("player.info", PlayerInfoRequest.class);
        register("player.raycast", PlayerRaycastRequest.class);
        register("player.interact.use", PlayerUseRequest.class);
        register("player.interact.attack", PlayerAttackRequest.class);
        register("player.movement.jump", PlayerJumpRequest.class);
        register("player.inventory", PlayerInventoryRequest.class);
        register("player.inventory.hand", PlayerInventoryHandRequest.class);
        register("player.inventory.slot", PlayerInventorySlotRequest.class);
        register("player.inventory.open", PlayerInventoryOpenRequest.class);
        register("player.inventory.close", PlayerInventoryCloseRequest.class);
        register("player.inventory.hand.select", PlayerInventoryHandSelectRequest.class);
        register("player.inventory.hand.drop", PlayerInventoryHandDropRequest.class);
        register("player.rotate", PlayerRotateRequest.class);
        register("player.look", PlayerLookRequest.class);
        register("player.look.target", PlayerLookTargetRequest.class);
        register("player.container", PlayerContainerRequest.class);
        register("player.container.info", PlayerContainerInfoRequest.class);
        register("world.entity.get", WorldEntityGetRequest.class);
        register("world.block.get", WorldBlockGetRequest.class);
        register("player.chat", PlayerChatRequest.class);
    }
}
