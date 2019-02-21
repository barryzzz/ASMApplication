package com.example.ams_plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class CustomVisitor extends ClassVisitor {
    private boolean isTimeRecord;

    public CustomVisitor(int api) {
        super(api);
    }

    public CustomVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        methodVisitor = new AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, desc) {
            @Override
            protected void onMethodEnter() {
//                System.out.println("onMethodEnter:" + desc );


                if ("onClick".equals(name)) {

                    mv.visitMethodInsn(INVOKESTATIC, "com/example/lishoulin/amsapplication/DebouncedClick", "getInstance",
                            "()Lcom/example/lishoulin/amsapplication/DebouncedClick;", false);
                    mv.visitVarInsn(ALOAD, 1);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/example/lishoulin/amsapplication/DebouncedClick", "isCanClick",
                            "(Landroid/view/View;)Z", false);
                    Label label = new Label();
                    mv.visitJumpInsn(IFNE, label);
                    mv.visitInsn(RETURN);
                    mv.visitLabel(label);


                } else if ("onCreate".equals(name)) {
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

                if (isTimeRecord) {
                    mv.visitMethodInsn(INVOKESTATIC, "com/example/lishoulin/amsapplication/TimeUtil", "getInstance",
                            "()Lcom/example/lishoulin/amsapplication/TimeUtil;", false);
                    mv.visitLdcInsn(name + desc);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/example/lishoulin/amsapplication/TimeUtil", "recordEnter",
                            "(Ljava/lang/String;)V", false);
                }


            }

            @Override
            protected void onMethodExit(int i) {
                super.onMethodExit(i);
//                System.out.println("onMethodExit:" + name + " " + desc);
                if (isTimeRecord) {
                    mv.visitMethodInsn(INVOKESTATIC, "com/example/lishoulin/amsapplication/TimeUtil", "getInstance",
                            "()Lcom/example/lishoulin/amsapplication/TimeUtil;", false);
                    mv.visitLdcInsn(name + desc);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/example/lishoulin/amsapplication/TimeUtil", "recordExit",
                            "(Ljava/lang/String;)V", false);
                    isTimeRecord = false;
                }

            }

            @Override
            public AnnotationVisitor visitAnnotation(String s, boolean b) {
                System.out.println("visitAnnotation:" + s);
                if ("Lcom/example/lishoulin/amsapplication/TimeRecord;".equals(s)) {
                    isTimeRecord = true;
                } else {
                    isTimeRecord = false;
                }

                return super.visitAnnotation(s, b);
            }
        };

        return methodVisitor;
    }

}
