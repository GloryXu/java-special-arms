package chapter03.two.four;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMClassModifyAdpter extends ClassVisitor {

    public ASMClassModifyAdpter(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }

    public MethodVisitor visitMethod(final int access, final String methodName,
                                     final String desc, final String signature, final String[] exceptions) {
        if ("display2".equals(methodName)) {
            return null;//我们屏蔽了这个方法
        }
        if ("display1".equals(methodName)) {
            MethodVisitor methodVisitor = cv.visitMethod(access, methodName, desc, signature, exceptions);
            methodVisitor.visitCode();
            //增加的语句等价于增加代码：name = "我是name"
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);// 加载this到栈顶
            methodVisitor.visitLdcInsn("我是name");// idc指令，从常量池中取出值加载到栈顶，这个代码会隐藏修改常量池
            // 将栈顶的值赋值给ForASMTestClass类的name属性
            methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, "chapter03/two/four/ForASMTestClass", "name", "Ljava/lang/String;");

            //这条语句等价于增加代码：value = "我是value";
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            methodVisitor.visitLdcInsn("我是value");
            // 将栈顶的值赋值给ForASMTestClass类的value属性
            methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, "chapter03/two/four/ForASMTestClass", "value", "Ljava/lang/String;");

            //再将一个属性获取出来打印出来
            methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            methodVisitor.visitFieldInsn(Opcodes.GETFIELD, "chapter03/two/four/ForASMTestClass", "name", "Ljava/lang/String;");
            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");

            methodVisitor.visitEnd();
            return methodVisitor;//返回visitor
        } else {
            return cv.visitMethod(access, methodName, desc, signature, exceptions);
        }
    }

}
