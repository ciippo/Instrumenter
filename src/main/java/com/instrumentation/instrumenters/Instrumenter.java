package com.instrumentation.instrumenters;

import org.objectweb.asm.ClassVisitor;

import com.instrumentation.data.InstrumentationData;

public interface Instrumenter<T> {

    ClassVisitor instrument(ClassVisitor visitor);

    void addData(T data);

    void setInstrumentationData(InstrumentationData instrumentationData);
}
