package li.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import li.util.Log;

public class MonitoringDataSource extends SimpleDataSource {
    private static final Log log = Log.init();

    private int times = 0;
    private List<Connection> connections = new ArrayList<Connection>();

    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = new MonitoringConnection(this, super.getConnection(username, password));
        connections.add(connection);
        times++;
        log.debug("正在获取一个链接, 共获取链接" + times + "次, 还有未关闭的链接" + connections.size() + "个");
        String conns = "";
        for (Connection con : connections) {
            conns += con + "\t";
        }
        if (!conns.isEmpty()) {
            log.trace(conns);
        }
        return connection;
    }

    public void removeConnection(Connection connection) {
        connections.remove(connection);
        log.debug("正在关闭一个链接, 共获取链接" + times + "次, 还有未关闭的链接" + connections.size() + "个");
        String conns = "";
        for (Connection con : connections) {
            conns += con + "\t";
        }
        if (!conns.isEmpty()) {
            log.trace(conns);
        }
    }
}