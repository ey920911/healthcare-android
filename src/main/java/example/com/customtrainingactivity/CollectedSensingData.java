package example.com.customtrainingactivity;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CollectedSensingData {
	public static ConcurrentLinkedQueue<double[]> raw_data =  new ConcurrentLinkedQueue<double[]>();
	public static ConcurrentLinkedQueue<double[]> raw_data2 = new ConcurrentLinkedQueue<double[]>();
}
