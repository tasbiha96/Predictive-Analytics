
public class SimilarityMeasure {

	// Euclidean distance
    public static double euclideanDistance(double[] vectorA, double[] vectorB) {

        double sum = 0.0;

        for (int i=0; i<vectorA.length; i++) {
            sum = sum + Math.pow(vectorA[i] - vectorB[i],2);
        }
        return Math.sqrt(sum);
    }
    
    // Cosine similarity
    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }   
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

}
