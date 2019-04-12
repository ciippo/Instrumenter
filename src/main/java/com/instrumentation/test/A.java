package com.instrumentation.test;

import java.util.List;

import com.instrumentation.annotations.FieldInstrumenter;
import com.instrumentation.annotations.InstrumentClass;
import com.instrumentation.annotations.MethodInstrumenter;
import com.instrumentation.instrumenters.impl.FieldAdder;
import com.instrumentation.instrumenters.impl.FieldAsReference;
import com.instrumentation.instrumenters.impl.MethodAdder;
import com.instrumentation.instrumenters.impl.MethodReplacer;

@InstrumentClass(name = "com.instrumentation.test.FieldAnnotationTest$B")
public class A implements ITestAddMethod {

    interface I {
        List<String> getInterfaces();

        String getClassNameToInstrumenter();
    }

    @FieldInstrumenter(instrumenter = FieldAsReference.class)
    private I data;
    private final String field;

    @FieldInstrumenter(instrumenter = FieldAdder.class)
    private String fasdf;

    public A() {
        this.field = "as";
    }

    @MethodInstrumenter(instrumenter = MethodReplacer.class)
    public void test() {
        this.fasdf = "asgfd";
        System.out.println(this.data.getInterfaces());
        System.out.println(this.data.getClassNameToInstrumenter());
    }

    @MethodInstrumenter(instrumenter = MethodReplacer.class)
    public void changeA() {
        noVisibleMethod(this.fasdf);
    }

    private void noVisibleMethod(final String param) {

    }

    @MethodInstrumenter(instrumenter = MethodAdder.class)
    @Override
    public void testB() {
        System.out.println(this.field);
        this.data.getClassNameToInstrumenter();
    }
}