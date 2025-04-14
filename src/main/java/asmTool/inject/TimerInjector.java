package asmTool.inject;

import asmTool.helpers.MethodHelper;
import asmTool.util.DebugLog;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import static asmTool.util.VarIndexAllocator.getNext;

public class TimerInjector {
    public static int injectStart(MethodNode method, String varName) {
        int nextIndex = getNext(method);
        InsnList inject = new InsnList();
        inject.add(new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "java/lang/System", "nanoTime", "()J", false
        ));
        inject.add(new VarInsnNode(Opcodes.LSTORE, nextIndex));
        method.instructions.insert(inject);
        DebugLog.inject("Injected System.nanoTime() store as %s at index %d", varName, nextIndex);
        return nextIndex;
    }


    public static void injectEnd(MethodNode method, int startIndex) {
        int tempIndex = getNext(method); // get a safe local var index

        InsnList inject = new InsnList();

        // long elapsed = System.nanoTime() - startTime;
        inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false));
        inject.add(new VarInsnNode(Opcodes.LLOAD, startIndex));
        inject.add(new InsnNode(Opcodes.LSUB));
        inject.add(new VarInsnNode(Opcodes.LSTORE, tempIndex));

        // System.out.println("Elapsed ns: " + elapsed);
        inject.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        inject.add(new TypeInsnNode(Opcodes.NEW, "java/lang/StringBuilder"));
        inject.add(new InsnNode(Opcodes.DUP));
        inject.add(new LdcInsnNode("Elapsed ns: "));
        inject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false));
        inject.add(new VarInsnNode(Opcodes.LLOAD, tempIndex));
        inject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false));
        inject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false));
        inject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

        MethodHelper.findLastReturnInsn(method).ifPresent(ret -> method.instructions.insertBefore(ret, inject));

        DebugLog.inject("Injected elapsed timer log before return in %s", method.name);
    }
}
