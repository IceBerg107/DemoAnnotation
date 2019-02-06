package com.ns.processor;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

class Binding {
    private final ClassName className;
    private final TypeName targetTypeName;
    private final ImmutableList<FieldName> fieldNames;

    Binding(ClassName className, TypeName targetTypeName, ImmutableList<FieldName> fieldNames) {
        this.className = className;
        this.fieldNames = fieldNames;
        this.targetTypeName = targetTypeName;
    }

    JavaFile createJava() {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className.simpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        if (!fieldNames.isEmpty()) {

            MethodSpec.Builder method = MethodSpec.methodBuilder("analyse")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class);

            ParameterSpec param = ParameterSpec.builder(targetTypeName, "target")
                    .build();

            method.addParameter(param);
            method.addStatement("if ($L == null) throw new $T($S)",
                    param.name,
                    NullPointerException.class,
                    "target cannot be null");

            for (FieldName fieldName : fieldNames) {
                String accessor = String.format("%s.%s", param.name, fieldName.getName());
                method.addStatement("if ($L == null || $L.isEmpty()) throw new $T(\"$L is null or empty\")",
                        accessor,
                        accessor,
                        RuntimeException.class,
                        fieldName.getName());
            }

            classBuilder.addMethod(method.build());
        }

        return JavaFile.builder(className.packageName(), classBuilder.build())
                .addFileComment("Generated code - do not modify!")
                .build();
    }
}
