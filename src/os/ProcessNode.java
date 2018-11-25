package os;

public class ProcessNode {
	private Process process;
	private ProcessNode next;
	
	public ProcessNode() {
		this(null);
	}
	
	public ProcessNode(Process process) {
		this(process, null);
	}
	
	public ProcessNode(Process process, ProcessNode next) {
		this.process = process;
		this.next = next;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public ProcessNode getNext() {
		return next;
	}

	public void setNext(ProcessNode next) {
		this.next = next;
	}
	
	
}
