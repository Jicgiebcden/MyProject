package threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <B>ThreadPoolExecutor配置</B>
 * <br>
 * 一、ThreadPoolExcutor为一些Executor提供了基本的实现，这些Executor是由Executors中的工厂 newCahceThreadPool、
 * newFixedThreadPool和newScheduledThreadExecutor返回的。 ThreadPoolExecutor是一个灵活的健壮的池实现，允许各种各样的用户定制。
 * <br>
 * 二、线程的创建与销毁
 * <br>1、核心池大小、最大池大小和存活时间共同管理着线程的创建与销毁。
 * <br>2、核心池的大小是目标的大小；线程池的实现试图维护池的大小；即使没有任务执行，池的大小也等于核心池的大小，并直到工作队列充满前，池都不会创建更多的线程。如果当前池的大小超过了核心池的大小，线程池就会终止它。
 * <br>3、最大池的大小是可同时活动的线程数的上限。
 * <br>4、如果一个线程已经闲置的时间超过了存活时间，它将成为一个被回收的候选者。
 * <br>5、newFixedThreadPool工厂为请求的池设置了核心池的大小和最大池的大小，而且池永远不会超时
 * <br>6、newCacheThreadPool工厂将最大池的大小设置为Integer.MAX_VALUE，核心池的大小设置为0，超时设置为一分钟。这样创建了无限扩大的线程池，会在需求量减少的情况下减少线程数量。
 * <br>
 * 三、管理
 * <br>1、 ThreadPoolExecutor允许你提供一个BlockingQueue来持有等待执行的任务。任务排队有3种基本方法：无限队列、有限队列和同步移交。
 * <br>2、 newFixedThreadPool和newSingleThreadExectuor默认使用的是一个无限的 LinkedBlockingQueue。如果所有的工作者线程都处于忙碌状态，
 * 任务会在队列中等候。如果任务持续快速到达，超过了它们被执行的速度，队列也会无限制地增加。稳妥的策略是使用有限队列，比如ArrayBlockingQueue或有限的LinkedBlockingQueue以及 PriorityBlockingQueue。
 * <br>3、对于庞大或无限的池，可以使用SynchronousQueue，完全绕开队列，直接将任务由生产者交给工作者线程
 * <br>4、可以使用PriorityBlockingQueue通过优先级安排任务
 */
public class TestThreadPool {

	private static int produceTaskSleepTime = 2;

	private static int produceTaskMaxNumber = 10;

	public static void main(String[] args) {

		// 构造一个线程池
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 3,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(6),
//				new ThreadPoolExecutor.DiscardOldestPolicy());// 等待的任务队列满的时候，会抛弃最旧的任务
				new ThreadPoolExecutor.AbortPolicy());// 等待队列满的时候，会抛出RejectedExecutionException异常

		for (int i = 1; i <= produceTaskMaxNumber; i++) {
			try {
				String task = "task@ " + i;
				System.out.println("创建任务并提交到线程池中：" + task);
				threadPool.execute(new ThreadPoolTask(task));

//				Thread.sleep(produceTaskSleepTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}