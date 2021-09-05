package geektime.hw.week4;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainConcurrent {

    public static void main(String[] args) throws Exception {
        asyncExtendThread();
        asyncRunnable();
        asyncThreadPool(null);
        asyncCountDownLatch();
    }

    private static void asyncCountDownLatch() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        asyncThreadPool(countDownLatch);
    }

    private static void asyncThreadPool(CountDownLatch countDownLatch) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try {
            Future<Integer> taskFuture = executorService.submit(() -> {
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
                return sum();
            });
            while (!taskFuture.isDone()) {
                Thread.sleep(200);
            }
            System.out.printf("线程池(%s)异步计算结果：" + taskFuture.get() + "%n", countDownLatch != null ? "包含CountDownLatch" : "不包含CountDownLatch");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    // Thread 线程
    private static void asyncExtendThread() {
        int result;
        long start = System.currentTimeMillis();
        SumTaskThread sumTaskThread = new SumTaskThread();
        sumTaskThread.start();
        while (true) {
            if (sumTaskThread.isAlive()) {
                continue;
            }
            result = sumTaskThread.getSumResult();
            break;
        }
        long end = System.currentTimeMillis();
        System.out.printf("继承Thread异步计算结果(%dms)：%d%n", end - start, result);
    }

    // Runnable 接口
    private static void asyncRunnable() {
        long start = System.currentTimeMillis();
        SumTask sumTask = new SumTask();
        Thread thread = new Thread(sumTask);
        thread.start();

        int result;
        while (true) {
            if (thread.isAlive()) {
                continue;
            }
            result = sumTask.getSumResult();
            break;
        }
        long end = System.currentTimeMillis();
        System.out.printf("Runnable接口异步计算结果(%dms)：%d%n", end - start, result);
    }

    static class SumTaskThread extends Thread {
        private int sumResult = 0;

        @Override
        public void run() {
            sumResult = sum();
        }

        public int getSumResult() {
            return sumResult;
        }
    }

    static class SumTask implements Runnable {
        private int sumResult = 0;


        @Override
        public void run() {
            sumResult = sum();
        }

        public int getSumResult() {
            return sumResult;
        }
    }

    //  base codes...
    private static int sum() {
        return fibonacci(36);
    }

    private static int fibonacci(int a) {
        if (a < 2)
            return 1;
        return fibonacci(a - 1) + fibonacci(a - 2);
    }
}
