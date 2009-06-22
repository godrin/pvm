package vm_java.machine;

import vm_java.VM;
import vm_java.code.CodeStatement;
import vm_java.code.VMException;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.types.VMExceptionFunctionNotFound;

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
				try {
					Thread.sleep(Options.GRANULARITY_MS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			long startTime=System.currentTimeMillis();
			long endTime=startTime;
			VMScope scope=job.getScope();
			
			do {
				CodeStatement statement=job.getNextStatement();
				if(statement==null)
					break;
				
				try {
					statement.execute(scope);
				} catch (VMException e) {
					//FIXME: implement exceptions !!!
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (VMExceptionOutOfMemory e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (VMExceptionFunctionNotFound e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				endTime=System.currentTimeMillis();
			}while((endTime-startTime)<Options.GRANULARITY_MS);
			System.out.println("ONE RUN:"+(endTime-startTime));
			
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
