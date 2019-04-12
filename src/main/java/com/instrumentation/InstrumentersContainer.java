package com.instrumentation;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import com.instrumentation.instrumenters.Instrumenter;

/**
 * Contiene las instancias de los {@link Instrumenter}.
 * <p>
 * Esta clase mantiene un mapa con los instrumenters en función de su clase. Para cada tipo de instrumenter solo existirá 
 * una instancia que será la encargada de instrumentar la clase objetivo.
 * 
 * Invocando al método <code>applyInstrumenters</code> sobre un {@link ClassVisitor} compondremos un nuevo {@link ClassVisitor}
 * que contendrá los cambios de todos los instrumenters contenidos en esta clase. De esta forma solo hará falta pasar
 * dicho visitor por un {@link ClassWriter} para realizar todos los cambios sobre el bytecode de la clase objetivo. 
 * 
 * 
 * @author José Antonio Álvarez Lapido
 *
 */
public class InstrumentersContainer {

	/**
	 * Mapa que contiene los instrumenters por tipo.
	 */
    private final Map<Class<?>, Instrumenter<?>> instrumentersMap;

    /**
     * Instancia un {@link InstrumentersContainer}.
     */
    public InstrumentersContainer() {
        this.instrumentersMap = new HashMap<>();
    }

    /**
     * Recupera el {@link Instrumenter} del tipo especificado. En caso de que no exista en el contenedor aún, se encargará
     * de instanciarlo.
     * 
     * @param instrumenterClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Instrumenter<T> getInstrumenter(final Class<? extends Instrumenter<T>> instrumenterClass) {
        Instrumenter<T> instrumenter = (Instrumenter<T>) this.instrumentersMap.get(instrumenterClass);
        if (instrumenter == null) {
            try {
                instrumenter = instrumenterClass.newInstance();
                this.instrumentersMap.put(instrumenterClass, instrumenter);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return instrumenter;
    }

    /**
     * Aplica los instrumenters al {@link ClassVisitor} recibido y devuelve un ClassVisitor con la composicion 
     * de todos los instrumenters.
     * 
     * @param classVisitor
     * @return 
     */
    public ClassVisitor applyInstrumenters(final ClassVisitor classVisitor) {
        ClassVisitor modifiedClassVisitor = classVisitor;
        for (final Instrumenter<?> instrumenter : this.instrumentersMap.values()) {
            modifiedClassVisitor = instrumenter.instrument(modifiedClassVisitor);
        }
        return modifiedClassVisitor;
    }
}
