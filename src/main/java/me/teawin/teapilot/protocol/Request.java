package me.teawin.teapilot.protocol;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;

public abstract class Request extends Replayable implements Callable<@Nullable Response> {
}
