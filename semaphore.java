import java.util.concurrent.*;

public class semaphore {
    public static void main(String[] args) throws Exception {
        Store store = new Store(0);
        Semaphore items = new Semaphore(0);
        Semaphore spaces = new Semaphore(100);
        Producer producer = new Producer(store, items, spaces);
        Consumer consumer = new Consumer(store, items, spaces);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(producer);
        executor.submit(consumer);
        Thread.sleep(5000);
        producer.stop();
        consumer.stop();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Final value: " + store.getValue());
    }
}

class Store {
    private int value;

    public Store(int initialValue) {
        this.value = initialValue;
    }

    public int getValue() {
        return value;
    }

    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }
}

class Producer implements Runnable {
    private final Store store;
    private final Semaphore items;
    private final Semaphore spaces;
    private volatile boolean running = true;

    public Producer(Store store, Semaphore items, Semaphore spaces) {
        this.store = store;
        this.items = items;
        this.spaces = spaces;
    }

    @Override
    public void run() {
        try {
            while (running) {
                spaces.acquire();
                synchronized (store) {
                    store.increment();
                    System.out.println("Produced: " + store.getValue());
                }
                items.release();
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        running = false;
    }
}

class Consumer implements Runnable {
    private final Store store;
    private final Semaphore items;
    private final Semaphore spaces;
    private volatile boolean running = true;

    public Consumer(Store store, Semaphore items, Semaphore spaces) {
        this.store = store;
        this.items = items;
        this.spaces = spaces;
    }

    @Override
    public void run() {
        try {
            while (running) {
                items.acquire();
                synchronized (store) {
                    store.decrement();
                    System.out.println("Consumed: " + store.getValue());
                }
                spaces.release();
                Thread.sleep(15);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        running = false;
    }
}
