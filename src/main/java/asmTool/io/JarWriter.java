package asmTool.io;

import asmTool.util.DebugLog;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class JarWriter {
    public static void writeClassesToJar(List<ClassNode> classNodes, File originalJar, File outputJar) throws IOException {
        DebugLog.raw(DebugLog.YELLOW, ">> Writing %d classes to new jar: %s", classNodes.size(), outputJar.getName());

        try (
                JarOutputStream jos = new JarOutputStream(new FileOutputStream(outputJar));
                JarFile original = new JarFile(originalJar)
        ) {
            Set<String> added = new HashSet<>();

            // 1. Write all modified classes
            for (ClassNode node : classNodes) {
                ClassWriter cw = new CustomClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                node.accept(cw);
                byte[] bytes = cw.toByteArray();

                String entryName = node.name + ".class";
                JarEntry entry = new JarEntry(entryName);
                jos.putNextEntry(entry);
                jos.write(bytes);
                jos.closeEntry();

                added.add(entryName);
                DebugLog.raw(DebugLog.GREEN, "  + %s", entryName);
            }

            // 2. Copy over non-class entries from original jar
            Enumeration<JarEntry> entries = original.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!added.contains(name) && !name.endsWith(".class")) {
                    jos.putNextEntry(new JarEntry(name));
                    InputStream in = original.getInputStream(entry);
                    in.transferTo(jos);
                    jos.closeEntry();
                }
            }

            DebugLog.raw(DebugLog.CYAN, ">> Saved new jar with resources preserved.");
        }
    }

}
