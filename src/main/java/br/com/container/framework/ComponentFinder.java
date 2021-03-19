package br.com.container.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComponentFinder {

    public static List<Class> getClasses(Class rootClass) throws IOException, ClassNotFoundException {
        var classLoader = rootClass.getClassLoader();
        var path = rootClass.getPackageName().replace('.', '/');

        var resources = classLoader.getResources(path);

        var dirs = new ArrayList<File>();

        while (resources.hasMoreElements()) {
            var resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        var classes = new ArrayList<Class>();

        for (File directory : dirs) {
            classes.addAll(findClasses(directory, rootClass.getPackageName()));
        }

        return classes;
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        var classes = new ArrayList<Class>();

        if (!directory.exists()) {
            return classes;
        }

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(
                        findClasses(file, packageName + "." + file.getName())
                );
            } else if (file.getName().endsWith(".class")) {
                classes.add(
                        Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6))
                );
            }
        }

        return classes;
    }
}
