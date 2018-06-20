package cn.ollyice.support.butterknife.plugin

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import java.io.File
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC

/**
 * Created by admin on 2018/6/1.
 */

object FinalRClassBuilder {
    @Throws(Exception::class)
    fun brewJava(rFile: File, outputDir: File, packageName: String, className: String) {
        val compilationUnit = JavaParser.parse(rFile)
        val resourceClass = compilationUnit.types[0]

        var hashMap = HashMap<String,ArrayList<String>>()
        resourceClass.childNodes.forEach { className ->
            if (className is ClassOrInterfaceDeclaration) {
                hashMap.put(className.nameAsString,ArrayList<String>())
                className.childNodes.forEach { fieldName ->
                    if (fieldName is FieldDeclaration){
                        val fieldName = fieldName.variables[0].nameAsString
                        hashMap[className.nameAsString]?.add(fieldName)
                    }
                }
            }
        }
        if(hashMap.size > 0){
            val result = TypeSpec.classBuilder(className).addModifiers(PUBLIC).addModifiers(FINAL)

            hashMap.forEach { type, arrayList ->
                val resourceType = TypeSpec.classBuilder(type).addModifiers(PUBLIC, STATIC, FINAL)
                arrayList.forEach { key ->
                    val fieldSpecBuilder = FieldSpec.builder(String::class.javaObjectType, key)
                            .addModifiers(PUBLIC, STATIC, FINAL)
                            .initializer("\"R." + type + "." +  key + "\"")
                    resourceType.addField(fieldSpecBuilder.build())
                }
                result.addType(resourceType.build())
            }

            val finalR = JavaFile.builder(packageName, result.build())
                    .addFileComment("Generated code from Butter Knife gradle plugin. Do not modify!")
                    .build()

            finalR.writeTo(outputDir)
        }
    }

}
