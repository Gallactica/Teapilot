package me.teawin.teapilot.protocol.response.container;

import com.google.gson.JsonNull;
import com.google.gson.annotations.JsonAdapter;
import me.teawin.teapilot.protocol.Response;
import me.teawin.teapilot.protocol.type.SlotItem;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;


import java.util.Collections;
import java.util.List;

public class ContainerResponse extends Response {
    @Nullable
    private String type;
    @Nullable
    private Text title;
    private List<SlotItem> slots;

    public ContainerResponse(@Nullable String type, @Nullable Text title, List<SlotItem> slots) {
        this.type = type;
        this.title = title;
        this.slots = slots;
    }

    public ContainerResponse(String type, Text title) {
        this(type, title, Collections.emptyList());
    }
}