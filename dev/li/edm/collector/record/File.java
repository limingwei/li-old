package li.edm.collector.record;

import li.annotation.Bean;
import li.annotation.Table;
import li.dao.Record;

@Bean
@Table("t_file")
public class File extends Record<File> {
    private static final long serialVersionUID = 4785969853386722774L;
}