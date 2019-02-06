package com.ns.processor;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;
import com.ns.annotations.NotEmpty;
import com.ns.annotations.NotEmptyClass;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
public class NotEmptyProcessor extends AbstractProcessor {
    private final String STRING_TYPE = "java.lang.String";

    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<TypeElement, Binding> bindingMap = findAndParseAnnotations(roundEnv);

        for (Map.Entry<TypeElement, Binding> entry : bindingMap.entrySet()) {
            TypeElement typeElement = entry.getKey();
            Binding binding = entry.getValue();

            JavaFile javaFile = binding.createJava();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        String.format("Error writing java file for type %s", typeElement),
                        typeElement);
            }
        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add(NotEmptyClass.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Map<TypeElement, Binding> findAndParseAnnotations(RoundEnvironment env) {
        Map<TypeElement, Binding> map = new LinkedHashMap<>();

        for (Element element : env.getElementsAnnotatedWith(NotEmptyClass.class)) {
            if (element.getKind() != ElementKind.CLASS) continue;

            ImmutableList.Builder<FieldName> fieldBindings = ImmutableList.builder();
            for (Element enclosedElement : element.getEnclosedElements()) {
                if (enclosedElement .getAnnotation(NotEmpty.class) == null) continue;

                TypeMirror typeMirror = enclosedElement.asType();
                if (typeMirror.getKind() == TypeKind.DECLARED &&
                        isSameType(typeMirror, STRING_TYPE) &&
                        !hasModifier(enclosedElement, Modifier.PRIVATE)) {

                    String name = enclosedElement.getSimpleName().toString();
                    fieldBindings.add(new FieldName(name));
                }
            }

            TypeElement enclosingElement = (TypeElement) element;
            String packageName = getPackage(enclosingElement).getQualifiedName().toString();
            String className = enclosingElement.getSimpleName().toString();

            ClassName genClassName = ClassName.get(packageName, className + "_Analyser");
            TypeName targetTypeName = TypeName.get(enclosingElement.asType());

            Binding binding = new Binding(genClassName, targetTypeName, fieldBindings.build());
            map.put(enclosingElement, binding);
        }

        return map;
    }

    private boolean isSameType(TypeMirror typeMirror, String otherType) {
        return typeMirror.toString().equals(otherType);
    }

    private boolean hasModifier(Element element, Modifier modifier) {
        return element.getModifiers().contains(modifier);
    }

    private PackageElement getPackage(Element element) {
        while (element.getKind() != ElementKind.PACKAGE) {
            element = element.getEnclosingElement();
        }

        return (PackageElement) element;
    }
}
