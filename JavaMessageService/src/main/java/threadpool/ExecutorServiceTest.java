package threadpool;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试线程池
 * ExecutorService会自主安排线程池中的线程并行处理多个任务
 * @author huangym3
 * @time 2016年11月25日 下午4:28:01
 */
public class ExecutorServiceTest {

	public static void main(String[] args) {
//		ExecutorService executorService = Executors.newFixedThreadPool(4);
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.execute(new Runnable() {
			public void run() {
				System.out.println("Asynchronous task");
			}
		});

		Set<Callable<String>> callables = new HashSet<Callable<String>>();

		callables.add(new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep(1000);
				System.out.println("Task 1");
				return "Task 1";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep(2000);
				System.out.println("Task 2");
				return "Task 2";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep(3000);
				System.out.println("Task 3");
				return "Task 3";
			}
		});

		try {
			executorService.invokeAll(callables);
			executorService.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
