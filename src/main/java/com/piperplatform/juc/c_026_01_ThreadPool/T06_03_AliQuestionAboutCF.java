package com.piperplatform.juc.c_026_01_ThreadPool;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class T06_03_AliQuestionAboutCF {
    public static void main(String[] args) throws IOException {
        MyTask task1 = new MyTask("Task1", 3, true);
        MyTask task2 = new MyTask("Task2", 4, true);
        MyTask task3 = new MyTask("Task3", 1, false);

        CompletableFuture.supplyAsync(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return task1.call();
            }
        }).thenAccept(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean result) {
                callback(result);
            }
        });
        CompletableFuture.supplyAsync(() -> task2.call()).thenAccept(result -> callback(result));
        CompletableFuture.supplyAsync(() -> task3.call()).thenAccept(result -> callback(result));

        System.in.read();
    }

    private static void callback(Boolean result) {
        if (!result){
            // 处理结束流程
            // 通知其他线程结束
            // 超时处理
            System.exit(0);
        }
    }

    private static class MyTask{
        private String name;
        private int timeInSeconds;
        private boolean ret;

        public MyTask(String name, int timeInSeconds, boolean ret) {
            this.name = name;
            this.timeInSeconds = timeInSeconds;
            this.ret = ret;
        }

        public Boolean call(){
            try{
                Thread.sleep(timeInSeconds*1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(name+" task callback");
            return ret;
        }
    }
}
