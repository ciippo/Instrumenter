package com.instrumentation;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.instrumentation.data.InstrumentationData;

public class ReferencesToClassForcer extends MethodVisitor {

    private final InstrumentationData instrumentationData;

    public ReferencesToClassForcer(final MethodVisitor mv, final InstrumentationData instrumentationData) {
        super(Opcodes.ASM7,mv);
        this.instrumentationData = instrumentationData;
    }
    
    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        super.visitFieldInsn(opcode, getOwner(owner), name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
    	super.visitMethodInsn(opcode, getOwner(owner), name, descriptor, isInterface);
    }

    private String getOwner(final String owner) {
        String forcedOwner = owner;
        final String typeInstrumenter = instrumentationData.getClassNameInstrumenter();
        if (owner.equals(typeInstrumenter)) {
            forcedOwner = this.instrumentationData.getClassNameToInstrumenter();
        }
        return forcedOwner;
    }
}