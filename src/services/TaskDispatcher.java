package services;

import java.util.*;

public class TaskDispatcher {

    private Timer timer;
    private TimerTask task;

    public TaskDispatcher(Runnable task, Long interval) {

        this.task = new TimerTask() {

            @Override
            public void run() {
                task.run();
            }
        };
        this.timer = new Timer();
        timer.schedule(this.task, interval);
    }

    public void clearTask() {
        task.cancel();
    }
}
