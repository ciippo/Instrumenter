package com.instrumentation.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.instrumentation.Extractor;

public class TestOutOfClasspath {

	private final static String FILE = "T:\\dev\\evolutivo_etiquetas\\Instrumenter2\\com\\instrumentation\\test\\instrumenter\\A.class";
	
	public static void main(String[] args) throws IOException {
		File file = new File(FILE);
		byte[] bytes = Files.readAllBytes(file.toPath());
		Extractor extractor = new Extractor();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(extractor, 0);
		
		final ClassReader c = new ClassReader("com.instrumentation.test.FieldAnnotationTest$B");
        final ClassWriter classWriter = new ClassWriter(0);
        c.accept(extractor.getInstrumentersContainer().applyInstrumenters(classWriter), 0);
		System.out.println();
	}
}
