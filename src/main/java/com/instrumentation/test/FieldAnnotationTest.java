package com.instrumentation.test;

import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import com.instrumentation.VisitorMethodCallsStacker;
import com.instrumentation.data.InstrumentationData;
import com.instrumentation.exceptions.UnsupportedClassException;

public class FieldAnnotationTest {

    public static void main(final String[] args) throws IOException, UnsupportedClassException {
//        final B b = new B();
//        ((ITestAddMethod) b).testB();
//        b.test();
//        b.changeA();
//        System.err.println();
        
        
        ClassReader classReader = new ClassReader("com.instrumentation.test.instrumenter.A");
        ClassVisitor extractor;
		classReader.accept(extractor = VisitorMethodCallsStacker.create(ClassVisitor.class), 0);
        System.out.println();
        // final Extractor extractor = new Extractor();
        // final ClassReader classReader = new ClassReader("com.instrumentation.test.FieldAnnotationTest$A");
        // classReader.accept(extractor, 0);
        //
        // final ClassReader c = new ClassReader("com.instrumentation.test.FieldAnnotationTest$B");
        // final ClassWriter classWriter = new ClassWriter(0);
        // c.accept(extractor.instrument(classWriter), 0);
        //
        // saveClass(classWriter.toByteArray());
        //
        // System.out.println();
    }

    private static void saveClass(final byte[] byteArray) throws IOException {
        try (final FileOutputStream outputStream = new FileOutputStream("T:/test.class")) {
            outputStream.write(byteArray);
        }
    }

    public static class B {

        private final InstrumentationData data = new InstrumentationData();
        private String a;
        private String field;

        public B() {
            this.data.getInterfaces().add("Probando");
            this.data.setClassNameToInstrumenter("className");
        }

        public void test() {
            System.out.println(this.a);
        }

        public void changeA() {
            noVisibleMethod("fawef");
        }

        private void noVisibleMethod(final String param) {
            this.a = param;
        }
    }
}
