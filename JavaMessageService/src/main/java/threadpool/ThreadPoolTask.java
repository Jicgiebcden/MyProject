package threadpool;

import java.io.Serializable;

public class ThreadPoolTask implements Runnable, Serializable {

	private static final long serialVersionUID = -5065318424727227901L;

	private Object attachData;

	ThreadPoolTask(Object tasks) {
		this.attachData = tasks;
	}

	public void run() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("开始执行任务：" + attachData);
		
		attachData = null;
	}

	public Object getTask() {
		return this.attachData;
	}
}