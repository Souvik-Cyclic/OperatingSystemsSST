import java.util.concurrent.*;

public class locks {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Value<Integer> value = new Value<>(0);
        Adder adder = new Adder(value);
        Subtractor subtractor = new Subtractor(value);
        Future<Void> future1 = executor.submit(adder);
        Future<Void> future2 = executor.submit(subtractor);
        future1.get();
        future2.get();
        executor.shutdown();

        // Print the value after both tasks are complete
        System.out.println("Final value: " + value.getValue());
    }
}

class Value<T> {
    private T value;
    
    public Value(T value) {
        this.value = value;
    }
    
    public synchronized T getValue() {
        return value;
    }
    
    public synchronized void setValue(T value) {
        this.value = value;
    }
}

class Adder implements Callable<Void> {
    Value<Integer> value;
    
    public Adder(Value<Integer> value) {
        this.value = value;
    }
    
    @Override
    public Void call() throws Exception {
        synchronized(value) {
            for (int i = 0; i < 10000; i++) {
                value.setValue(value.getValue() + 1);
            }
        }
        return null;
    }
}

class Subtractor implements Callable<Void> {
    Value<Integer> value;
    
    public Subtractor(Value<Integer> value) {
        this.value = value;
    }
    
    @Override
    public Void call() throws Exception {
        synchronized(value) {
            for (int i = 0; i < 10000; i++) {
                value.setValue(value.getValue() - 1);
            }
        }
        return null;
    }
}

