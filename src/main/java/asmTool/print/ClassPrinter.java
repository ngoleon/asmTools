package asmTool.print;

import asmTool.util.DebugLog;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class ClassPrinter {
    public static void printClassOverview(ClassNode clazz) {
        DebugLog.clazz("%s", clazz.name);
        DebugLog.raw(DebugLog.CYAN, "  Super: %s", clazz.superName);
        if (clazz.interfaces != null && !clazz.interfaces.isEmpty()) {
            DebugLog.raw(DebugLog.CYAN, "  Interfaces: %s", String.join(", ", clazz.interfaces));
        }
        DebugLog.raw(DebugLog.CYAN, "  Access: 0x%s", Integer.toHexString(clazz.access));
        System.out.println();
    }

    public static void printAllMethods(ClassNode clazz) {
        DebugLog.method("in %s", clazz.name);
        for (MethodNode m : clazz.methods) {
            String accessStr = getAccess(m.access);
            DebugLog.raw(DebugLog.GREEN, "  - %s %s%s", accessStr, m.name, m.desc);
        }

        System.out.println();
    }

    public static void printAllFields(ClassNode clazz) {
        DebugLog.field("in %s", clazz.name);
        for (FieldNode f : clazz.fields) {
            DebugLog.raw(DebugLog.BLUE, "  - %s %s : %s", f.name, getAccess(f.access), f.desc);
        }
        System.out.println();
    }
    public static void printJarOverview(List<ClassNode> classList) {
        DebugLog.raw(DebugLog.YELLOW, "[JAR] total classes (%d total)", classList.size());
        for (ClassNode clazz : classList) {
            DebugLog.raw(DebugLog.YELLOW, "  - %s", clazz.name);
        }
        System.out.println();
    }
    private static String getAccess(int access) {
        StringBuilder sb = new StringBuilder();
        if ((access & 0x0001) != 0) sb.append("public ");
        if ((access & 0x0002) != 0) sb.append("private ");
        if ((access & 0x0004) != 0) sb.append("protected ");
        if ((access & 0x0008) != 0) sb.append("static ");
        if ((access & 0x0010) != 0) sb.append("final ");
        return sb.toString().trim();
    }

}
