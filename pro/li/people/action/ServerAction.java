package li.people.action;

import li.annotation.Aop;
import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.AuthFilter;
import li.people.Const;
import li.people.record.Server;

@Bean
public class ServerAction extends AbstractAction implements Const {
    @Inject
    Server serverDao;

    @At("server_list.do")
    @Aop(AuthFilter.class)
    public void list(Page page) {
        setRequest(LIST, serverDao.list(page));
        setRequest(PAGE, page);
        view("server/list");
    }

    @At("server_add.do")
    public void add() {
        view("server/add");
    }

    @At(value = "server_save.do", method = POST)
    public void save(Server server) {
        write(serverDao.saveIgnoreNull(server) ? "添加服务器成功" : "添加服务器失败");
    }

    @At("server_edit.do")
    public void edit(Integer id) {
        setRequest("server", serverDao.find(id));
        view("server/edit");
    }

    @At(value = "server_update.do", method = POST)
    public void update(Server server) {
        write(serverDao.updateIgnoreNull(server) ? "修改服务器成功" : "修改服务器失败");
    }

    @At(value = "server_delete.do", method = POST)
    public void delete(Integer id) {
        write(serverDao.delete(id) ? "删除服务器成功" : "删除服务器失败");
    }
}