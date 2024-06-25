package me.teawin.soulkeeper.protocol.request.world;

import me.teawin.soulkeeper.protocol.Request;
import me.teawin.soulkeeper.protocol.Response;
import me.teawin.soulkeeper.protocol.response.EntityResponse;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WorldEntityGetRequest extends Request {
    int id;
    UUID uuid;

    @Override
    public @Nullable Response call() throws Exception {
        assert MinecraftClient.getInstance().world != null;

        Entity entity = null;

        if (id != 0) {
            entity = MinecraftClient.getInstance().world.getEntityById(id);
        } else if (uuid != null) {
            for (Entity worldEntity : MinecraftClient.getInstance().world.getEntities()) {
                if (worldEntity.getUuid().equals(uuid)) {
                    entity = worldEntity;
                    break;
                }
            }
        }

        return new EntityResponse(entity);
    }
}
