package asmTool.io;

import asmTool.util.DebugLog;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarReader {
    public static List<ClassNode> loadClassesFromJar(File jarFile) throws IOException {
        DebugLog.log("Opening jar: %s", jarFile.getName());

        List<ClassNode> classes = new ArrayList<>();
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    try (InputStream is = jar.getInputStream(entry)) {
                        CustomClassReader helper = new CustomClassReader(is);
                        classes.add(helper.classNode);
                        DebugLog.log("Loaded class: %s", helper.classNode.name);
                    }
                }
            }
        }
        return classes;
    }

}
