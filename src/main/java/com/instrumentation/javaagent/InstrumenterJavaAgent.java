package com.instrumentation.javaagent;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.instrumentation.Extractor;
import com.instrumentation.InstrumentersContainer;

public class InstrumenterJavaAgent {

    private InstrumenterJavaAgent() {
        // no-op
    }

    public static void premain(final String agentArgs, final Instrumentation inst) {
        final File instrumentersPath = new File(agentArgs);
        final Map<String, InstrumentersContainer> instrumenterContainers = readInstrumenters(instrumentersPath);
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(final ClassLoader loader, final String className,
                    final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain,
                    final byte[] classfileBuffer) throws IllegalClassFormatException {
                final InstrumentersContainer instrumentersContainer = instrumenterContainers.get(className);
                if (instrumentersContainer != null) {
                    final ClassWriter classWriter = new ClassWriter(0);
                    final ClassReader classReader = new ClassReader(classfileBuffer);
                    classReader.accept(instrumentersContainer.applyInstrumenters(classWriter), 0);
                    return classWriter.toByteArray();
                }
                return classfileBuffer;
            }
        });
    }

    public static void agentmain(final String agentArgs, final Instrumentation inst) {
        premain(agentArgs, inst);
    }

    private static Map<String, InstrumentersContainer> readInstrumenters(final File instrumenterPath) {
        final Map<String, InstrumentersContainer> instrumenterContainers = new HashMap<>();
        for (final File file : instrumenterPath.listFiles()) {
            if (file.getAbsolutePath().endsWith(".class")) {
                read(file, instrumenterContainers);
            }
        }
        return instrumenterContainers;
    }

    private static void read(final File file, final Map<String, InstrumentersContainer> instrumenterContainers) {
        try {
            final byte[] bytes = Files.readAllBytes(file.toPath());
            final Extractor extractor = new Extractor();
            final ClassReader classReader = new ClassReader(bytes);
            classReader.accept(extractor, 0);
            instrumenterContainers.put(extractor.getClassNameToInstrument(), extractor.getInstrumentersContainer());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
