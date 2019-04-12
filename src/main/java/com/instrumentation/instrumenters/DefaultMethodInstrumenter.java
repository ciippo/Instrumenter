package com.instrumentation.instrumenters;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.instrumentation.data.MethodData;

public abstract class DefaultMethodInstrumenter extends DefaultInstrumenter<MethodData> {

    private MethodData actualMethodData;

    @Override
    public ClassVisitor instrument(final ClassVisitor visitor) {
        return new ClassVisitor(Opcodes.ASM7,visitor) {
            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc,
                    final String signature, final String[] exceptions) {
                final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                final MethodData methodData = new MethodData(access, name, desc, signature, exceptions);
                if (enterMethod(methodData)) {
                    setActualMethodData(methodData);
                    return instrument(mv);
                }
                return mv;
            }
        };
    }

    private void setActualMethodData(final MethodData actualMethodData) {
        this.actualMethodData = actualMethodData;
    }

    protected MethodData getActualMethodData() {
        return this.actualMethodData;
    }

    protected boolean enterMethod(final MethodData methodData) {
        return getDatas().contains(methodData);
    }

    protected abstract MethodVisitor instrument(MethodVisitor mv);
}
