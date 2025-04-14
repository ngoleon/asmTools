package asmTool;

import java.io.File;
import java.util.List;

import asmTool.inject.PrintlnInjector;
import asmTool.io.CustomClassReader;
import asmTool.inject.TimerInjector;
import asmTool.print.ClassPrinter;
import asmTool.print.InstructionPrinter;
import asmTool.print.MethodPrinter;
import asmTool.util.DebugLog;
import asmTool.helpers.MethodHelper;
import asmTool.io.JarReader;
import asmTool.io.JarWriter;
import asmTool.util.Position;
import org.objectweb.asm.tree.*;
public class AsmTool {
    public static void main(String[] args) throws Exception {
        DebugLog.enabled = true;

        File jarFile = new File("src/main/resources/testTarget.jar");
        File editedFile = new File("src/main/resources/testTargetEdited.jar");
        List<ClassNode> classes = JarReader.loadClassesFromJar(jarFile);
        ClassPrinter.printJarOverview(classes);
        for (ClassNode clazz : classes) {
            ClassPrinter.printClassOverview(clazz);
            ClassPrinter.printAllMethods(clazz);
            ClassPrinter.printAllFields(clazz);
        }
        for (ClassNode clazz : classes) {
            System.out.println();
            DebugLog.clazz("== Inspecting class: %s ==", clazz.name);

            for (MethodNode method : clazz.methods) {
                InstructionPrinter.printInstructions(method);

                if (method.name.equals("sayHello")) {
                    PrintlnInjector.inject(method,">> Entering foo", Position.START);
                }

                if (method.name.equals("add")) {
                    PrintlnInjector.inject(method,"Starting nano timer", Position.START);
                    int timerIndex = TimerInjector.injectStart(method,"startTime");
                    TimerInjector.injectEnd(method, timerIndex);
                }
            }
        }

        JarWriter.writeClassesToJar(classes, jarFile, editedFile);
        // Optionally write back modified classes to disk
        // (or a new jar if you want to get fancy later)
    }
}
