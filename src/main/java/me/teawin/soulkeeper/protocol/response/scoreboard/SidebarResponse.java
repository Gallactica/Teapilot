package me.teawin.soulkeeper.protocol.response.scoreboard;

import me.teawin.soulkeeper.protocol.Response;
import net.minecraft.text.Text;

import java.util.List;

public class SidebarResponse extends Response {
    private final Text title;
    private final List<Text> texts;

    public SidebarResponse(Text title, List<Text> texts) {
        this.title = title;
        this.texts = texts;
    }
}
