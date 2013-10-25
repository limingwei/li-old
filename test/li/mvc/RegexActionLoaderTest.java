package li.mvc;

import java.util.Map;
import java.util.Map.Entry;

import li.model.Action;
import li.util.Reflect;

import org.junit.Test;

public class RegexActionLoaderTest {
    @Test
    public void getActions() {
        ActionLoader actionLoader = new RegexActionLoader();
        Reflect.set(actionLoader, "typeRegex", "li.people.action.*");
        Reflect.set(actionLoader, "methodRegex", ".*(list|add|save|edit|update|delete).*");
        Map<String, Action> actions = actionLoader.getActions();
        for (Entry<String, Action> entry : actions.entrySet()) {
            System.err.println(entry.getKey() + "\t" + entry.getValue().actionMethod);
        }
    }
}