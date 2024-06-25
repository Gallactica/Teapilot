package me.teawin.teapilot.protocol.response;

import me.teawin.teapilot.protocol.Response;

public class ClientResponse extends Response {
    private int width;
    private int height;
    private int x;
    private int y;
    private double scale;
    private double fov;
    private boolean isFullscreen;

    public ClientResponse() {
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }

    public void setFullscreen(boolean fullscreen) {
        isFullscreen = fullscreen;
    }
}
