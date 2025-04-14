package asmTool.print;

import asmTool.util.DebugLog;
import asmTool.util.OpcodeHelper;
import org.objectweb.asm.tree.*;

public class InstructionPrinter {
    public static void printInstructions(MethodNode method) {
        System.out.println();
        DebugLog.method("Instructions in method \"%s\":", method.name);
        for (AbstractInsnNode insn : method.instructions) {
            String op = OpcodeHelper.opcodeToString(insn.getOpcode());
            if (op.equalsIgnoreCase("NOT_FOUND")) continue;
            DebugLog.opcode("  %s", print(insn));
        }
    }

    public static String print(AbstractInsnNode insn) {
        if (insn == null) return "null";
        String base = OpcodeHelper.opcodeToString(insn.getOpcode());

        return switch (insn.getType()) {
            case AbstractInsnNode.VAR_INSN -> {
                VarInsnNode v = (VarInsnNode) insn;
                yield base + " var=" + v.var;
            }
            case AbstractInsnNode.TYPE_INSN -> {
                TypeInsnNode t = (TypeInsnNode) insn;
                yield base + " desc=" + t.desc;
            }
            case AbstractInsnNode.FIELD_INSN -> {
                FieldInsnNode f = (FieldInsnNode) insn;
                yield base + " " + f.owner + "." + f.name + " : " + f.desc;
            }
            case AbstractInsnNode.METHOD_INSN -> {
                MethodInsnNode m = (MethodInsnNode) insn;
                yield base + " " + m.owner + "." + m.name + m.desc;
            }
            case AbstractInsnNode.INVOKE_DYNAMIC_INSN -> {
                InvokeDynamicInsnNode i = (InvokeDynamicInsnNode) insn;
                yield base + " " + i.name + " " + i.desc;
            }
            case AbstractInsnNode.JUMP_INSN -> {
                JumpInsnNode j = (JumpInsnNode) insn;
                yield base + " -> label_" + System.identityHashCode(j.label);
            }
            case AbstractInsnNode.LDC_INSN -> {
                LdcInsnNode ldc = (LdcInsnNode) insn;
                yield base + " ldc=" + ldc.cst;
            }
            case AbstractInsnNode.INT_INSN -> {
                IntInsnNode i = (IntInsnNode) insn;
                yield base + " value=" + i.operand;
            }
            case AbstractInsnNode.LOOKUPSWITCH_INSN -> {
                yield base + " (lookup switch)";
            }
            case AbstractInsnNode.TABLESWITCH_INSN -> {
                yield base + " (table switch)";
            }
            case AbstractInsnNode.LABEL -> {
                yield "label_" + System.identityHashCode(insn);
            }
            case AbstractInsnNode.LINE -> {
                LineNumberNode l = (LineNumberNode) insn;
                yield "// line " + l.line;
            }
            default -> base;
        };
    }
}
