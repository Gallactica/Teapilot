package me.teawin.teapilot.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.teawin.teapilot.protocol.json.*;
import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;

public class GsonConfigurator {
    public static final Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(ItemStack.class, new ItemStackJson());
        gsonBuilder.registerTypeAdapter(Text.class, new TextJson());
        gsonBuilder.registerTypeAdapter(BlockPos.class, new BlockPosJson());
        gsonBuilder.registerTypeAdapter(BlockState.class, new BlockStateJson());
        gsonBuilder.registerTypeAdapter(ClientBossBar.class, new BossBarJson());
        gsonBuilder.registerTypeHierarchyAdapter(Entity.class, new EntityJson());
        gsonBuilder.registerTypeHierarchyAdapter(Position.class, new PositionJson());
        gsonBuilder.registerTypeHierarchyAdapter(Screen.class, new ScreenJson());
//        gsonBuilder.registerTypeHierarchyAdapter(SlotItem.class, new SlotItemJson());

        gson = gsonBuilder.create();
    }
}
