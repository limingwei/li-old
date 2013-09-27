package li.generator;

import java.util.List;

import li.generator.entity.Entity;
import li.generator.resource.Resource;
import li.generator.store.FileStore;
import li.generator.store.Store;
import li.generator.template.Template;

public class Gen {
    public static void main(String[] args) {
        Store store = new FileStore().setDir("D:\\workspace\\li_github\\more\\li\\generator\\template\\li_ar");
        List<Resource> resources = store.getResources();

        List<Entity> entities = Db.getEntities("jdbc:mysql://127.0.0.1/li?characterEncoding=UTF-8", "root", "");

        for (Entity entity : entities) {
            for (Resource resource : resources) {
                Template template = new Template().setResource(resource).setValues(entity.toMap());
                System.err.println(template.make());
            }
        }
    }
}

// setOutDir("C:\\Users\\明伟\\Desktop\\out")