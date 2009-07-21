/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vm_java;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import vm_java.code.Program;
import vm_java.context.VMContext;
import vm_java.context.VMExceptionOutOfMemory;
import vm_java.context.VMScope;
import vm_java.internal.VMLog;
import vm_java.llparser.LLParser2;
import vm_java.machine.Options;
import vm_java.machine.Process;
import vm_java.machine.Task;
import vm_java.machine.VMFatalError;

/**
 * 
 * @author davidkamphausen
 */
public class VM {
	private List<VMContext> mContexts = new ArrayList<VMContext>();
	private Queue<Task> mJobs = new LinkedList<Task>();
	private List<Process> mProcesses = new ArrayList<Process>();
	private boolean running = false;
	private LLParser2 mParser;
	private VMFatalError mFatal;

	public VM(LLParser2 pParser) {
		mParser = pParser;
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

	public void run(Program program) throws VMExceptionOutOfMemory {
		VMScope scope = new VMScope(program.getContext());
		addJob(program.execution(scope));
	}

	public synchronized void addJob(Task job) {
		mJobs.add(job);
	}

	public int jobCount() {
		return mJobs.size();
	}

	public synchronized Task popJob() {
		if (mJobs.size() > 0) {
			return mJobs.poll();
		} else {
			return null;
		}
	}

	public synchronized void run() {
		if (running)
			return;
		running = true;
		for (int i = 0; i < Options.getInstance().getThreadCount(); i++) {
			mProcesses.add(new Process(this, i));
		}
		for (Process p : mProcesses) {
			p.run();
		}
		running = false;
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

	public void enqueue(Task task) {
		mJobs.add(task);
	}

	public LLParser2 getParser() {
		return mParser;
	}

	public boolean hasJob(Task child) {
		return mJobs.contains(child);
	}

	public void setFatalError(VMFatalError fatal) {
		VMLog.error(fatal);
		mJobs.clear();
		mFatal = fatal;
	}

	public VMFatalError getFatalError() {
		return mFatal;
	}
}
