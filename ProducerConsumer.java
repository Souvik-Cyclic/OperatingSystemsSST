import java.util.concurrent.*;

public class ProducerConsumer {
    public static void main(String[] args) throws Exception {
        Store store = new Store(new Value<>(3));
        Producer producer = new Producer(store, 100);
        Consumer consumer = new Consumer(store, 3);
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

class Value<T> {
    private T value;
    
    public Value(T value) {
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
    
    public void setValue(T value) {
        this.value = value;
    }
}

class Store {
    private final Value<Integer> value;

    public Store(Value<Integer> value) {
        this.value = value;
    }

    public synchronized int getValue() {
        return value.getValue();
    }

    public synchronized void increment() {
        value.setValue(value.getValue() + 1);
    }

    public synchronized void decrement() {
        value.setValue(value.getValue() - 1);
    }
}

class Producer implements Runnable {
    private final Store store;
    private final int max;
    private volatile boolean running = true;

    public Producer(Store store, int max) {
        this.store = store;
        this.max = max;
    }

    @Override
    public void run() {
        try {
            while (running) {
                synchronized (store) {
                    if (store.getValue() < max) {
                        store.increment();
                        System.out.println("Produced: " + store.getValue());
                    }
                }
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
    private final int min;
    private volatile boolean running = true;

    public Consumer(Store store, int min) {
        this.store = store;
        this.min = min;
    }

    @Override
    public void run() {
        try {
            while (running) {
                synchronized (store) {
                    if (store.getValue() > 0) {
                        store.decrement();
                        System.out.println("Consumed: " + store.getValue());
                    }
                }
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
