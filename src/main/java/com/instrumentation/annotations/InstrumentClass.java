package com.instrumentation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value = { ElementType.TYPE })
public @interface InstrumentClass {
    String name();
}
