package me.teawin.teapilot.protocol;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;

public abstract class Replayable {
    @Nullable String replyId;

    public void setReplyId(@Nullable String replyId) {
        this.replyId = replyId;
    }

    public void setReplyId(@Nullable JsonElement jsonElement) {
        if (jsonElement != null)
            setReplyId(jsonElement.getAsString());
    }
}
