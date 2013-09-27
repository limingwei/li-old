package li.generator.store;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import li.generator.resource.FileResource;
import li.generator.resource.Resource;
import li.util.Files;

public class FileStore extends Store {
    private File rootDir;

    public FileStore setDir(String dir) {
        this.rootDir = new File(dir);
        return this;
    }

    public List<Resource> getResources() {
        List<Resource> resources = new ArrayList<Resource>();
        List<String> files = Files.list(rootDir, "^.*\\.ftl$", true, 1);
        for (String file : files) {
            resources.add(new FileResource().setFile(new File(file)));
        }
        return resources;
    }
}