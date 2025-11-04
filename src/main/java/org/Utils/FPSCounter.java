package org.Utils;

import utils.Time;

public final class FPSCounter {

    private static int fpsCount = 0;
    private static double previousCheckedTick = 0.0;

    public static void init() {
        previousCheckedTick = Time.getRealTime();
    }

    public static void update() {
        fpsCount++;
        if (Time.getRealTime() - previousCheckedTick >= 1) {
            System.out.println("FPS: " + fpsCount);
            fpsCount = 0;
            previousCheckedTick = Time.getRealTime();
        }
    }

}
