package com.instrumentation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import com.instrumentation.data.MethodData;
import com.instrumentation.instrumenters.Instrumenter;

@Target(value = { ElementType.METHOD })
public @interface MethodInstrumenter {
    Class<? extends Instrumenter<MethodData>> instrumenter();
}
