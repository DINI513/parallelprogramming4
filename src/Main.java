import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public final static int THREADS = 10;
    public final static int CAPACITY = 15;

    public static void main(String[] args){
        RunTestMap(new HashMap<Integer, Integer>(CAPACITY));
        RunTestMap(new HashMap<Integer, Integer>(CAPACITY));
        RunTestMap(new Hashtable<Integer, Integer>(CAPACITY));
        RunTestMap(Collections.synchronizedMap(new HashMap<Integer, Integer>(CAPACITY)));
        RunTestMap(new ConcurrentHashMap<Integer, Integer>(CAPACITY));
    }

    public static void RunTestMap(Map<Integer, Integer> map){
        Random random = new Random();
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
        long timeStart = System.currentTimeMillis();

        for (int i = 1; i <= THREADS; i++) {
            if (i % 2 == 0)
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 100000; i++)
                            map.put(random.nextInt(CAPACITY), random.nextInt());
                    }
                });
            else
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 100000; i++)
                            map.get(random.nextInt(CAPACITY));
                    }
                });

        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long timeEnd = System.currentTimeMillis();
        System.out.println("Тестированние для " + map.getClass() + " завершено. Затраченное время: " + (timeEnd - timeStart) + " мс");
    }
}