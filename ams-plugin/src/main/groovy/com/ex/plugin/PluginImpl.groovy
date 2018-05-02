package com.ex.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.example.ams_plugin.View$ClickVisitor
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

public class PluginImpl extends Transform implements Plugin<Project> {


    @Override
    String getName() {
        return "PluginImpl"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(this)
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        println '//===============Plugin visit start===============//'

        if (outputProvider != null)
            outputProvider.deleteAll()

        inputs.each { TransformInput input ->  //对目录下的class文件插桩
            input.directoryInputs.each { DirectoryInput directoryInput ->

                //代码插桩
                if (directoryInput.file.isDirectory()) {

                    directoryInput.file.eachFileRecurse { File file ->
                        def name = file.name
                        println name
                        if (name.equals("MainActivity.class")) {
                            ClassReader classReader = new ClassReader(file.bytes)
                            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)

//                            ClassVisitor cv = new ToastVisitor(classWriter)
                            ClassVisitor cv=new View$ClickVisitor(classWriter);

                            classReader.accept(cv, ClassReader.EXPAND_FRAMES)
                            byte[] bytes = classWriter.toByteArray()
                            FileOutputStream fos = new FileOutputStream(file.parentFile.absolutePath + File.separator + name)
                            fos.write(bytes)
                            fos.close()
                        }
                    }
                }



                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

            input.jarInputs.each { JarInput jarInput ->  //对jar文件代码插桩
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                //代码插桩
                def dest = outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)  //关键步骤，如果没有，出现class找不到问题
            }

        }
        println '//===============Plugin visit end===============//'

    }
}
