package com.example.ams_plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class ToastVisitor extends ClassVisitor {
    private String classname;
    private String supername;

    public ToastVisitor(int api) {
        super(api);
    }

    public ToastVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        System.out.println("插桩初始化");
        this.classname = name;
        this.supername = superName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        methodVisitor = new AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, desc) {
            @Override
            protected void onMethodEnter() {
                System.out.println(name);
                if ("onCreate".equals(name)) {
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitMethodInsn(INVOKESTATIC,
                            "com/example/lishoulin/amsapplication/TraceUtil",
                            "onActivityCreate",
                            "(Landroid/app/Activity;)V",
                            false);
                } else if ("onDestroy".equals(name)) {
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitMethodInsn(INVOKESTATIC,
                            "com/example/lishoulin/amsapplication/TraceUtil",
                            "onActivityDestroy",
                            "(Landroid/app/Activity;)V",
                            false);
                }
            }
        };

        return methodVisitor;
    }
}
