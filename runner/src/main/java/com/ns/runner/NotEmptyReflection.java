package com.ns.runner;

import com.ns.annotations.NotEmpty;

import java.lang.reflect.Field;

final class NotEmptyReflection {
    static void analyse(Object target) {
        Class<?> clazz = target.getClass();
        for (Field declaredField : clazz.getDeclaredFields()) {
            if (declaredField.getAnnotation(NotEmpty.class) != null) {
                if (!declaredField.getType().isAssignableFrom(String.class)) continue;
                declaredField.setAccessible(true);
                Person temp = new Person();

                try {
                    String value = (String) declaredField.get(temp);
                    if (value.isEmpty()) throw new RuntimeException("Field is empty");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Illegal access", e);
                }
            }
        }
    }
}
