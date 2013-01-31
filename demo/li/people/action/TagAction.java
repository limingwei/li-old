package li.people.action;

import li.annotation.Bean;
import li.annotation.Inject;
import li.mvc.AbstractAction;
import li.people.record.Tag;

@Bean
public class TagAction extends AbstractAction {
    @Inject
    Tag tagDao;
}