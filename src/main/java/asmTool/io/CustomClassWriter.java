package asmTool.io;
import org.objectweb.asm.ClassWriter;

public class CustomClassWriter extends ClassWriter {
    public CustomClassWriter(int flags) {
        super(flags);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        // Provide a default implementation to avoid class loading
        return "java/lang/Object";
    }
}
