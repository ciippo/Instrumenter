/**
 * AsmExtractor.java 01-oct-2018
 *
 * Copyright 2018 INDITEX.
 * Departamento de Sistemas
 */
package com.instrumentation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;

import com.instrumentation.exceptions.UnsupportedClassException;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.InvocationHandler;

/**
 * Utilizada en las clases <code>Visitor</code> de la librería ASM.
 * </p>
 * Proxy dinámico que extrae las llamadas a una interface en forma de {@link Consumer} de forma que se puedan volver a
 * aplicar exactamente esas mismas llamadas en el mismo orden de ejecución. Esto permite hacer una replica de los
 * metodos que son visitados al recorrer el bytecode de una clase, pudiendo reescribirlos igual posteriormente gracias a
 * un <code>Writer</code>.
 *
 * @author José Antonio Álvarez Lapido
 * @param <T> Tipo de Visitor
 */
@SuppressWarnings("unchecked")
public class VisitorMethodCallsStacker<T> implements InvocationHandler {

	/**
	 * Clases soportadas para este proxy dinámico.
	 */
	private final static Class<?>[] SUPPORTED_CLASSES = {
			ClassVisitor.class,
			MethodVisitor.class,
			FieldVisitor.class,
			ModuleVisitor.class}; 
	
	/**
	 * Comprueba si la clase es soportada.
	 * 
	 * @param clazz
	 * @return true si la clase es soportada.
	 */
	private static boolean isSupportedClass(Class<?> clazz) {
    	for(Class<?> supportedClass:SUPPORTED_CLASSES) {
    		if(clazz.equals(supportedClass)) {
    			return true;
    		}
    	}
    	return false;
    }
	
    private final List<Consumer<T>> functions;
    private final List<VisitorMethodCallsStacker<?>> innerStackers;

    private VisitorMethodCallsStacker() {
        this.functions = new ArrayList<>();
        this.innerStackers = new ArrayList<>();
    }

    /**
     * Crea el.
     *
     * @param <T>
     *            tipo generico
     * @param clazz
     *            clazz
     * @return the t
     * @throws UnsupportedClassException 
     */
    public static <T> T create(final Class<T> clazz) throws UnsupportedClassException {
    	if(!isSupportedClass(clazz)) {
    		throw new UnsupportedClassException();
    	}
    	Enhancer enhancer = new Enhancer();
    	enhancer.setSuperclass(clazz);
    	enhancer.setCallback(new VisitorMethodCallsStacker<>());
    	return clazz.cast(enhancer.create(new Class<?>[] {int.class},new Object[] {Opcodes.ASM7}));
    }

    /**
     * Recupera el {@link VisitorMethodCallsStacker} interno del proxy.
     * 
     * @param visitor
     * @return
     */
    public static <T> VisitorMethodCallsStacker<T> getAsmExtractor(final T visitor) {
    	//Todos los proxys creados por CGLIB implementan la interface Factory
    	if(visitor instanceof Factory) {
    		return (VisitorMethodCallsStacker<T>) ((Factory) visitor).getCallbacks()[0];
    	}
        return null;
    }

    /**
     * Reconstruct.
     *
     * @param item
     *            item
     */
    private void reconstruct(final T item) {
        this.functions.forEach(f -> f.accept(item));
    }

    public List<Consumer<T>> getFunctions() {
        return this.functions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final Class<?> returnClass = method.getReturnType();
        Object returnProxy = null;
        if (returnClass != null && isSupportedClass(returnClass)) {
            // Creamos otro proxy dinámico como retorno
            returnProxy = create(returnClass);
        }
        final VisitorMethodCallsStacker<Object> asmExtractor = addToInnerExtractors(returnProxy);
        final Consumer<T> c = t -> {
            try {
                final Object returnObject = method.invoke(t, args);
                if ((returnObject != null) && (asmExtractor != null)) {
                    asmExtractor.reconstruct(returnObject);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
        this.functions.add(c);
        return returnProxy;
    }

    private VisitorMethodCallsStacker<Object> addToInnerExtractors(final Object object) {
        VisitorMethodCallsStacker<Object> extractor = null;
        if (object != null) {
            extractor = (VisitorMethodCallsStacker<Object>) ((Factory) object).getCallbacks()[0];
            this.innerStackers.add(extractor);
        }
        return extractor;
    }
}
