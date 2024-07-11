import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
// import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// import java.util.concurrent.Future;
import java.util.concurrent.Future;
// Print numbers from 1 to 10 using a different thread.
// class NumberPrinter extends Thread{
//     @Override
//     public void run(){
//         for(int i=1; i<=10; i++){
//             try{
//                 Thread.sleep(1000);
//             }
//             catch(InterruptedException ex){
//             }
//             System.out.println(i + " " + Thread.currentThread().getName());
//         }
//     }
// }

// Print numbers from 1 to 100 each with a different thread.
// class SingleNumberPrinter extends Thread {
//     public int number;

//     public SingleNumberPrinter(int number) {
//         this.number = number;
//     }

//     @Override
//     public void run() {
//         System.out.println(number + " " + Thread.currentThread().getName());
//     }
// }

// class HelloWorldPrinter extends Thread{
//     @Override
//     public void run(){
//         System.out.println("Hello World " + Thread.currentThread().getName());
//     }
// }

// Create a student class with run method.
// class Student extends Thread{
//     @Override
//     public void run(){

//     }
// }

//Write code to return a list having 2x of the elements of the original list, returned by a new thread
// class ListMultiplier implements Runnable {
//     public List<Integer> list;
//     public List<Integer> result;

//     public ListMultiplier(ArrayList<Integer> list, ArrayList<Integer> result) {
//         this.list = list;
//         this.result = result;
//     }

//     @Override
//     public void run() {
//         System.out.println(Thread.currentThread().getName());
//         for (int i=0; i<list.size(); i++) {
//             result.add(list.get(i) * 2);
//         }
//     }
// }

// class Sorter implements Callable<ArrayList<Integer>>{
//     ArrayList<Integer> list;

//     public Sorter(ArrayList<Integer> list){
//         this.list = list;
//     }

//     @Override
//     public ArrayList<Integer> call() throws InterruptedException, ExecutionException{
//         if (list.size() <= 1) return list;
//         int mid = list.size() / 2;
//         ArrayList<Integer> left = getSubArray(list, 0, mid);
//         ArrayList<Integer> right = getSubArray(list, mid, list.size());

//         //Executor service to sort the two halfs in parallel.
//         ExecutorService es = Executors.newCachedThreadPool();

//         Sorter leftSorter = new Sorter(left);
//         Sorter rightSorter = new Sorter(right);

//         Future<ArrayList<Integer>> leftSortedFuture = es.submit(leftSorter);
//         Future<ArrayList<Integer>> rightSortedFuture = es.submit(rightSorter);

//         ArrayList<Integer> leftSortedList = leftSortedFuture.get();
//         ArrayList<Integer> rightSortedList = rightSortedFuture.get();

//         return merge(leftSortedList, rightSortedList);
//     }

//     public ArrayList<Integer> merge(ArrayList<Integer> A, ArrayList<Integer> B){
//         ArrayList<Integer> mergedList = new ArrayList<>();
//         int i=0, j=0;
//         while (i < A.size() && j < B.size()) {
//             if (A.get(i) <= B.get(j)) {
//                 mergedList.add(A.get(i));
//                 i++;
//             } else {
//                 mergedList.add(B.get(j));
//                 j++;
//             }
//         }
//         while (i < A.size()) {
//             mergedList.add(A.get(i));
//             i++;
//         }
//         while (j < B.size()) {
//             mergedList.add(B.get(j));
//             j++;
//         }
//         return mergedList;
//     }

//     public ArrayList<Integer> getSubArray(ArrayList<Integer> list, int s, int e){
//         ArrayList<Integer> sublist = new ArrayList<>();
//         for(int i=s; i<e; i++){
//             sublist.add(list.get(i));
//         }
//         return sublist;
//     }
// }
class Value {
    private int val;

    public Value(int val) {
        this.val = val;
    }

    public synchronized void add(int amount) {
        val += amount;
    }

    public synchronized void subtract(int amount) {
        val -= amount;
    }

    public int getVal() {
        return val;
    }
}

class Adder implements Callable<Void> {
    Value val;

    public Adder(Value val) {
        this.val = val;
    }

    @Override
    public Void call() throws Exception {
        for (int i = 1; i <= 10; i++) {
            val.add(1);
            System.out.println("Adder: " + val.getVal() + " " + Thread.currentThread().getName());
            try {
                Thread.sleep(100); // Sleep to simulate some work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }
}

class Subtractor implements Callable<Void> {
    Value val;

    public Subtractor(Value val) {
        this.val = val;
    }

    @Override
    public Void call() throws Exception {
        for (int i = 0; i < 10; i++) {
            val.subtract(1);
            System.out.println("Subtractor: " + val.getVal() + " " + Thread.currentThread().getName());
            try {
                Thread.sleep(100); // Sleep to simulate some work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }
}

public class Main {
    // public static void main(String[] args) throws InterruptedException,
    // ExecutionException {
    // // System.out.println("Hello World " + Thread.currentThread().getName());
    // // HelloWorldPrinter hwp = new HelloWorldPrinter();
    // // hwp.start();
    // // HelloWorldPrinter hwp2 = new HelloWorldPrinter();
    // // hwp2.start();

    // // NumberPrinter np = new NumberPrinter();
    // // np.start();
    // // NumberPrinter np2 = new NumberPrinter();
    // // np2.start();

    // // for (int i = 1; i <= 100; i++) {
    // // try{
    // // Thread.sleep(1);
    // // }
    // // catch(InterruptedException ex){
    // // }
    // // SingleNumberPrinter np = new SingleNumberPrinter(i);
    // // np.start();
    // // }

    // // ExecutorService es = Executors.newFixedThreadPool(10);
    // // for(int i=1; i<=100; i++){
    // // try{
    // // Thread.sleep(1);
    // // }
    // // catch(InterruptedException e){}
    // // es.submit(new SingleNumberPrinter(i));
    // // }

    // // ExecutorService es = Executors.newFixedThreadPool(10);
    // // ArrayList<Integer> list = new ArrayList<>();
    // // list.add(1);
    // // list.add(2);
    // // list.add(3);
    // // list.add(4);
    // // list.add(5);
    // // ArrayList<Integer> result = new ArrayList<>();
    // // ListMultiplier task = new ListMultiplier(list, result);
    // // es.submit(task);
    // // System.out.println("Original list: " + list);
    // // System.out.println("Result list: " + result);
    // // es.shutdown();
    // ArrayList<Integer> list = new ArrayList<>();
    // for(int i=10; i>=0; i--){
    // list.add(i);
    // }

    // Sorter sorter = new Sorter(list);

    // ExecutorService es = Executors.newCachedThreadPool();
    // Future<ArrayList<Integer>> sortedFuture = es.submit(sorter);

    // ArrayList<Integer> sortedList = sortedFuture.get();
    // System.out.println("Sorted List: " + sortedList);
    // }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Value sharedValue = new Value(0);

        Adder adder = new Adder(sharedValue);
        Subtractor subtractor = new Subtractor(sharedValue);

        ExecutorService executor = Executors.newCachedThreadPool();

        Future<Void> adderFuture = executor.submit(adder);
        Future<Void> subtractorFuture = executor.submit(subtractor);

        adderFuture.get(); // Wait for adder to finish
        subtractorFuture.get(); // Wait for subtractor to finish

        executor.shutdown();

        System.out.println("Final value: " + sharedValue.getVal());
    }
}