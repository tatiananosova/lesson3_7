package ru.geekbrains;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {
    private static final Logger logger = Logger.getLogger(TestRunner.class);

    public static void start(Class<?> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        logger.info("Starting tests for class " + clazz.getSimpleName());
        Object o = clazz.getConstructor().newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> beforeSuiteMethods = new ArrayList<>();
        Map<Method, Integer> testMethods = new HashMap<>();
        List<Method> afterSuiteMethods = new ArrayList<>();
        for (Method method : methods) {
            logger.debug("Method " + method);
            if (method.isAnnotationPresent(Test.class)) {
                Test testAnnotation = method.getAnnotation(Test.class);
                testMethods.put(method, testAnnotation.priority());
            }
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                beforeSuiteMethods.add(method);
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                afterSuiteMethods.add(method);
            }
        }
        Map<Method, Integer> testMethodsSorted = sortByValue(testMethods);
        if (beforeSuiteMethods.size() > 1) {
            throw new RuntimeException("Class should have no more than one BeforeSuite");
        }
        if (afterSuiteMethods.size() > 1) {
            throw new RuntimeException("Class should have no more than one AfterSuite");
        }
        for (Method method : beforeSuiteMethods) {
            method.invoke(o);
        }
        for (Method method : testMethodsSorted.keySet()) {
            method.invoke(o);
        }
        for (Method method : afterSuiteMethods) {
            method.invoke(o);
        }
    }

    private static Map<Method, Integer> sortByValue(Map<Method, Integer> map) {
        List<Map.Entry<Method, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map<Method, Integer> resultMap = new LinkedHashMap<>();
        for (Map.Entry<Method, Integer> entry: list) {
            resultMap.put(entry.getKey(), entry.getValue());
        }
        return resultMap;
    }
}
