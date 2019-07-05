
public class Client {
	
	
public static void main(String[] args) throws Exception {
	long startTime = System.nanoTime();
	System.out.println(startTime);
	final String settingFile= "variable_setting.txt";
	final String testFile= "mnist_test.csv";
	final String trainFile = "mnist_train.csv";
	double[] settings = new double[8];
	
	ReadFile.getSettings(settingFile, settings);
	Layers ANN = new Layers(settings);
	int ePoch = ANN.getEpoch();
	for (int num_Epoch=0; num_Epoch<ePoch; num_Epoch++) {
		System.out.println("EPOCH number ... "+ num_Epoch);
		ANN.getTheError();
		ANN.trainNetwork(ANN, trainFile);
	
		}
	
	ANN.test(ANN, testFile);
	long endTime   = System.nanoTime();
	long totalTime = endTime - startTime;
	System.out.println(totalTime);
	}
}
