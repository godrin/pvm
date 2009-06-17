package vm_java.machine;

import vm_java.VM;

public class Process {
	private VMThread thread;

	public Process(VM pVm) {
		thread=new VMThread(pVm);
	}
	
	public void run() {
		thread.start();
	}

	public boolean running() {
		return thread.todoLeft();
	}

	public void join() throws InterruptedException {
		thread.join();
	}

}
