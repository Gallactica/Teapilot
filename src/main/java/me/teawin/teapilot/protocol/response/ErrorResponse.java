package me.teawin.teapilot.protocol.response;

import me.teawin.teapilot.protocol.Response;

public class ErrorResponse extends Response {
    private String error;
    private String stack;

    public ErrorResponse(String error, String stack) {
        this.error = error;
        this.stack = stack;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }
}
