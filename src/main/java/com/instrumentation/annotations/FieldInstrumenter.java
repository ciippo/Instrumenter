package com.instrumentation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import com.instrumentation.data.FieldData;
import com.instrumentation.instrumenters.Instrumenter;

@Target(value = { ElementType.FIELD })
public @interface FieldInstrumenter {
    Class<? extends Instrumenter<FieldData>> instrumenter();
}
