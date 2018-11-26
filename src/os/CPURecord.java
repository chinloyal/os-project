package os;

public class CPURecord {
	private String CPUName;
	private double turnAroundTime;
	private double waitingTime;
	private double responseTime;
	private String algorithm;
	
	public CPURecord() {
		CPUName = "";
		turnAroundTime = waitingTime = responseTime = 0;
	}

	public String getCPUName() {
		return CPUName;
	}

	public void setCPUName(String cPUName) {
		CPUName = cPUName;
	}

	public double getTurnAroundTime() {
		return turnAroundTime;
	}

	public void setTurnAroundTime(double turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}

	public double getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(double waitingTime) {
		this.waitingTime = waitingTime;
	}

	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	
}
