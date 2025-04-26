package asmTool;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class AnnotationCleaner {
    private static final File CACHE_DIR = new File("src/jars/");
    private static int classAnnotationCount = 0;
    private static int fieldAnnotationCount = 0;
    private static int methodAnnotationCount = 0;

    public static void main(String[] args) throws IOException {
        for (File inputFile : CACHE_DIR.listFiles()) {
            if (shouldSkipFile(inputFile)) {
                continue;
            }
            processFile(inputFile);
        }
    }

    private static boolean shouldSkipFile(File inputFile) {
        if (!inputFile.getName().endsWith(".jar") && !inputFile.getName().endsWith(".cache")) {
            return true;
        }
        if (inputFile.getName().contains("cleaned")) {
            return true;
        }
        for (File comparisonFile : CACHE_DIR.listFiles()) {
            if (comparisonFile.getName().contains("cleaned") && comparisonFile.getName().contains(inputFile.getName().replaceAll("\\.jar|\\.cache", ""))) {
                return true;
            }
        }
        return false;
    }

    private static void processFile(File inputFile) throws IOException {
        System.out.printf("Input: %s | Output: %s%n", inputFile.getName(), getOutputName(inputFile));
        String outputJarPath = getOutputFilePath(inputFile.getAbsolutePath());

        try (JarFile jarFile = new JarFile(inputFile);
             JarOutputStream jos = new JarOutputStream(new FileOutputStream(outputJarPath))) {

            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                try (InputStream is = jarFile.getInputStream(entry)) {
                    if (entry.getName().endsWith(".class")) {
                        processClassEntry(entry, is, jos);
                    } else {
                        copyEntry(entry, is, jos);
                    }
                }
            }

            printAnnotationCounts();
            resetAnnotationCounts();
        }
    }

    private static void processClassEntry(JarEntry entry, InputStream is, JarOutputStream jos) throws IOException {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(is);
        classReader.accept(classNode, 0);

        new AnnotationCleaner().patchAnnotations(classNode);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        byte[] modifiedClass = classWriter.toByteArray();

        JarEntry newEntry = new JarEntry(entry.getName());
        jos.putNextEntry(newEntry);
        jos.write(modifiedClass);
        jos.closeEntry();
    }

    private static void copyEntry(JarEntry entry, InputStream is, JarOutputStream jos) throws IOException {
        jos.putNextEntry(new JarEntry(entry.getName()));
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            jos.write(buffer, 0, bytesRead);
        }
        jos.closeEntry();
    }

    private static String getOutputFilePath(String inputJarPath) {
        int dotIndex = inputJarPath.lastIndexOf('.');
        return dotIndex > 0 ? inputJarPath.substring(0, dotIndex) + "-cleaned" + inputJarPath.substring(dotIndex) : inputJarPath + "-cleaned";
    }

    private static String getOutputName(File inputFile) {
        int dotIndex = inputFile.getName().lastIndexOf('.');
        return dotIndex > 0 ? inputFile.getName().substring(0, dotIndex) + "-cleaned" + inputFile.getName().substring(dotIndex) : inputFile.getName() + "-cleaned";
    }

    public void patchAnnotations(ClassNode classNode) {
        patchClassAnnotations(classNode);
        patchFieldAnnotations(classNode);
        patchMethodAnnotations(classNode);
    }

    private void patchClassAnnotations(ClassNode classNode) {
        if (classNode.invisibleAnnotations != null) {
            classNode.invisibleAnnotations.removeIf(annotationNode -> {
                if (annotationNode.desc.equals("Ljavax/inject/Named;")) {
                    classAnnotationCount++;
                    return true;
                }
                return false;
            });
        }
    }

    private void patchFieldAnnotations(ClassNode classNode) {
        for (FieldNode fieldNode : classNode.fields) {
            if (fieldNode.invisibleAnnotations != null) {
                fieldNode.invisibleAnnotations.removeIf(annotationNode -> {
                    if (annotationNode.desc.equals("Ljavax/inject/Named;")) {
                        fieldAnnotationCount++;
                        return true;
                    }
                    return false;
                });
            }
        }
    }

    private void patchMethodAnnotations(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.invisibleAnnotations != null) {
                methodNode.invisibleAnnotations.removeIf(annotationNode -> {
                    if (annotationNode.desc.equals("Ljavax/inject/Named;")) {
                        methodAnnotationCount++;
                        return true;
                    }
                    return false;
                });
            }
        }
    }

    private static void printAnnotationCounts() {
        System.out.printf("Dropped Annotations: %d | Class: %d | Field: %d | Method: %d%n",
                classAnnotationCount + fieldAnnotationCount + methodAnnotationCount,
                classAnnotationCount, fieldAnnotationCount, methodAnnotationCount);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private static void resetAnnotationCounts() {
        classAnnotationCount = 0;
        fieldAnnotationCount = 0;
        methodAnnotationCount = 0;
    }
}

