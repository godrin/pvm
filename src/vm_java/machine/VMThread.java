package vm_java.machine;

import vm_java.VM;
import vm_java.code.VMException;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.internal.VMLog;
import vm_java.types.VMExceptionFunctionNotFound;

public class VMThread extends Thread {

	private VM vm;
	private Task job;
	private int id;

	public VMThread(VM pVM, int pID) {
		vm = pVM;
		id = pID;
	}

	@Override
	public void run() {
		super.run();

		try {

			while (vm.running()) {
				getJob();
				if (job == null) {
					try {
						Thread.sleep(Options.GRANULARITY_MS);
					} catch (InterruptedException e) {
						VMLog.error(e);
					}
					continue;
				}
				long startTime = System.currentTimeMillis();
				long endTime = startTime;

				try {
					if (job.canResume()) {
						VMLog.debug("Running Job on thread " + id + ":"
								+ job.inspect() + " ##:" + vm.jobCount());
						job.go();
					} else {

						VMLog.debug("Not Running Job:" + job.inspect()
								+ " waiting for " + job.childJobsCount()
								+ " all ##:" + vm.jobCount());

						if (job.childJobsCount() > 0) {
							job.debugChildren();
						}

						vm.addJob(job);
						continue;

					}
				} catch (VMException e) {
					VMLog.error(e);
				} catch (VMExceptionOutOfMemory e) {
					VMLog.error(e);
				} catch (VMExceptionFunctionNotFound e) {
					VMLog.error(e);
				}

				endTime = System.currentTimeMillis();
				long diff = endTime - startTime;

				double time = (diff / 1000.0);

				job.getScope().getContext().addTime(time);
				job = null;

			}

		} catch (VMFatalError fatal) {
			vm.setFatalError(fatal);
		}
	}

	private synchronized void getJob() {
		job = vm.popJob();
	}

	boolean todoLeft() {
		return job != null;
	}
}
