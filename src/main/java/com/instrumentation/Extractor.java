package com.instrumentation;

import java.util.Arrays;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.instrumentation.annotations.InstrumentClass;
import com.instrumentation.data.FieldData;
import com.instrumentation.data.InstrumentationData;
import com.instrumentation.data.MethodData;
import com.instrumentation.exceptions.UnsupportedClassException;
import com.instrumentation.visitors.InstrumenterInstancerFieldVisitor;
import com.instrumentation.visitors.InstrumenterInstancerMethodVisitor;

/**
 * 
 * 
 * 
 * @author José Antonio Álvarez Lapido
 *
 */
public class Extractor extends ClassVisitor {

	private static final int OPCODE_ASM = Opcodes.ASM7;
	
    private final InstrumentersContainer instrumentersContainer;
    private final InstrumentationData instrumentationData;

    /**
     * Instancia un {@link Extractor}.
     */
    public Extractor() {
    	super(OPCODE_ASM);
        this.instrumentationData = new InstrumentationData();
        this.instrumentersContainer = new InstrumentersContainer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
            final Object value) {
        final FieldData fieldData = new FieldData(access, name, desc, signature, value);
        this.instrumentationData.getFieldDatas().add(fieldData);
        return new InstrumenterInstancerFieldVisitor(fieldData, instrumentersContainer, instrumentationData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
            final String[] exceptions) {
    	try {
	        final MethodVisitor mv = VisitorMethodCallsStacker.create(MethodVisitor.class);
	        final VisitorMethodCallsStacker<MethodVisitor> extractor = VisitorMethodCallsStacker.getAsmExtractor(mv);
	        final MethodData methodData = new MethodData(access, name, desc, signature, exceptions);
	        this.instrumentationData.putMethodData(methodData, extractor.getFunctions());
	        return new InstrumenterInstancerMethodVisitor(mv, methodData, instrumentersContainer, instrumentationData);
    	}catch(UnsupportedClassException e) {
    		throw new RuntimeException(e);
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(final int version, final int access, final String name, final String signature,
            final String superName, final String[] interfaces) {
    	this.instrumentationData.setClassNameInstrumenter(name);
        this.instrumentationData.getInterfaces().addAll(Arrays.asList(interfaces));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        final Type classInstrumenterAnnotationType = Type.getType(InstrumentClass.class);
        if (classInstrumenterAnnotationType.toString().equals(desc)) {
            return new AnnotationVisitor(OPCODE_ASM) {
                @Override
                public void visit(final String name, final Object value) {
                    getInstrumentationData().setClassNameToInstrumenter((String) value);
                }
            };
        }
        return null;
    }
    
    private InstrumentationData getInstrumentationData() {
        return this.instrumentationData;
    }

    public String getClassNameToInstrument() {
        return this.instrumentationData.getClassNameToInstrumenter();
    }

    public InstrumentersContainer getInstrumentersContainer() {
        return this.instrumentersContainer;
    }
}
