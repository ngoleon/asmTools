package asmTool.inject;

import asmTool.util.DebugLog;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ReturnInjector {
    public static void override(MethodNode method, Object value) {
        method.instructions.clear();

        if (value instanceof String s) {
            method.instructions.add(new LdcInsnNode(s));
            method.instructions.add(new InsnNode(Opcodes.ARETURN));
        } else if (value instanceof Integer i) {
            method.instructions.add(new LdcInsnNode(i));
            method.instructions.add(new InsnNode(Opcodes.IRETURN));
        }
        DebugLog.inject("Replaced method %s with constant return: %s", method.name, value);
    }
}

