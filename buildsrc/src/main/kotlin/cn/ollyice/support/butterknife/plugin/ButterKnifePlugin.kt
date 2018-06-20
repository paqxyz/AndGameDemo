package cn.ollyice.support.butterknife.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import groovy.util.XmlSlurper
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream
import java.io.RandomAccessFile
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KClass

/**
 * Created by admin on 2018/6/1.
 */

class ButterKnifePlugin() : Plugin<Project>{
    override fun apply(project: Project) {
        project.plugins.all {
            when(it){
                is LibraryPlugin -> {
                    project.extensions[LibraryExtension::class].run {
                        configureR3Generation(project, libraryVariants)
                    }
                }
                is AppPlugin -> {
                    project.extensions[AppExtension::class].run {
                        configureR3Generation(project, applicationVariants)
                    }
                }
            }
        }
    }

    private fun getPackageName(variant : BaseVariant) : String {
        val slurper = XmlSlurper(false, false)
        val list = variant.sourceSets.map { it.manifestFile }

        // According to the documentation, the earlier files in the list are meant to be overridden by the later ones.
        // So the first file in the sourceSets list should be main.
        val result = slurper.parse(list[0])
        return result.getProperty("@package").toString()
    }

    private fun configureR3Generation(project: Project, variants: DomainObjectSet<out BaseVariant>) {
        variants.all { variant ->
            val outputDir = project.buildDir.resolve(
                    "generated/source/r3/${variant.dirName}")

            val task = project.tasks.create("generate${variant.name.capitalize()}R3")
            task.outputs.dir(outputDir)
            variant.registerJavaGeneratingTask(task, outputDir)

            val rPackage = getPackageName(variant)
            val once = AtomicBoolean()

            variant.outputs.forEach { output->
                val processResources = output.processResources
                task.dependsOn(processResources)

                // Though there might be multiple outputs, their R files are all the same. Thus, we only
                // need to configure the task once with the R.java input and action.
                if (once.compareAndSet(false, true)) {
                    val pathToR = rPackage.replace('.', File.separatorChar)
                    val rFile = processResources.sourceOutputDir.resolve(pathToR).resolve("R.java")

                    System.out.print(rFile)
                    System.out.print("\r\n")
                    task.apply {
                        inputs.file(rFile)

                        doLast {
                            FinalRClassBuilder.brewJava(rFile, outputDir, rPackage, "R3")
                        }
                    }
                }
            }
        }
    }

    private operator fun <T : Any> ExtensionContainer.get(type: KClass<T>): T {
        return getByType(type.java)!!
    }

    fun print(any: Any?){
        System.out.print(if (any == null){"null"}else{any})
        System.out.print("\r\n")
    }
}
