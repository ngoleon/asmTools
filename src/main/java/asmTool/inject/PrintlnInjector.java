package asmTool.inject;

import asmTool.util.DebugLog;
import asmTool.util.Position;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class PrintlnInjector {
    public static void inject(MethodNode method, String message, Position pos) {
        InsnList insn = new InsnList();
        insn.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        insn.add(new LdcInsnNode(message));
        insn.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

        switch (pos) {
            case START -> method.instructions.insert(insn);
            case END -> method.instructions.add(insn);
        }
        DebugLog.inject("Println injected at %s in %s", pos, method.name);
    }
}
