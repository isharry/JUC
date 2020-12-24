package com.piperplatform.juc.c_026_01_ThreadPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class T06_04_AliQuestionAboutCF {
    enum Result {
        SUCCESS, FAIL, CANCELLED
    }

    static List<MyTask> tasks = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        tasks.add(new MyTask("Task1", 3, Result.SUCCESS));
        tasks.add(new MyTask("Task2", 2, Result.SUCCESS));
        tasks.add(new MyTask("Task3", 4, Result.FAIL));
        tasks.add(new MyTask("Task4", 4, Result.FAIL));
        tasks.add(new MyTask("Task5", 4, Result.FAIL));
        tasks.add(new MyTask("Task6", 5, Result.FAIL));
        tasks.add(new MyTask("Task7", 6, Result.FAIL));

        for (MyTask task : tasks) {
            CompletableFuture.supplyAsync(() -> task.call()).thenAccept(result -> callback(result, task));
        }

        System.in.read();
    }

    private static void callback(Result result, MyTask task) {
        if (Result.FAIL == result) {
            for (MyTask _task : tasks) {
                if (_task != task){
                    _task.cancel();
                }
            }
        }
    }

    private static class MyTask {
        private String name;
        private int timeInSeconds;
        private Result ret;
        boolean cancelling = false;
//        volatile boolean cancelled = false;
        boolean cancelled = false;

        public MyTask(String name, int timeInSeconds, Result ret) {
            this.name = name;
            this.timeInSeconds = timeInSeconds * 1000;
            this.ret = ret;
        }

        public Result call() {
            int interval = 100;
            int total = 0;

            try {
                for (; ; ) {
                    Thread.sleep(interval);
                    total += interval;
                    if (total >= timeInSeconds) break;
                    if (cancelled) return Result.CANCELLED;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + " task end.");
            return ret;
        }

        public void cancel() {
            cancelling = true;
//            synchronized (this) {
//                System.out.println(name + " cancelling.");
//                try {
//                    TimeUnit.MILLISECONDS.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(name + " cancelled.");
//            }
            System.out.println(name + " cancelling.");
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + " cancelled.");
            cancelled = true;
        }
    }
}
