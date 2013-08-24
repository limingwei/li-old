package li.ioc;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import li.util.Files;

public class ClassInJar {
    public static void main(String[] args) throws Exception {
        String libDir = "D:/workspace/li_github/WebContent" + File.separator + "WEB-INF" + File.separator + "lib";

        System.out.println(libDir);

        List<String> jars = Files.list(new File(libDir), "^.*\\.jar$", true, 1);
        for (String jar : jars) {
            JarFile jarFile = new JarFile(jar);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                if (!jarEntry.isDirectory()) {
                    String entryName = jarEntry.getName();
                    if (entryName.endsWith(".class")) {
                        System.out.println(entryName);
                    }
                }
            }
        }
    }

    private static String getWebRootPath() {
        try {
            String path = ClassInJar.class.getResource("/").toURI().getPath();
            return new File(path).getParentFile().getParentFile().getCanonicalPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
