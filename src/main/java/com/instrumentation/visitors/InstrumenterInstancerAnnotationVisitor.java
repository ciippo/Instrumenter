package com.instrumentation.visitors;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.instrumentation.InstrumentersContainer;
import com.instrumentation.data.InstrumentationData;
import com.instrumentation.instrumenters.Instrumenter;

/**
 * {@link AnnotationVisitor} que analiza los valores colocados en una anotación y en caso de que dicho valor 
 * se trate de una clase de tipo {@link Instrumenter}, la instancia e inicializa manteniendo la instancia dentro
 * del {@link InstrumentersContainer}.
 * 
 * @author José Antonio Álvarez Lapido
 *
 * @param <T> Tipo de datos con el que trabaja el {@link Instrumenter} a instanciar por esta clase.
 */
public class InstrumenterInstancerAnnotationVisitor <T> extends AnnotationVisitor{

	private static final Logger LOGGER = 
			Logger.getLogger("com.instrumentation.visitors.InstrumenterInstancerAnnotationVisitor");
	
	private final InstrumentersContainer instrumentersContainer;
    private final T data;
    private final InstrumentationData instrumentationData;

    public InstrumenterInstancerAnnotationVisitor(final InstrumentersContainer instrumentersContainer,
            final InstrumentationData instrumentationData, final T data) {
    	super(Opcodes.ASM7);
        this.instrumentersContainer = instrumentersContainer;
        this.data = data;
        this.instrumentationData = instrumentationData;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void visit(final String name, final Object value) {
    	if(value instanceof Type) {
            final Type type = (Type) value;
            final String className = type.getClassName();
            final Class<?> clazz = getClass(className);
            if (Instrumenter.class.isAssignableFrom(clazz)) {
                final Class<Instrumenter<T>> castedInstrumenterClass = (Class<Instrumenter<T>>) clazz;
                final Instrumenter<T> instrumenter = this.instrumentersContainer.getInstrumenter(castedInstrumenterClass);
                instrumenter.setInstrumentationData(this.instrumentationData);
                instrumenter.addData(this.data);
            }
    	}
    }

    /**
     * Recupera la clase en funcion del nombre.
     * 
     * @param className
     * @return class
     */
    private static Class<?> getClass(final String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            LOGGER.log(Level.WARNING,"No se ha podido encontrar la clase especificada.", e);
        }
        return Void.class;
    }
}
