package chapter03.two.four;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;

public class ASMTestMain {

    private final static DynamicClassLoader TEST_CLASS_LOADER = new DynamicClassLoader(
            (URLClassLoader) ASMTestMain.class.getClassLoader()
    );

    public static void main(String[] args) throws ClassNotFoundException, IOException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, SecurityException,
            InvocationTargetException, NoSuchMethodException {
        //在字节码增强前记录一个Class
        Class<?> beforeASMClass = TEST_CLASS_LOADER.loadClass("chapter03.two.four.ForASMTestClass");

        //我们重新装载修改后的类
        TEST_CLASS_LOADER.defineClassByByteArray("chapter03.two.four.ForASMTestClass", asmChangeClassCall());
        Class<?> afterASMClass = TEST_CLASS_LOADER.loadClass("chapter03.two.four.ForASMTestClass");

        //分别通过新老class创建对象
        Object beforeObject = beforeASMClass.newInstance();
        Object afterObject = afterASMClass.newInstance();

        //分表调用它们的代码
        beforeASMClass.getMethod("display1").invoke(beforeObject);
        System.out.println();
        afterASMClass.getMethod("display1").invoke(afterObject);
    }

    private static byte[] asmChangeClassCall() throws IOException {
        ClassReader classReader = new ClassReader("chapter03.two.four.ForASMTestClass");

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ASMClassModifyAdpter modifyAdpter = new ASMClassModifyAdpter(1, classWriter);
        classReader.accept(modifyAdpter, ClassReader.SKIP_DEBUG);
        //这里输出的字节码，可以用javap命令来查看哦
        //byte []bytes = classWriter.toByteArray();
        //new FileOutputStream("d:/ForASMTestClass.class").write(bytes);
        //return bytes;

        return classWriter.toByteArray();
    }
}
