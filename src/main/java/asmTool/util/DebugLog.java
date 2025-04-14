package asmTool.util;

public class DebugLog {
    public static boolean enabled = true;

    // ANSI color codes
    public static final String RESET = "\u001B[0m";
    public static final String GRAY = "\u001B[90m";
    public static final String RED = "\u001B[91m";
    public static final String GREEN = "\u001B[92m";
    public static final String YELLOW = "\u001B[93m";
    public static final String BLUE = "\u001B[94m";
    public static final String MAGENTA = "\u001B[95m";
    public static final String CYAN = "\u001B[96m";

    // Log with cyan by default
    public static void log(String msg) {
        if (enabled) System.out.println(CYAN + "[DEBUG] " + RESET + msg);
    }
    public static void log(String fmt, Object... args) {
        if (enabled) System.out.println(CYAN + "[DEBUG] " + RESET + String.format(fmt, args));
    }
    public static void clazz(String fmt, Object... args) {
        if (enabled) System.out.println(CYAN + "[CLASS] " + RESET + String.format(fmt, args));
    }
    public static void method(String fmt, Object... args) {
        if (enabled) System.out.println(GREEN + "[METHOD] " + RESET + String.format(fmt, args));
    }

    public static void field(String fmt, Object... args) {
        if (enabled) System.out.println(BLUE + "[FIELD] " + RESET + String.format(fmt, args));
    }

    public static void opcode(String fmt, Object... args) {
        if (enabled) System.out.println(MAGENTA + "[OPCODE] " + RESET + String.format(fmt, args));
    }

    // Optional: colored variants

    public static void warn(String fmt, Object... args) {
        if (enabled) System.out.println(YELLOW + "[WARN] " + RESET + String.format(fmt, args));
    }

    public static void error(String fmt, Object... args) {
        if (enabled) System.out.println(RED + "[ERROR] " + RESET + String.format(fmt, args));
    }

    public static void raw(String color, String fmt, Object... args) {
        if (enabled) System.out.println(color + String.format(fmt, args) + RESET);
    }


    public static void inject(String fmt, Object... args) {
        if (enabled) System.out.println(YELLOW + "[INJECT] " + RESET + String.format(fmt, args));
    }

    public static void classHeader(String name) {
        if (enabled) System.out.println(CYAN + "[CLASS] " + RESET + name);
    }

    public static void method(String name) {
        if (enabled) System.out.println(GREEN + "[METHOD] " + RESET + name);
    }

    public static void instruction(String line) {
        if (enabled) System.out.println(MAGENTA + "  " + line + RESET);
    }

    public static void info(String fmt, Object... args) {
        if (enabled) System.out.println(CYAN + "[INFO] " + RESET + String.format(fmt, args));
    }
}
