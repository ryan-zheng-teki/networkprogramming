package com.qsol.reactive;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class VarHandleTutorial {
    private volatile int x;
    private static VarHandle X;

    static  {
        try {
            X = MethodHandles.lookup().findVarHandle(VarHandleTutorial.class, "X", int.class);
        } catch (ReflectiveOperationException e) {
            System.out.println("VarHandle weird");
        }
    }
}
