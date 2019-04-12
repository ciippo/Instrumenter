package com.instrumentation.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.objectweb.asm.MethodVisitor;

/**
 * Contiene los datos necesarios que se van a extraer de la clase instrumentadora y se van a usar para 
 * instrumentar la clase objetivo.
 * 
 * <ul>
 * 	<li>Nombre de la clase objetivo (Clase a instrumentar)</li>
 * 	<li>Nombre de la clase instrumentadora</li>
 * 	<li>Datos de los campos</li>
 * 	<li>Lista de interfaces que implementa la clase instrumentadora</li>
 * 	<li>Datos de los métodos</li>
 * </ul>
 * 
 * @author José Antonio Álvarez Lapido
 *
 */
public class InstrumentationData {

    private String classNameInstrumenter;
    private String classNameToInstrumenter;
    private List<FieldData> fieldDatas;
    private List<String> interfaces;
    private Map<MethodData, List<Consumer<MethodVisitor>>> methodDatas;

    /**
     * Añade los datos del método y la lista de {@link Consumer} necesarios para duplicarlo.
     * 
     * @param methodData
     * @param functions
     */
    public void putMethodData(final MethodData methodData, final List<Consumer<MethodVisitor>> functions) {
        if (this.methodDatas == null) {
            this.methodDatas = new HashMap<>();
        }
        this.methodDatas.put(methodData, functions);
    }

    /**
     * Recupera la lista de {@link Consumer} necesarios para duplicar el método especificado.
     * 
     * @param access
     * @param name
     * @param desc
     * @param signature
     * @param exceptions
     * @return
     */
    public List<Consumer<MethodVisitor>> getMethodFunctions(final int access, final String name, final String desc,
            final String signature, final String[] exceptions) {
        final MethodData methodData = new MethodData(access, name, desc, signature, exceptions);
        return this.methodDatas.get(methodData);
    }

    /**
     * Recupera la lista de {@link Consumer} necesarios para duplicar el método especificado.
     * 
     * @param methodData
     * @return
     */
    public List<Consumer<MethodVisitor>> getMethodFunctions(final MethodData methodData) {
        return this.methodDatas.get(methodData);
    }

    /**
     * 
     * @param classNameInstrumenter
     */
    public void setClassNameInstrumenter(String classNameInstrumenter) {
		this.classNameInstrumenter = classNameInstrumenter;
	}
    
    /**
     * 
     * @return
     */
    public String getClassNameInstrumenter() {
		return classNameInstrumenter;
	}

    /**
     * 
     * @return
     */
    public String getClassNameToInstrumenter() {
        return this.classNameToInstrumenter;
    }

    /**
     * 
     * @param classNameToInstrumenter
     */
    public void setClassNameToInstrumenter(final String classNameToInstrumenter) {
        this.classNameToInstrumenter = classNameToInstrumenter.replace('.', '/');
    }

    /**
     * 
     * @return
     */
    public List<String> getInterfaces() {
        if (this.interfaces == null) {
            this.interfaces = new ArrayList<>();
        }
        return this.interfaces;
    }

    /**
     * 
     * @return
     */
    public List<FieldData> getFieldDatas() {
        if (this.fieldDatas == null) {
            this.fieldDatas = new ArrayList<>();
        }
        return this.fieldDatas;
    }
}