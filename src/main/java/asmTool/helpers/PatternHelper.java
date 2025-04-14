package asmTool.helpers;

import org.objectweb.asm.tree.*;

import java.util.*;
import java.util.function.Predicate;

public class PatternHelper {

    /**
     * Match a sequence of consecutive instructions against a predicate pattern.
     */
    public static List<AbstractInsnNode> matchConsecutive(MethodNode method, Predicate<AbstractInsnNode>... pattern) {
        List<AbstractInsnNode> result = new ArrayList<>();
        AbstractInsnNode[] insns = method.instructions.toArray();

        for (int i = 0; i <= insns.length - pattern.length; i++) {
            boolean match = true;
            for (int j = 0; j < pattern.length; j++) {
                if (!pattern[j].test(insns[i + j])) {
                    match = false;
                    break;
                }
            }
            if (match) result.add(insns[i]);
        }

        return result;
    }

    /**
     * Match any instruction matching any of the given predicates.
     */
    public static List<AbstractInsnNode> matchAnywhere(MethodNode method, Predicate<AbstractInsnNode>... filters) {
        List<AbstractInsnNode> result = new ArrayList<>();
        for (AbstractInsnNode insn : method.instructions) {
            for (Predicate<AbstractInsnNode> filter : filters) {
                if (filter.test(insn)) {
                    result.add(insn);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Match a repeating block pattern (e.g., GETSTATIC, LDC, INVOKEVIRTUAL).
     */
    public static List<List<AbstractInsnNode>> matchRepeated(MethodNode method, Predicate<AbstractInsnNode>... pattern) {
        List<List<AbstractInsnNode>> result = new ArrayList<>();
        AbstractInsnNode[] insns = method.instructions.toArray();

        for (int i = 0; i <= insns.length - pattern.length; i++) {
            boolean match = true;
            for (int j = 0; j < pattern.length; j++) {
                if (!pattern[j].test(insns[i + j])) {
                    match = false;
                    break;
                }
            }
            if (match) {
                List<AbstractInsnNode> block = new ArrayList<>();
                for (int j = 0; j < pattern.length; j++) block.add(insns[i + j]);
                result.add(block);
                i += pattern.length - 1; // skip ahead
            }
        }

        return result;
    }

    // === Common predicates ===

    public static Predicate<AbstractInsnNode> opcode(int opcode) {
        return insn -> insn.getOpcode() == opcode;
    }

    public static Predicate<AbstractInsnNode> isPrintln() {
        return insn ->
                insn instanceof MethodInsnNode m &&
                        m.owner.equals("java/io/PrintStream") &&
                        m.name.equals("println");
    }

    public static Predicate<AbstractInsnNode> isLdc(Object value) {
        return insn ->
                insn instanceof LdcInsnNode ldc &&
                        ldc.cst.equals(value);
    }

    public static Predicate<AbstractInsnNode> isMethod(String owner, String name, String desc) {
        return insn ->
                insn instanceof MethodInsnNode m &&
                        m.owner.equals(owner) &&
                        m.name.equals(name) &&
                        m.desc.equals(desc);
    }

    public static Predicate<AbstractInsnNode> any(Predicate<AbstractInsnNode>... patterns) {
        return insn -> Arrays.stream(patterns).anyMatch(p -> p.test(insn));
    }
}
