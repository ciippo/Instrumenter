package com.instrumentation.instrumenters.impl;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.instrumentation.data.FieldData;
import com.instrumentation.instrumenters.DefaultInstrumenter;

public class FieldAsReference extends DefaultInstrumenter<FieldData> {

    private final Map<String, String> mapDescriptions;

    public FieldAsReference() {
        this.mapDescriptions = new HashMap<>();
    }

    @Override
    public ClassVisitor instrument(final ClassVisitor visitor) {
        return new ClassVisitor(Opcodes.ASM7,visitor) {

            @Override
            public FieldVisitor visitField(final int access, final String name, final String desc,
                    final String signature, final Object value) {
                for (final FieldData fieldData : getDatas()) {
                    if (fieldData.getName().equals(name)) {
                        getMapDescriptions().put(Type.getType(fieldData.getDesc()).getInternalName(), desc);
                    }
                }
                return super.visitField(access, name, desc, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(final int access, final String nameMethod, final String descMethod,
                    final String signature, final String[] exceptions) {
                final MethodVisitor mv = super.visitMethod(access, nameMethod, descMethod, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM7,mv) {
                    @Override
                    public void visitFieldInsn(final int opcode, final String owner, final String name,
                            final String desc) {
                        String realDesc = getMapDescriptions().get(Type.getType(desc).getInternalName());
                        if (realDesc == null) {
                            realDesc = desc;
                        }
                        super.visitFieldInsn(opcode, owner, name, realDesc);
                    }

                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
                    		boolean isInterface) {
                    	String realOwner = owner;
                        int realOpcode = opcode;
                        if (getMapDescriptions().get(owner) != null) {
                            realOwner = getMapDescriptions().get(owner);
                            realOpcode = Opcodes.INVOKEVIRTUAL;
                        }
                    	super.visitMethodInsn(realOpcode, realOwner, name, descriptor, isInterface);
                    }
                };
            }
        };
    }

    private Map<String, String> getMapDescriptions() {
        return this.mapDescriptions;
    }
}
