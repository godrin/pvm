package vm_java.machine;

public class Options {
	private int threadCount;

	private long maxTotalMemory;
	private long maxPrograms;
	private long maxMemoryPerProgram;
	private double maxTotalCPU;
	private double maxCPUPerProgram;

	private static Options mInstance = null;

	public static final long KB = 1024;
	public static final long MB = 1024 * KB;
	public static final long GB = 1024 * MB;
	
	public static final int GRANULARITY_MS = 10; 

	public static synchronized Options getInstance() {
		if (mInstance == null)
			mInstance = new Options();
		return mInstance;
	}

	private Options() {
		threadCount = 4;
		maxTotalMemory = 512 * MB;
		maxMemoryPerProgram = 128 * MB;
		maxPrograms = 16;
		maxTotalCPU = 0.2;
		maxCPUPerProgram = 0.1;
	}

	/**
	 * @return the threadCount
	 */
	public final int getThreadCount() {
		return threadCount;
	}

	/**
	 * @return the maxTotalMemory
	 */
	public final long getMaxTotalMemory() {
		return maxTotalMemory;
	}

	/**
	 * @return the maxPrograms
	 */
	public final long getMaxPrograms() {
		return maxPrograms;
	}

	/**
	 * @return the maxMemoryPerProgram
	 */
	public final long getMaxMemoryPerProgram() {
		return maxMemoryPerProgram;
	}

	/**
	 * @return the maxTotalCPU
	 */
	public final double getMaxTotalCPU() {
		return maxTotalCPU;
	}

	/**
	 * @return the maxCPUPerProgram
	 */
	public final double getMaxCPUPerProgram() {
		return maxCPUPerProgram;
	}

	/**
	 * @return the mInstance
	 */
	public static final Options getMInstance() {
		return mInstance;
	}
}
