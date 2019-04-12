package com.instrumentation.instrumenters.impl;

import java.util.List;
import java.util.function.Consumer;

import org.objectweb.asm.MethodVisitor;

import com.instrumentation.ReferencesToClassForcer;
import com.instrumentation.instrumenters.DefaultMethodInstrumenter;

public class MethodReplacer extends DefaultMethodInstrumenter {

    @Override
    protected MethodVisitor instrument(final MethodVisitor mv) {
        final List<Consumer<MethodVisitor>> functions = getInstrumentationData()
                .getMethodFunctions(getActualMethodData());
        final ReferencesToClassForcer referencesToClassForcer = new ReferencesToClassForcer(mv,
                getInstrumentationData());
        functions.forEach(f -> f.accept(referencesToClassForcer));
        return null;
    }
}
