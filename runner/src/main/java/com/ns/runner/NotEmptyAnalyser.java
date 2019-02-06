package com.ns.runner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class NotEmptyAnalyser {
    private static final String ANALYSER_SUFFIX = "Analyser";
    private static final String ANALYSER_METHOD_NAME = "analyse";

    static void analyse(Object target) {
        final Class<?> clazz = target.getClass();
        final String className = clazz.getName();
        final String implName = className + "_" + ANALYSER_SUFFIX;

        try {
            final Class<?> aClass = Class.forName(implName);
            final Method method = aClass.getDeclaredMethod(ANALYSER_METHOD_NAME, clazz);
            method.invoke(NotEmptyAnalyser.class, target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
