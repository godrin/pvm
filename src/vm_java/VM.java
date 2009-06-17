/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import vm_java.code.Program;
import vm_java.context.VMContext;
import vm_java.context.VMScope;
import vm_java.machine.Job;
import vm_java.machine.Options;
import vm_java.machine.Process;

/**
 * 
 * @author davidkamphausen
 */
public class VM {
	private List<VMContext> mContexts = new ArrayList<VMContext>();
	private Queue<Job> mJobs = new ArrayDeque<Job>();
	private List<Process> mProcesses = new ArrayList<Process>();

	public VM() {

	}

	public synchronized VMContext createContext() {
		VMContext c = new VMContext(this);
		mContexts.add(c);
		return c;
	}

	public long getMemoryUsage() {
		long s = 0;
		for (VMContext c : mContexts) {
			s += c.getMemoryUsage();
		}
		return s;
	}

	public void run(Program program) {
		VMScope scope = new VMScope(program.getContext());
		addJob(new Job(program, scope));

	}

	public void addJob(Job job) {
		mJobs.add(job);
	}

	public Job popJob() {
		return mJobs.poll();
	}

	public void run() {
		for (int i = 0; i < Options.getInstance().getThreadCount(); i++) {
			mProcesses.add(new Process(this));
		}
		for (Process p : mProcesses) {
			p.run();
		}
	}

	public void join() throws InterruptedException {
		for (Process p : mProcesses) {
			p.join();
		}

	
	}

	public boolean finished() {
		if (mJobs.size() > 0)
			return false;
		for (Process p : mProcesses) {
			if (p.running())
				return false;
		}

		return true;
	}

	public boolean running() {
		return !finished();
	}
}
