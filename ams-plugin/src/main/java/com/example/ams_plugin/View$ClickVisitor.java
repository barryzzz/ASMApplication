package com.example.ams_plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class View$ClickVisitor extends ClassVisitor {
    public View$ClickVisitor(int api) {
        super(api);
    }

    public View$ClickVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        if (((access & Opcodes.ACC_PUBLIC) != 0 && (access & Opcodes.ACC_STATIC) == 0)
                && name.equals("onClick")
                && desc.equals("(Landroid/view/View;)V")) {
            methodVisitor = new OnClickAdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, desc);
            return methodVisitor;
        } else {
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }


    private class OnClickAdviceAdapter extends AdviceAdapter {
        /**
         * Creates a new {@link AdviceAdapter}.
         *
         * @param api    the ASM API version implemented by this visitor. Must be one
         *               of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
         * @param mv     the method visitor to which this adapter delegates calls.
         * @param access the method's access flags (see {@link Opcodes}).
         * @param name   the method's name.
         * @param desc   the method's descriptor (see {@link Type Type}).
         */
        protected OnClickAdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
        }


        @Override
        public void visitCode() {
            super.visitCode();

            mv.visitVarInsn(ALOAD,1);
            mv.visitMethodInsn(INVOKESTATIC,
                    "com/example/lishoulin/amsapplication/DebouncedClick",
                    "isCanClick","(Landroid/view/View;)Z",false);
            Label label=new Label();
            mv.visitJumpInsn(IFNE,label);
            mv.visitInsn(RETURN);
            mv.visitLabel(label);
        }
    }
}
