import java.util.Scanner;

public class ReadFile {
    String train_file_name;
    private static int numberOfInputs;
	static int input_Layers;
 	static int output_Layers;
 	static int hidden_Layers;
 	static double minWeight;
 	static double maxWeight;
 	static double learningRate;
    static int ePoch;
    
    static In in;
    static String settingFile= "variable_setting.txt";
 	static String trainingFile= "mnist_train.csv";
 	private double[] mysetting = new double[8];
	public ReadFile() {
		readSetting(settingFile, mysetting);
	}

	static double[] getSettings(String settingFile, double[] settings) {
		
		readSetting(settingFile, settings);
		return settings;
	}
	@SuppressWarnings("resource")
	static void readSetting(String settingFile, double[] settings2) {
		int e=0;
		In in = new In(settingFile);
		@SuppressWarnings("unused")
		String header = in.readLine();	
		String line = in.readLine();	
		Scanner scanner = new Scanner(line);
		while (scanner.hasNext()) {
		    String token=scanner.next();
		    settings2[e]=Double.parseDouble(token);
		    e++;
		};
		ReadFile.minWeight = settings2[0];
		ReadFile.maxWeight = settings2[1];
		ReadFile.learningRate = settings2[2];
		ReadFile.numberOfInputs = (int) settings2[3];
		ReadFile.input_Layers =(int) settings2[4];
		ReadFile.output_Layers =(int)settings2[5];
		ReadFile.hidden_Layers =(int)settings2[6];
		ReadFile.ePoch= (int)settings2[7];
		
	}
	public  static void trianNetwork(Layers aNN, String trainFile, int inp, int outp) {
		int k =0;
		String line ;
		In in = new In(trainFile);
		while(k<numberOfInputs) {
			 double[] oneInput =new double[inp+outp+1];
			 oneInput[0]=1.0; //first bit is considered as Input0 =1
			 line = in.readLine();
			 @SuppressWarnings("resource")
			 Scanner scanner = new Scanner(line);
			 scanner.useDelimiter(",");
			 int index =1;
			 String targetValue = scanner.next();
			 setTarget(oneInput, targetValue); // returns an array of size outputLayer that has the value of thresholds
			 
			 while(scanner.hasNext()){
				 String token = scanner.next();
				 oneInput[index]= Double.parseDouble(token);
				 index++;
			 }
			 aNN.forwarddrop(oneInput, targetValue);
			 aNN.backpropError(oneInput);
			 k ++;
		}
	}	
	
    private static void setTarget(double[] input, String target2) {
    	int i= Integer.parseInt(target2);
    	for (int j=input_Layers+1;j<input_Layers+i+1;j++) 
			input[j] = 0.01;
		input[i+input_Layers+1]= 0.49;
		for(int j=input_Layers+i+2; j<input.length; j++) 
			input[j]= 0.01;
	}
    /* Test files due the design of the network have different methods to read etc. 
     * 
     * 
     * 
     * 
     */
	public static void test(Layers ann, String testFile) {
		String line ;
		In in = new In(testFile);
		while (in.hasNextLine()) {
			 double[] oneInput =new double[input_Layers+output_Layers];
			 line = in.readLine();
			 @SuppressWarnings("resource")
			 Scanner scanner = new Scanner(line);
			 scanner.useDelimiter(",");
			 int index =0;
			 String targetValue = scanner.next();
			 setTestTarget(oneInput, targetValue);
			 while(scanner.hasNext()){
				 String token = scanner.next();
				 oneInput[index]= Double.parseDouble(token);
				 index++;
			 }
		ann.forwardtest(oneInput);
		ann.getTheError();
		}
	}

	private static void setTestTarget(double[] input, String target2) {
	
	    	int i= Integer.parseInt(target2);
	    	
			for (int j=input_Layers;j<input_Layers+i+1;j++) 
				input[j] = 0.01;
			
			input[i+input_Layers]= 0.49;
			for(int j=input_Layers+i+1; j<input.length; j++) 
				input[j]= 0.01;
	}
}
//try {
//	TimeUnit.SECONDS.sleep(5);
//} catch (InterruptedException e) {
//	e.printStackTrace();
//}