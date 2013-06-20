package li.demo.record;

import li.annotation.Inject;
import li.people.record.Role;
import li.test.BaseTest;

import org.junit.Test;

public class RoleTest extends BaseTest {
    @Inject
    Role roleDao;

    @Test
    public void list() {
        System.out.println(roleDao.list(page));
    }

    @Test
    public void saveIgnoreNull() {
        for (int i = 0; i < 5; i++) {
            Role role = new Role().set("name", "role_name_" + i + "_" + System.currentTimeMillis());
            System.out.println(roleDao.saveIgnoreNull(role));
        }
    }
}