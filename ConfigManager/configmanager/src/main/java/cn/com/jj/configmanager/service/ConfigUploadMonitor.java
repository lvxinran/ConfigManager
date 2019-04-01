package cn.com.jj.configmanager.service;

public class ConfigUploadMonitor {	
	private static double progress;

	public static double getProgress() {
		return progress;
	}
	

	public static void setProgress(double progress) {
		ConfigUploadMonitor.progress = progress;
	}

	public static void startMonitor() {
		setProgress(0.0);
	}
}
