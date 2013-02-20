package li.javarunjs;

import java.io.FileReader;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Demo {
    public static void main(String[] args) throws Exception {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");
        scriptEngine.eval(new FileReader(System.getProperty("user.dir") + "\\dev\\li\\javarunjs\\func.js"));

        System.out.println(((Invocable) scriptEngine).invokeFunction("sum", 1, 2));

        System.out.println(((Invocable) scriptEngine).invokeFunction("hello", "黎明伟"));

        ((Invocable) scriptEngine).invokeFunction("test");
    }

    public static void main2(String[] args) throws Exception {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");

        // 1
        scriptEngine.put("msg", "just a test");
        String str = "msg += '!!!';var user = {name:'tom',age:23,hobbies:['football','basketball']}; var name = user.name; var hb = user.hobbies[1];";
        scriptEngine.eval(str);

        String msg = (String) scriptEngine.get("msg");
        String name = (String) scriptEngine.get("name");
        String hb = (String) scriptEngine.get("hb");
        System.out.println(msg);
        System.out.println(name + ":" + hb);

        // 2
        scriptEngine.eval("function add (a, b) {c = a + b; return c; }");
        Invocable jsInvoke = (Invocable) scriptEngine;

        Object result1 = jsInvoke.invokeFunction("add", new Object[] { 10, 5 });
        System.out.println(result1);

        // 3
        Adder adder = jsInvoke.getInterface(Adder.class);
        int result2 = adder.add(10, 35);
        System.out.println(result2);

        // 4
        scriptEngine.eval("function run() {print('www.java2s.com');}");
        Invocable invokeEngine = (Invocable) scriptEngine;
        Runnable runner = invokeEngine.getInterface(Runnable.class);
        Thread t = new Thread(runner);
        t.start();
        t.join();

        // 5
        String jsCode = "importPackage(java.util);var list2 = Arrays.asList(['A', 'B', 'C']); ";
        scriptEngine.eval(jsCode);
        List<String> list2 = (List<String>) scriptEngine.get("list2");
        for (String val : list2) {
            System.out.println(val);
        }

    }
}

interface Adder {
    int add(int a, int b);
}
