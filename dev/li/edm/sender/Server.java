package li.edm.sender;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_server")
public class Server extends Record<Server> {
    private static final long serialVersionUID = -3628705397989705029L;
}