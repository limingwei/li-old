package li.people.action;

import li.annotation.At;
import li.annotation.Bean;
import li.annotation.Inject;
import li.dao.Page;
import li.mvc.AbstractAction;
import li.people.Const;
import li.people.record.Bill;

@Bean
public class BillAction extends AbstractAction implements Const {
    @Inject
    Bill billDao;

    @At("bill_list.do")
    public void list(Page page) {
        setRequest(LIST, billDao.list(page));
        setRequest(PAGE, page);
        view("bill/list");
    }

    @At("bill_add.do")
    public void add() {
        view("bill/add");
    }

    @At(value = "bill_save.do", method = POST)
    public void save(Bill bill) {
        write(billDao.saveIgnoreNull(bill) ? "添加账单成功" : "添加账单失败");
    }

    @At("bill_edit.do")
    public void edit(Integer id) {
        setRequest("bill", billDao.find(id));
        view("bill/edit");
    }

    @At(value = "bill_update.do", method = POST)
    public void update(Bill bill) {
        write(billDao.updateIgnoreNull(bill) ? "修改账单成功" : "修改账单失败");
    }

    @At(value = "bill_delete.do", method = POST)
    public void delete(Integer id) {
        write(billDao.delete(id) ? "删除账单成功" : "删除账单失败");
    }
}