package li.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import li.dao.Record;

/**
 * 反射工具类,封装了一些反射方法
 * 
 * @author li (limw@w.cn)
 * @version 0.1.4 (2012-05-08)
 */
public class Reflect {
    /**
     * 缓存指定类型的指定属性的读写值方式,0或null=第一次或出错,1=通过get方法,2=通过Field,3=是Map
     */
    private static final Map<String, Integer> GETTER_SETTER_MAP = new HashMap<String, Integer>();

    /**
     * 缓存指定类型的指定属性的数据类型
     */
    private static final Map<String, Class<?>> FIELD_TYPE_MAP = new HashMap<String, Class<?>>();

    /**
     * 得到所有属性,包括超类中的
     * 
     * @param type
     */
    public static List<Field> getFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        Field[] fs = type.getDeclaredFields();
        for (Field field : fs) {
            fields.add(field);
        }
        if (Object.class != type.getSuperclass()) {// 扫描超类的Field
            fields.addAll(getFields(type.getSuperclass()));
        }
        return fields;
    }

    /**
     * 根据传入的类名返回对应的Class对象
     */
    public static <T> Class<T> getType(String type) {
        try {
            return (Class<T>) Class.forName(type);
        } catch (Exception e) {
            throw new RuntimeException("Exception at li.util.Reflect.getType(String)", e);
        }
    }

    /**
     * 构造一个新的实例,根据类型,参数类型列表和参数列表
     */
    public static <T> T born(Class<T> type, Class<?>[] argTypes, Object[] args) {
        try {
            return type.getConstructor(argTypes).newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Exception at li.util.Reflect.born(Class<T>, Class<?>[], Object[])", e);
        }
    }

    /**
     * 构造一个新的实例,根据类型和参数列表
     */
    public static <T> T born(Class<T> type, Object... args) {
        return born(type, typesOf(args), args);
    }

    /**
     * 构造一个新的实例,根据类型名,参数类型列表和参数列表
     */
    public static <T> T born(String type, Class<?>[] argTypes, Object[] args) {
        return (T) born(getType(type), argTypes, args);
    }

    /**
     * 构造一个新的实例,根据类型名和参数列表
     */
    public static <T> T born(String type, Object... args) {
        return (T) born(getType(type), typesOf(args), args);
    }

    /**
     * 得到一个方法,根据对象类型,方法名和参数类型列表
     */
    public static Method getMethod(Class<?> targetType, String methodName, Class<?>... argTypes) {
        try {
            Method method = targetType.getDeclaredMethod(methodName, argTypes);// 在当前类型中查找方法
            method.setAccessible(true);// 设置可见性为true
            return method;
        } catch (Exception e) {// 在超类型中查找方法
            return Object.class == targetType.getSuperclass() ? null : getMethod(targetType.getSuperclass(), methodName, argTypes);
        }
    }

    /**
     * 执行target的method方法,以args为参数
     */
    public static Object invoke(Object target, Method method, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException("method invoking Exception", e);
        }
    }

    /**
     * 执行target的methodName方法,args类型需要顺序匹配于argTypes
     */
    public static Object invoke(Object target, String methodName, Class<?>[] argTypes, Object[] args) {
        return invoke(target, getMethod(target.getClass(), methodName, argTypes), args);
    }

    /**
     * 执行target的methodName方法,args为参数列表,不可以为null
     * 
     * @param args 可以没有,表示方法无参数,不可以为null
     */
    public static Object invoke(Object target, String methodName, Object... args) {
        return invoke(target, getMethod(target.getClass(), methodName, typesOf(args)), args);
    }

    /**
     * 执行targetType类型的methodName静态方法,args类型需要顺序匹配于argTypes
     */
    public static Object call(String targetType, String methodName, Class<?>[] argTypes, Object[] args) {
        return invoke(null, getMethod(getType(targetType), methodName, argTypes), args);
    }

    /**
     * 执行targetType类型的methodName静态方法,args为参数列表,不可以为null
     */
    public static Object call(String targetType, String methodName, Object... args) {
        return invoke(null, getMethod(getType(targetType), methodName, typesOf(args)), args);
    }

    /**
     * 得到一个targetType的名为fieldName的属性,或者它的超类的
     */
    public static Field getField(Class<?> targetType, String fieldName) {
        try {
            Field field = targetType.getDeclaredField(fieldName);// 在当前类型中得到属性
            field.setAccessible(true);// 设置可操作性为true
            return field;
        } catch (Exception e) {// 如果当前类型中无这个属性,则从其超类中查找
            return Object.class == targetType.getSuperclass() ? null : getField(targetType.getSuperclass(), fieldName);
        }
    }

    /**
     * 探测一个属性的类型,从Setter或Field或Getter,有缓存不会多次执行
     */
    public static Class<?> fieldType(Class<?> targetType, String fieldName) {
        Class<?> fieldType = FIELD_TYPE_MAP.get(targetType + "#" + fieldName);
        if (null == fieldType) {
            String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method[] methods = targetType.getMethods();
            for (Method method : methods) {
                if (1 == method.getParameterTypes().length && method.getName().equals(setterName)) {
                    fieldType = method.getParameterTypes()[0];// 从setter探测
                }
            }
            Field field = getField(targetType, fieldName);
            if (null != field) { // 从field探测
                fieldType = field.getType();
            }
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getter = getMethod(targetType, getterName);
            if (null != getter) {// 从getter探测
                fieldType = getter.getReturnType();
            }
        }
        FIELD_TYPE_MAP.put(targetType + "#" + fieldName, fieldType);
        return fieldType;
    }

    /**
     * 使用getter方法返回属性值
     */
    public static Object getByGetter(Object target, String fieldName) {
        try {
            String method = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getter = getMethod(target.getClass(), method);
            return getter.invoke(target);// 使用Getter方法
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 访问字段以读取属性值
     */
    public static Object getByField(Object target, String fieldName) {
        try {
            return getField(target.getClass(), fieldName).get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回target的名为fieldName的属性的值,优先采用Getter方法,Field字段其次
     */
    public static Object get(Object target, String fieldName) {
        Integer getFlag = GETTER_SETTER_MAP.get(target.getClass() + "#" + fieldName + "#get");
        Object result = null;
        if (null == getFlag || getFlag.equals(0)) {// 第一次或出错
            try {
                result = getByGetter(target, fieldName);// 使用Getter方法
                GETTER_SETTER_MAP.put(target.getClass() + "#" + fieldName + "#get", 1);
            } catch (Exception e) {// 没有匹配的Getter方法
                try {
                    result = getByField(target, fieldName);// 通过属性访问
                    GETTER_SETTER_MAP.put(target.getClass() + "#" + fieldName + "#get", 2);
                } catch (Exception ex) {
                    if (target instanceof Map) {
                        result = ((Map) target).get(fieldName);// 通过Record.get()方法
                        GETTER_SETTER_MAP.put(target.getClass() + "#" + fieldName + "#get", 3);
                    } else {
                        GETTER_SETTER_MAP.put(target.getClass() + "#" + fieldName + "#get", 0);
                        throw new RuntimeException("Reflect.get() target=" + target + ",fieldName=" + fieldName);
                    }
                }
            }
        } else if (getFlag.equals(1)) {// 通过get方法
            result = getByGetter(target, fieldName);// 使用Getter方法
        } else if (getFlag.equals(2)) {// 通过Field
            result = getByField(target, fieldName);// 通过属性访问
        } else if (getFlag.equals(3)) {// 是Map
            result = ((Map) target).get(fieldName);// 通过Map.get()方法
        }
        return result;
    }

    /**
     * 访问setter方法给属性设值
     */
    public static void setBySetter(Object target, String fieldName, Object value) {
        try {
            String method = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method setter = getMethod(target.getClass(), method, fieldType(target.getClass(), fieldName));
            setter.invoke(target, value);// 使用Setter方法,这里没做类型转换
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 访问字段以设值
     */
    public static void setByField(Object target, String fieldName, Object value) {
        try {
            Field field = getField(target.getClass(), fieldName);
            field.set(target, Convert.toType(field.getType(), value));// 通过属性访问,这里有做类型转换
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置 target的名为fieldName的属性的值为 value,优先采用Setter方法,Field字段其次
     */
    public static void set(Object target, String fieldName, Object value) {
        Integer setFlag = GETTER_SETTER_MAP.get(target.getClass() + "#" + fieldName + "#set");
        if (null == setFlag || setFlag.equals(0)) {// 第一次或出错
            try {
                setBySetter(target, fieldName, value);// 使用Setter方法
                GETTER_SETTER_MAP.put(target.getClass() + "#" + fieldName + "#set", 1);
            } catch (Exception e) {// 没有匹配的Getter方法
                try {
                    setByField(target, fieldName, value);// 通过属性访问
                    GETTER_SETTER_MAP.put(target.getClass() + "#" + fieldName + "#set", 2);
                } catch (Exception ex) {
                    if (target instanceof Map) {
                        ((Map) target).put(fieldName, value);// 通过Map.get()方法
                        GETTER_SETTER_MAP.put(target.getClass() + "#" + fieldName + "#set", 3);
                    } else {
                        GETTER_SETTER_MAP.put(target.getClass() + "#" + fieldName + "#set", 0);
                        throw new RuntimeException("Reflect.get() target=" + target + ",fieldName=" + fieldName);
                    }
                }
            }
        } else if (setFlag.equals(1)) {// 通过get方法
            setBySetter(target, fieldName, value);// 使用Setter方法
        } else if (setFlag.equals(2)) {// 通过Field
            setByField(target, fieldName, value);// 通过属性访问
        } else if (setFlag.equals(3)) {// 是Map
            ((Map) target).put(fieldName, value);// 通过Record.set()方法,这里也没做类型转换
        }
    }

    /**
     * 返回一些对象的类型数组
     */
    public static Class<?>[] typesOf(Object... objects) {
        Class<?>[] types = new Class[objects.length];
        for (int i = 0; i < objects.length; i++) {
            types[i] = objects[i].getClass();
        }
        return types;
    }

    /**
     * 进行对象属性浅层复制
     * 
     * @param src 源对象
     * @param dest 目的对象
     * @return 返回目的对象
     */
    public static <T> T copy(Object src, T dest) {
        List<li.model.Field> fields = li.model.Field.list(dest.getClass(), false);
        for (li.model.Field attribute : fields) {// 迭代目标对象中的每一个属性
            Field field = getField(src.getClass(), attribute.name);
            if (null != field && !Modifier.isFinal(field.getModifiers())) {// 如果两个对象中均有此属性,且目标对象中的此属性可写
                set(dest, attribute.name, get(src, attribute.name));
            }
        }
        if (src instanceof Record<?> && dest instanceof Record<?>) {
            Set<Entry<?, ?>> set = ((Record<?>) src).entrySet();
            for (Entry<?, ?> entry : set) {
                ((Record<?>) dest).set(entry.getKey(), entry.getValue());
            }
        }
        return dest;
    }

    /**
     * 超类的第index个泛型参数在子类中的实际类型
     * 
     * @param subType 子类类型
     * @param index 泛型参数索引
     */
    public static Type actualType(Class<?> subType, Integer index) {
        try {
            return ((ParameterizedType) subType.getGenericSuperclass()).getActualTypeArguments()[index];
        } catch (Exception e) {// 不能转换为ParameterizedType或者数组越界的异常,探测他的超类
            return Object.class == subType.getSuperclass() ? null : actualType(subType.getSuperclass(), index);
        }
    }

    /**
     * 得到一个方法的参数注解列表
     */
    public static <T extends Annotation> T[] argAnnotations(Method method, Class<T> annotation) {
        T[] array = (T[]) Array.newInstance(annotation, method.getParameterTypes().length);// 生成注解数组
        Annotation[][] ats = method.getParameterAnnotations();// 所有参数注解的二维数组
        for (int i = 0; i < ats.length; i++) {// 每一个参数的注解数组
            for (Annotation at : ats[i]) {// 一个参数上的每一个注解
                if (annotation.isAssignableFrom(at.annotationType())) {// 如果注解类型是指定类型
                    array[i] = (T) at;
                    break;// 跳出当前层循环,处理下一个参数
                }
            }
        }
        return array;
    }

    /**
     * 获得传入的方法的形参名列表,有性能问题但此方法不会多次执行
     */
    public static String[] argNames(Method method) {
        return MethodParameterScaner.getParameterNames(method);
    }

    /**
     * 郑重声明:此类来自Nutz的Wendal,感谢Wendal的无私奉献！ 通过读取Class文件,获得方法形参名称列表
     * 
     * @author wendal(wendal1985@gmail.com)
     */
    private static class MethodParameterScaner {
        /**
         * 获取Method的形参名称列表
         * 
         * @param method 需要解析的方法
         * @return 形参名称列表,如果没有调试信息,将返回null
         */
        private static String[] getParameterNames(Method method) {
            try {
                int size = method.getParameterTypes().length;
                if (size == 0) {
                    return new String[0];
                }
                StringBuilder key = new StringBuilder();// getDescriptor
                key.append(method.getName()).append(',');
                key.append('(');
                Class<?>[] types = method.getParameterTypes();
                for (Class<?> klass : types) {
                    getDescriptor(key, klass);
                }
                key.append(')');
                getDescriptor(key, method.getReturnType());

                InputStream inputStream = method.getDeclaringClass().getResourceAsStream("/" + method.getDeclaringClass().getName().replace('.', '/') + ".class");

                List<String> list = getParamNameMap(inputStream).get(key.toString());// getParameterNames
                if (list != null && list.size() != size) {
                    return list.subList(0, size).toArray(new String[0]);
                }
                return list.toArray(new String[0]);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 传入Class文件流,返回类的所有方法参数的MapList
         */
        private static Map<String, List<String>> getParamNameMap(InputStream in) throws IOException {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(in));
            Map<String, List<String>> names = new HashMap<String, List<String>>();
            Map<Integer, String> strs = new HashMap<Integer, String>();
            dis.skipBytes(4);// Magic
            dis.skipBytes(2);// 副版本号
            dis.skipBytes(2);// 主版本号

            int constant_pool_count = dis.readUnsignedShort();// 读取常量池
            for (int i = 0; i < (constant_pool_count - 1); i++) {
                byte flag = dis.readByte();
                switch (flag) {
                case 7:// CONSTANT_Class:
                    dis.skipBytes(2);
                    break;
                case 9:// CONSTANT_Fieldref:
                case 10:// CONSTANT_Methodref:
                case 11:// CONSTANT_InterfaceMethodref:
                    dis.skipBytes(2);
                    dis.skipBytes(2);
                    break;
                case 8:// CONSTANT_String:
                    dis.skipBytes(2);
                    break;
                case 3:// CONSTANT_Integer:
                case 4:// CONSTANT_Float:
                    dis.skipBytes(4);
                    break;
                case 5:// CONSTANT_Long:
                case 6:// CONSTANT_Double:
                    dis.skipBytes(8);
                    i++;// 必须跳过一个,这是class文件设计的一个缺陷,历史遗留问题
                    break;
                case 12:// CONSTANT_NameAndType:
                    dis.skipBytes(2);
                    dis.skipBytes(2);
                    break;
                case 1:// CONSTANT_Utf8:
                    int len = dis.readUnsignedShort();
                    byte[] data = new byte[len];
                    dis.read(data);
                    strs.put(i + 1, new String(data, "UTF-8"));// 必然是UTF8的
                    break;
                case 15:// CONSTANT_MethodHandle:
                    dis.skipBytes(1);
                    dis.skipBytes(2);
                    break;
                case 16:// CONSTANT_MethodType:
                    dis.skipBytes(2);
                    break;
                case 18:// CONSTANT_InvokeDynamic:
                    dis.skipBytes(2);
                    dis.skipBytes(2);
                    break;
                default:
                    throw new RuntimeException("Impossible!! flag=" + flag);
                }
            }

            dis.skipBytes(2);// 版本控制符
            dis.skipBytes(2);// 类名
            dis.skipBytes(2);// 超类

            // 跳过接口定义
            int interfaces_count = dis.readUnsignedShort();
            dis.skipBytes(2 * interfaces_count);// 每个接口数据,是2个字节

            // 跳过字段定义
            int fields_count = dis.readUnsignedShort();
            for (int i = 0; i < fields_count; i++) {
                dis.skipBytes(2);
                dis.skipBytes(2);
                dis.skipBytes(2);
                int attributes_count = dis.readUnsignedShort();
                for (int j = 0; j < attributes_count; j++) {
                    dis.skipBytes(2);// 跳过访问控制符
                    int attribute_length = dis.readInt();
                    dis.skipBytes(attribute_length);
                }
            }

            // 开始读取方法
            int methods_count = dis.readUnsignedShort();
            for (int i = 0; i < methods_count; i++) {
                dis.skipBytes(2); // 跳过访问控制符
                String methodName = strs.get(dis.readUnsignedShort());
                String descriptor = strs.get(dis.readUnsignedShort());
                short attributes_count = dis.readShort();
                for (int j = 0; j < attributes_count; j++) {
                    String attrName = strs.get(dis.readUnsignedShort());
                    int attribute_length = dis.readInt();
                    if ("Code".equals(attrName)) { // 形参只在Code属性中
                        dis.skipBytes(2);
                        dis.skipBytes(2);
                        int code_len = dis.readInt();
                        dis.skipBytes(code_len); // 跳过具体代码
                        int exception_table_length = dis.readUnsignedShort();
                        dis.skipBytes(8 * exception_table_length); // 跳过异常表

                        int code_attributes_count = dis.readUnsignedShort();
                        for (int k = 0; k < code_attributes_count; k++) {
                            int str_index = dis.readUnsignedShort();
                            String codeAttrName = strs.get(str_index);
                            int code_attribute_length = dis.readInt();
                            if ("LocalVariableTable".equals(codeAttrName)) {// 形参在LocalVariableTable属性中
                                int local_variable_table_length = dis.readUnsignedShort();
                                List<String> varNames = new ArrayList<String>(local_variable_table_length);
                                for (int l = 0; l < local_variable_table_length; l++) {
                                    dis.skipBytes(2);
                                    dis.skipBytes(2);
                                    String varName = strs.get(dis.readUnsignedShort());
                                    dis.skipBytes(2);
                                    dis.skipBytes(2);
                                    if (!"this".equals(varName)) // 非静态方法,第一个参数是this
                                        varNames.add(varName);
                                }
                                names.put(methodName + "," + descriptor, varNames);
                            } else
                                dis.skipBytes(code_attribute_length);
                        }
                    } else
                        dis.skipBytes(attribute_length);
                }
            }
            dis.close();
            return names;
        }

        /** 本方法来源于ow2的asm库的Type类 */
        private static void getDescriptor(final StringBuilder buf, final Class<?> c) {
            Class<?> d = c;
            while (true) {
                if (d.isPrimitive()) {
                    char car;
                    if (d == Integer.TYPE) {
                        car = 'I';
                    } else if (d == Void.TYPE) {
                        car = 'V';
                    } else if (d == Boolean.TYPE) {
                        car = 'Z';
                    } else if (d == Byte.TYPE) {
                        car = 'B';
                    } else if (d == Character.TYPE) {
                        car = 'C';
                    } else if (d == Short.TYPE) {
                        car = 'S';
                    } else if (d == Double.TYPE) {
                        car = 'D';
                    } else if (d == Float.TYPE) {
                        car = 'F';
                    } else /* if (d == Long.TYPE) */{
                        car = 'J';
                    }
                    buf.append(car);
                    return;
                } else if (d.isArray()) {
                    buf.append('[');
                    d = d.getComponentType();
                } else {
                    buf.append('L');
                    String name = d.getName();
                    int len = name.length();
                    for (int i = 0; i < len; ++i) {
                        char car = name.charAt(i);
                        buf.append(car == '.' ? '/' : car);
                    }
                    buf.append(';');
                    return;
                }
            }
        }
    }
}