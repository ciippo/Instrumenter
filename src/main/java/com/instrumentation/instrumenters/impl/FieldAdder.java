package com.instrumentation.instrumenters.impl;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import com.instrumentation.data.FieldData;
import com.instrumentation.instrumenters.DefaultInstrumenter;

public class FieldAdder extends DefaultInstrumenter<FieldData> {

    @Override
    public ClassVisitor instrument(final ClassVisitor visitor) {
        return new ClassVisitor(Opcodes.ASM7,visitor) {
            @Override
            public void visitEnd() {
                for (final FieldData field : getDatas()) {
                    super.visitField(field.getAccess(), field.getName(), field.getDesc(), field.getSignature(),
                            field.getValue());
                }
                super.visitEnd();
            }
        };
    }
}
