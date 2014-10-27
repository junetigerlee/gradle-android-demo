package cn.com.incito.wisdom.sdk.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

public class ReflectionUtils {
    public static final String TAG = "ReflectionUtils";

    public static Field getField(Field[] fields, String fieldName)
            throws Exception {
        Field f = null;

        for (Field lf : fields) {
            String currentFieldName = lf.getName();
            if (currentFieldName.equals(fieldName)) {
                f = lf;
                break;
            }
        }

        if (f == null)
            throw new Exception(new String(fieldName + " field not found!"));

        return f;
    }

    static void describeClassOrInterface(Class<?> className, String name) {
        displayModifiers(className.getModifiers());
        displayFields(className.getDeclaredFields());
        displayMethods(className.getDeclaredMethods());

        if (className.isInterface()) {
            Log.d(TAG, "Interface: " + name);
        } else {
            Log.d(TAG, "Class: " + name);
            displayInterfaces(className.getInterfaces());
            displayConstructors(className.getDeclaredConstructors());
        }
    }

    static void displayModifiers(int m) {
        Log.d(TAG, "Modifiers: " + Modifier.toString(m));
    }

    static void displayInterfaces(Class<?>[] interfaces) {
        if (interfaces.length > 0) {
            Log.d(TAG, "Interfaces: ");
            for (int i = 0; i < interfaces.length; ++i)
                Log.d("", interfaces[i].getName());
        }
    }

    static void displayFields(Field[] fields) {
        if (fields.length > 0) {
            Log.d(TAG, "Fields: ");
            for (int i = 0; i < fields.length; ++i)
                Log.d(TAG, fields[i].toString());
        }
    }

    static void displayConstructors(Constructor<?>[] constructors) {
        if (constructors.length > 0) {
            Log.d(TAG, "Constructors: ");
            for (int i = 0; i < constructors.length; ++i)
                Log.d(TAG, constructors[i].toString());
        }
    }

    static void displayMethods(Method[] methods) {
        if (methods.length > 0) {
            Log.d(TAG, "Methods: ");
            for (int i = 0; i < methods.length; ++i)
                Log.d(TAG, methods[i].toString());
        }
    }

    public static Field[] getAllFields(Class<?> klass) {
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(klass.getDeclaredFields()));

        if (klass.getSuperclass() != null) {
            fields.addAll(Arrays.asList(getAllFields(klass.getSuperclass())));
        }

        return fields.toArray(new Field[] {});
    }

    public static Package getPackage(Object obj) {
        final Class<?> cls = obj.getClass();
        return cls.getPackage();
    }

    public static Object getProperty(Object owner, String fieldName)
            throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        final Class<?> ownerClass = owner.getClass();
        final Field field = ownerClass.getField(fieldName);
        final Object property = field.get(owner);
        return property;
    }

    public static Object getStaticProperty(String className, String fieldName) {
        Object property = null;
        try {
            final Class<?> ownerClass = Class.forName(className);
            final Field field = ownerClass.getField(fieldName);
            property = field.get(ownerClass);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return property;
    }

    public static Object invokeMethod(Object owner, String methodName,
            Object[] args) throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        final Class<?> ownerClass = owner.getClass();
        final int length = args.length;
        final Class<?>[] argsClass = new Class[length];
        for (int i = 0, j = length; i < j; i++) {
            argsClass[i] = getRawVariableType(args[i]);
        }
        final Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(owner, args);
    }

    public static Object invokeMethod(String className,
            Class<?>[] constructArgsClass, Object[] constructArgs,
            String methodName, Class<?>[] methodArgsClass, Object[] methodArgs)
            throws InvocationTargetException {
        Object obj = null;
        try {
            final Class<?> ownerClass = Class.forName(className);
            final Constructor<?> constructor = ownerClass
                    .getConstructor(constructArgsClass);
            Object owner = constructor.newInstance(constructArgs);

            final Method method = ownerClass.getMethod(methodName,
                    methodArgsClass);
            obj = method.invoke(owner, methodArgs);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object invokeStaticMethod(String className,
            String methodName, Object[] args) throws ClassNotFoundException,
            SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        final Class<?> ownerClass = Class.forName(className);
        final int length = args.length;
        final Class<?>[] argsClassType = new Class[length];
        for (int i = 0, j = length; i < j; i++) {
            argsClassType[i] = getRawVariableType(args[i]);
        }
        final Method method = ownerClass.getDeclaredMethod(methodName,
                argsClassType);
        return method.invoke(null, args);
    }

    public static boolean isInstance(Object obj, Class<?> cls) {
        return cls.isInstance(obj);
    }

    public static Object newInstance(String className, Object[] args)
            throws NoSuchMethodException, ClassNotFoundException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        final Class<?> newoneClass = Class.forName(className);
        final int length = args.length;
        final Class<?>[] argsClass = new Class[length];
        for (int i = 0, j = length; i < j; i++) {
            argsClass[i] = getRawVariableType(args[i]);
        }
        final Constructor<?> constructor = newoneClass
                .getConstructor(argsClass);
        return constructor.newInstance(args);
    }

    public static void setConstructorAccessible(Constructor<?> constructor,
            boolean accessible) {
        constructor.setAccessible(accessible);
    }

    public static void setFieldAccessible(Field field, boolean accessible) {
        field.setAccessible(accessible);
    }

    public static void setMethodAccessible(Method method, boolean accessible) {
        method.setAccessible(accessible);
    }

    public static Class<?> getRawVariableType(Object variableClass) {
        Class<?> rawType = variableClass.getClass();
        if (variableClass == Boolean.class) {
            rawType = Boolean.TYPE;
        } else if (variableClass == Byte.class) {
            rawType = Byte.TYPE;
        } else if (variableClass == Double.class) {
            rawType = Double.TYPE;
        } else if (variableClass == Float.class) {
            rawType = Float.TYPE;
        } else if (variableClass == Integer.class) {
            rawType = Integer.TYPE;
        } else if (variableClass == Long.class) {
            rawType = Long.TYPE;
        } else if (variableClass == Short.class) {
            rawType = Short.TYPE;
        } else if (variableClass == Void.class) {
            rawType = Void.TYPE;
        }
        return rawType;
    }

    public static Object invokeObjectMethod(Object object, String methodName,
            Object... params) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        return invokeObjectMethod(object, methodName, false, params);
    }

    public static Object invokeObjectMethod(Object object, String methodName,
            boolean inParent, Object... params)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {

        Object result = null;

        Method[] methods = null;
        if (inParent) {
            methods = object.getClass().getMethods();
        } else {
            methods = object.getClass().getDeclaredMethods();
        }

        Method targetMethod = null;
        final int length = methods.length;
        for (int i = 0; i < length; i++) {
            final Method method = methods[i];
            final String name = method.getName();
            if (name.equals(methodName)) {
                targetMethod = method;
            }
        }
        if (targetMethod != null) {
            targetMethod.setAccessible(true);
            result = targetMethod.invoke(object, params);
        }
        return result;
    }
}
