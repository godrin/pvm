package vm_java.machine;

import vm_java.VM;
import vm_java.code.CodeStatement;
import vm_java.code.VMException;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.types.VMExceptionFunctionNotFound;

public class VMThread extends Thread {

	private VM vm;
	private Task job;

	public VMThread(VM pVM) {
		vm = pVM;
	}

	@Override
	public void run() {
		super.run();

		while (vm.running()) {
			getJob();
			if (job == null) {
				try {
					Thread.sleep(Options.GRANULARITY_MS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			long startTime = System.currentTimeMillis();
			long endTime = startTime;

			try {
				job.run();
			} catch (VMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (VMExceptionOutOfMemory e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (VMExceptionFunctionNotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			endTime = System.currentTimeMillis();
			long diff = endTime - startTime;

			double time = (diff / 1000.0);

			job.getScope().getContext().addTime(time);
			job = null;

		}
	}

	private synchronized void getJob() {
		job = vm.popJob();
	}

	boolean todoLeft() {
		return job != null;
	}
}
