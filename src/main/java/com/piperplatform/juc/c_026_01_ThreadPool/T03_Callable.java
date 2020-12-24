package com.piperplatform.juc.c_026_01_ThreadPool;

import java.util.concurrent.*;

public class T03_Callable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<String> c = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Hello Callable";
            }
        };
        ExecutorService service = Executors.newCachedThreadPool();
        Future<String> future = service.submit(c);//异步

        System.out.println(future.get());//阻塞
        service.shutdown();
    }
}
