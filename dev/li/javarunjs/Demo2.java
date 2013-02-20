package li.javarunjs;

import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Demo2 {
    public static void main(String[] args) throws Exception {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");
        scriptEngine.eval(new FileReader("D:\\workspace\\li\\dev\\li\\javarunjs\\func.js"));

        Object result = ((Invocable) scriptEngine).invokeFunction("sum", 1, 2);

        System.out.println(result);

        System.out.println(((Invocable) scriptEngine).invokeFunction("hello", "黎明伟"));
    }
}
