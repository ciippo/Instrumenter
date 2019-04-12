package com.instrumentation.instrumenters.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.instrumentation.ReferencesToClassForcer;
import com.instrumentation.data.MethodData;
import com.instrumentation.instrumenters.DefaultInstrumenter;

public class MethodAdder extends DefaultInstrumenter<MethodData> {

    @Override
    public ClassVisitor instrument(final ClassVisitor visitor) {
        return new ClassVisitor(Opcodes.ASM7,visitor) {

            @Override
            public void visit(final int version, final int access, final String name, final String signature,
                    final String superName, final String[] interfaces) {
                final List<String> totalInterfaces = new ArrayList<>(Arrays.asList(interfaces));
                totalInterfaces.addAll(getInstrumentationData().getInterfaces());
                super.visit(version, access, name, signature, superName,
                        totalInterfaces.toArray(new String[totalInterfaces.size()]));
            }

            @Override
            public void visitEnd() {
                final List<MethodData> methods = getDatas();
                for (final MethodData method : methods) {
                    final List<Consumer<MethodVisitor>> functions = getInstrumentationData().getMethodFunctions(method);
                    final MethodVisitor mv = super.visitMethod(method.getAccess(), method.getName(), method.getDesc(),
                            method.getSignature(), method.getExceptions());
                    final ReferencesToClassForcer referencesToClassForcer = new ReferencesToClassForcer(mv,
                            getInstrumentationData());
                    functions.forEach(f -> f.accept(referencesToClassForcer));
                }
                super.visitEnd();
            }
        };
    }
}