package asmTool.util;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class VarIndexAllocator {
    public static int getNext(MethodNode method) {
        int maxIndex = 0;
        for (AbstractInsnNode insn : method.instructions.toArray()) {
            if (insn instanceof VarInsnNode varInsn) {
                int index = varInsn.var;
                int size = switch (insn.getOpcode()) {
                    case Opcodes.LLOAD, Opcodes.DLOAD, Opcodes.LSTORE, Opcodes.DSTORE -> 2;
                    default -> 1;
                };
                maxIndex = Math.max(maxIndex, index + size);
            }
        }
        return maxIndex;
    }
}
