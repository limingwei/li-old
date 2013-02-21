package li.edm.sender;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public abstract class TimeOut {
    public TimeOut(Integer timeout) {
        final FutureTask futureTask = new FutureTask(new Callable() {
            public Object call() throws Exception {
                try {
                    run();
                    return "";
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        });

        new Thread() {
            public void run() {
                futureTask.run();
            };
        }.start();

        try {
            futureTask.get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            futureTask.cancel(true);
            throw new RuntimeException(e);
        }
    }

    public abstract void run() throws Throwable;
}