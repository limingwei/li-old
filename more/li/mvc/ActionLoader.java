package li.mvc;

import java.util.Map;

import li.model.Action;

public interface ActionLoader {
    public Map<String, Action> getActions();
}