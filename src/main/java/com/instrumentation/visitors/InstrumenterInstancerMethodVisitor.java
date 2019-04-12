package com.instrumentation.visitors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.instrumentation.InstrumentersContainer;
import com.instrumentation.data.InstrumentationData;
import com.instrumentation.data.MethodData;

/**
 * Analiza las anotaciones de los métodos utilizando el {@link InstrumenterInstancerAnnotationVisitor}.
 * 
 * @author José Antonio Álvarez Lapido
 *
 */
public class InstrumenterInstancerMethodVisitor extends MethodVisitor {
	
	private final MethodData methodData;
	private InstrumentersContainer instrumentersContainer;
	private InstrumentationData instrumentationData;

    public InstrumenterInstancerMethodVisitor(MethodVisitor mv,
    		MethodData methodData,
    		InstrumentersContainer instrumentersContainer,
    		InstrumentationData instrumentationData) {
    	super(Opcodes.ASM7,mv);
        this.methodData = methodData;
        this.instrumentersContainer = instrumentersContainer;
        this.instrumentationData = instrumentationData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        return new InstrumenterInstancerAnnotationVisitor<>(instrumentersContainer, 
        		instrumentationData, 
        		this.methodData);
    }
}
