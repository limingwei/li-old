package li.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import li.util.Log;

/**
 * @author : 明伟 
 * @date : 2013年7月22日 下午3:06:36
 * @version 1.0
 */
public class DataSourceWrapper extends SimpleDataSource {
    private static final Log log = Log.init();

    private List<Connection> connections = new ArrayList<Connection>();

    private int times = 0;

    private DataSource dataSource;

    public DataSourceWrapper() {}

    public DataSourceWrapper(String url, String username, String password) {
        super(url, username, password);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        if (null == this.dataSource) {
            this.dataSource = new SimpleDataSource(url, username, password);
        }
        return this.dataSource;
    }

    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = new ConnectionWrapper(this, this.getDataSource().getConnection(username, password));
        connections.add(connection);
        log.debug("Retrieving a link, total got " + (++times) + " links , there are " + connections.size() + " unclosed links");
        traceConnections();
        return connection;
    }

    public void removeConnection(Connection connection) {
        connections.remove(connection);
        log.debug("Closing a link, total got " + (++times) + " links , there are " + connections.size() + " unclosed links");
        traceConnections();
    }

    private void traceConnections() {
        String conns = "";
        for (Connection con : connections) {
            conns += con + "\t";
        }
        if (!conns.isEmpty()) {
            log.trace(conns);
        }
    }
}