package vm_java.machine;

import vm_java.VM;
import vm_java.code.Statement;
import vm_java.code.VMException;
import vm_java.context.VMScope;

public class VMThread extends Thread {
	
	private VM vm;
	private Job job;
	
	public VMThread(VM pVM) {
		vm=pVM;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		
		while(vm.running()) {
			getJob();
			if(job==null)
			{
				continue;
			}
			Statement statement=job.getNextStatement();
			VMScope scope=job.getScope();
			try {
				statement.execute(scope);
			} catch (VMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			putJob();
		}
	}
	
	private synchronized void getJob() {
		job=vm.popJob();
	}
	private synchronized void putJob() {
		if(!job.finished()) {
			vm.addJob(job);
			job=null;
		}
	}

	boolean todoLeft() {
		return job!=null;
	}

}
