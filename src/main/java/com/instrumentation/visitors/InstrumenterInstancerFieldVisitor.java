package com.instrumentation.visitors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import com.instrumentation.InstrumentersContainer;
import com.instrumentation.data.FieldData;
import com.instrumentation.data.InstrumentationData;

/**
 * Analiza las anotaciones de los campos utilizando el {@link InstrumenterInstancerAnnotationVisitor}.
 * 
 * @author José Antonio Álvarez Lapido
 *
 */
public class InstrumenterInstancerFieldVisitor extends FieldVisitor {
	
	private final FieldData fieldData;
	private InstrumentersContainer instrumentersContainer;
	private InstrumentationData instrumentationData;

    public InstrumenterInstancerFieldVisitor(final FieldData fieldData,
    		InstrumentersContainer instrumentersContainer,
    		InstrumentationData instrumentationData) {
    	super(Opcodes.ASM7);
        this.fieldData = fieldData;
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
        		this.fieldData);
    }
}
