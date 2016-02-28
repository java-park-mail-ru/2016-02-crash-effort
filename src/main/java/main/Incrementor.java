package main;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

/**
 * Created by vladislav on 28.02.16.
 */
@Singleton
public class Incrementor {
    private static long lastId = 0;

    @NotNull
    public static Long getNext() {
        return lastId++;
    }
}
