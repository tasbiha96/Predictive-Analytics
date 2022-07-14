import java.io.*;

public class PerformanceMeasures{
	int[] actualClass;
	int[] predictedClass;
	int[][] confusionMatrix;
	int[] truePositives;
	int[] falsePositives;
	int[] falseNegatives;
	int clusters;
	
	public PerformanceMeasures(int[] actualClass,int[] predictedClass, int clusters) {
		this.actualClass = actualClass;
		this.predictedClass = predictedClass;
		this.clusters = clusters;
		
		confusionMatrix = new int[clusters][clusters];
		truePositives = new int[clusters];
		falsePositives = new int[clusters];
		falseNegatives = new int[clusters];
	}
	public void printPerformance(String model) throws IOException {
		FileWriter writer = new FileWriter(model+"_model_performance.txt");
		
		System.out.println("Confusion Matrix");
		writer.write("Confusion Matrix\n");
		for (int i = 0; i<actualClass.length; i++) {
			//System.out.println("cluster_labels[i] " + cluster_labels[i] + " original_labels[i] "  + original_labels[i]);
			confusionMatrix[predictedClass[i]][actualClass[i]]++;
		}
		
	
}