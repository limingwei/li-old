package li.people.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_app")
public class App extends Record<App, Integer> {
    private static final long serialVersionUID = -1929804842749760L;
}