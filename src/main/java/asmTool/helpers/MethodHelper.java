package asmTool.helpers;

import asmTool.print.InstructionPrinter;
import asmTool.util.DebugLog;
import asmTool.util.OpcodeHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MethodHelper {
    /**
     * Get all return instructions in the method (IRETURN, RETURN, etc.)
     */
    public static List<AbstractInsnNode> findReturnInsns(MethodNode method) {
        List<AbstractInsnNode> result = new ArrayList<>();
        for (AbstractInsnNode insn : method.instructions) {
            int opcode = insn.getOpcode();
            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                result.add(insn);
            }
        }
        return result;
    }

    /**
     * Get the last return instruction in the method.
     */
    public static Optional<AbstractInsnNode> findLastReturnInsn(MethodNode method) {
        for (AbstractInsnNode insn = method.instructions.getLast(); insn != null; insn = insn.getPrevious()) {
            int opcode = insn.getOpcode();
            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                return Optional.of(insn);
            }
        }
        return Optional.empty();
    }

    /**
     * Get the first instruction (excluding labels, frames, line numbers).
     */
    public static AbstractInsnNode getFirstRealInsn(MethodNode method) {
        for (AbstractInsnNode insn : method.instructions) {
            if (insn.getOpcode() >= 0) return insn;
        }
        return null;
    }

    /**
     * Count the number of actual instructions (excluding labels, lines, etc.)
     */
    public static long countInstructions(MethodNode method) {
        return Arrays.stream(method.instructions.toArray())
                .filter(insn -> insn.getOpcode() >= 0)
                .count();
    }

    /**
     * Check if a method is empty (no instructions or only return).
     */
    public static boolean isEmpty(MethodNode method) {
        AbstractInsnNode first = getFirstRealInsn(method);
        return first == null || (first.getOpcode() >= Opcodes.IRETURN && first.getOpcode() <= Opcodes.RETURN);
    }

    /**
     * Check if a method contains any call to a specific method.
     */
    public static boolean callsMethod(MethodNode method, String owner, String name) {
        for (AbstractInsnNode insn : method.instructions) {
            if (insn instanceof MethodInsnNode m &&
                    m.owner.equals(owner) &&
                    m.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clone a method body (deep copy of instructions).
     */
    public static MethodNode clone(MethodNode original) {
        MethodNode clone = new MethodNode(original.access, original.name, original.desc, original.signature, original.exceptions.toArray(new String[0]));
        original.accept(clone);
        return clone;
    }
}
