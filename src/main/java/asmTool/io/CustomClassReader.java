package asmTool.io;

import asmTool.util.DebugLog;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;

public class CustomClassReader {
    public final ClassNode classNode;

    public CustomClassReader(InputStream inputStream) throws IOException {
        DebugLog.log("Loading class...");
        ClassReader reader = new ClassReader(inputStream);
        classNode = new ClassNode();
        reader.accept(classNode, 0);
        DebugLog.log("Loaded class: %s", classNode.name);
    }

    public byte[] toBytes() {
        DebugLog.log("Writing class to bytes...");
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public void saveToFile(String path) throws IOException {
        DebugLog.log("Saving to %s", path);
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(toBytes());
        }
    }
}
