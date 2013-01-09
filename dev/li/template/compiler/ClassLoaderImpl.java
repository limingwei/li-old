package li.template.compiler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.tools.JavaFileObject;

/**
 * @author li
 */
public final class ClassLoaderImpl extends ClassLoader {

    private final Map<String, JavaFileObject> classes = new HashMap<String, JavaFileObject>();

    ClassLoaderImpl(final ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    Collection<JavaFileObject> files() {
        return Collections.unmodifiableCollection(classes.values());
    }

    @Override
    protected Class<?> findClass(final String qualifiedClassName) throws ClassNotFoundException {
        JavaFileObject file = classes.get(qualifiedClassName);
        if (file != null) {
            byte[] bytes = ((JavaFileObjectImpl) file).getByteCode();
            return defineClass(qualifiedClassName, bytes, 0, bytes.length);
        }
        try {
            return Class.forName(qualifiedClassName);
        } catch (ClassNotFoundException nf) {
            return super.findClass(qualifiedClassName);
        }
    }

    void add(final String qualifiedClassName, final JavaFileObject javaFile) {
        classes.put(qualifiedClassName, javaFile);
    }
}