package com.mindfulpayments.featurely

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.validate
import com.mindfulpayments.featurely.annotations.FeatureFlag
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName

class FeatureFlagProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(FeatureFlag::class.qualifiedName.orEmpty())
            .filterIsInstance<KSClassDeclaration>()
            .filter(KSNode::validate)

        val featureFlags = symbols
            .mapNotNull { classDeclaration ->
                val annotation = classDeclaration.annotations.firstOrNull {
                    it.shortName.asString() == "FeatureFlag"
                } ?: return@mapNotNull null

                val name =
                    annotation.arguments.firstOrNull { it.name?.asString() == "name" }?.value as? String
                val default =
                    annotation.arguments.firstOrNull { it.name?.asString() == "default" }?.value as? Boolean

                name?.let { name to (default ?: false) }
            }.toList()
        if (!featureFlags.isEmpty()) {
            generateFeatureFlagRegistry(featureFlags)
        }
        return emptyList()
    }

    private fun generateFeatureFlagRegistry(
        featureFlags: List<Pair<String, Boolean>>
    ) {
        val className = "FeatureFlagRegistry"
        val packageName = "com.mindfulpayments.featurely"
        val enumClassName = "FeatureFlags"

        val enumTypeSpec = TypeSpec.enumBuilder(enumClassName)
        featureFlags.forEach { (name, _) ->
            enumTypeSpec.addEnumConstant(name)
        }

        val enumClass = enumTypeSpec.build()

        val featureFlagRegistryTypeSpec = TypeSpec.objectBuilder(className)
            .addProperty(
                PropertySpec.builder(
                    "flags",
                    HashMap::class.asClassName().parameterizedBy(
                        ClassName(packageName, enumClassName),
                        Boolean::class.asTypeName()
                    )
                )
                    .initializer("hashMapOf()")
                    .build()
            )
            .addInitializerBlock(
                CodeBlock.builder().apply {
                    featureFlags.forEach { (name, default) ->
                        addStatement("flags[FeatureFlags.$name] = %L", default)
                    }
                }.build()
            )
            .addFunction(
                FunSpec.builder("setFlag")
                    .addParameter("flag", ClassName(packageName, enumClassName))  // Enum parameter
                    .addParameter("value", Boolean::class)
                    .addStatement("flags[flag] = value")
                    .build()
            )
            .addFunction(
                FunSpec.builder("getFlag")
                    .addParameter("flag", ClassName(packageName, enumClassName))  // Enum parameter
                    .returns(Boolean::class)
                    .addStatement("return flags[flag] ?: false")  // Default to false if the flag is not found
                    .build()
            )
            .build()

        val fileSpec = FileSpec.builder(packageName, className)
            .addType(enumClass)
            .addType(featureFlagRegistryTypeSpec)
            .build()


        environment.codeGenerator.createNewFile(
            Dependencies(false),
            packageName,
            className
        ).bufferedWriter().use { writer ->
            fileSpec.writeTo(writer)
        }
    }

}
