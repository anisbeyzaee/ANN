import java.util.stream.IntStream;
public class Layers {
	int pass=0;
	int fail=0;
	double accuraccy=0;
	double netSum;
	private Neuron[] input_layer;
	private Neuron[] hidden_layer;
	private Neuron[] output_layer;
	private int inputLayer;
	private int outputLayer;
	private int hiddenLayer ;
	private double minWeight ;
	private double maxWeight ;
	private double learningRate;
	private int ePoch;
	private double[] target;
	  	// constructor 
public Layers(double[] settings) 
{	readSettings(settings);
	input_layer = new Neuron[inputLayer+1];
	hidden_layer = new Neuron[hiddenLayer+1];
	output_layer = new Neuron[outputLayer];
	IntStream.range(0, inputLayer+1).forEach(i -> input_layer[i] = new Neuron("I", hiddenLayer+1, minWeight, maxWeight,learningRate));
	IntStream.range(0, hiddenLayer+1).forEach(i -> hidden_layer[i] = new Neuron("H", outputLayer, minWeight, maxWeight,learningRate));
	IntStream.range(0, outputLayer).forEach(i -> output_layer[i] = new Neuron("O",learningRate));
	target = new double[outputLayer];
}
private void readSettings(double[] settings) {
	for(int i=0; i< settings.length; i++) {
		this.minWeight = settings[0];
		this.maxWeight = settings[1];
		this.learningRate = settings[2];
		this.inputLayer =(int) settings[4];
		this.outputLayer =(int)settings[5];
		this.hiddenLayer =(int)settings[6];
		this.ePoch= (int)settings[7];
	}
}
public double[] getTarget() {
	return target;
}

public Layers forwarddrop(double[] lineArray, String targetvalue) 
{
	
			 // Forwarding training data through the input layer
			 
	for (int j = 0; j< input_layer.length-1; j++) 
		input_layer[j].setOutput(lineArray[j]); 
			 
	/*
	 * setting up the target array 
	 * This Array later will be compared to the output from output layer
	 */
	for (int m=0; m<outputLayer; m++) 
		target[m]=lineArray[inputLayer+m+1];
			 
	/*
	 * hidden layer
	 * first element is the neuron that has the threshold
	 * weight of the neuron is being assigned already 
	 * but the output is 1
	 */
	hidden_layer[0].setOutput(1.0);
	
	for (int k= 1; k< hidden_layer.length;k++) 
	{
		//netSum =  input_layer[0].getWeights()[k-1];
		//hidden_layer[0].applyActivationFunction(1.0);
		netSum =  input_layer[0].getWeights()[k-1];
			for(int j = 1; j<input_layer.length; j++) 
				netSum= netSum +( input_layer[j].getWeights()[k-1] * input_layer[j].getOutput()); 
			hidden_layer[k].applyActivationFunction(netSum);
		
	} 
	int index=0;
	for (Neuron n: output_layer) 
    {	
		double netSum2= hidden_layer[0].getWeights()[index];
		
		for(int j = 1; j<hidden_layer.length; j++) {
			netSum2 = netSum2 + hidden_layer[j].getWeights()[index] * hidden_layer[j].getOutput();
		}
		n.applyActivationFunction(netSum2);
		index++;
	}
	return this;
}


public Layers backpropError(double[] lineArray){
	int index0=0;
	for (Neuron n :output_layer) 
	{
		n.setError((target[index0] - n.getOutput()) * n.derivative()); //n.derivative()
		index0++;
	}
	// updating error of each hidden unit
	for (Neuron n :hidden_layer) {
		double error=n.derivative();
		double temp= 0;//0 HERE  
		for ( int j=0; j< output_layer.length; j++) 
			temp= temp+ (n.getWeights()[j] * output_layer[j].getError());
		
		n.setError(temp* error);
	}
	// update each weight for output layer and hidden layer
	for (Neuron n: hidden_layer) {  //per hidden layer there is update weight for output 
		int index2 = 0;
		for(int j=0; j< output_layer.length; j++) {
			
			n.setWeight(index2, n.getWeights()[j] + (learningRate * output_layer[index2].getError()* n.getOutput()));
			
			index2++;
		}
	}
	for (Neuron n :input_layer) { // per input layer each weight for hidden layer is updated
		int index = 0;
		for(int j=0; j< hidden_layer.length; j++) {
			
			n.setWeight(index, n.getWeights()[j] + learningRate * hidden_layer[index].getError()* input_layer[j].getOutput());
			index++;
		}
	}
return this;
}

public Layers forwardtest(double[] lineArray){
	input_layer[0].setOutput(1);
	for (int j = 0; j< input_layer.length; j++) {
		input_layer[j].setOutput(lineArray[j]); 
	}
	
	// setting up the target array
	for (int m=0; m<outputLayer; m++) 
		target[m]=lineArray[inputLayer+m];
	 	
	/*  hidden layer
	*   
	*   
	*/ 
	hidden_layer[0].applyActivationFunction(1.0); //hidden_layer[0].setOutput(1.0);
	for (int k= 1; k< hidden_layer.length;k++){
	 
		netSum =  input_layer[0].getWeights()[k-1];
		
		for(int j = 1; j<input_layer.length; j++) 
			netSum= netSum +( input_layer[j].getWeights()[k-1] * input_layer[j].getOutput()); 
			hidden_layer[k].applyActivationFunction(netSum);
	}
	
	
	//output    
	int index=0;
	for (Neuron n: output_layer){	
		double netSum2= hidden_layer[0].getWeights()[index];
		for(int j = 1; j<hidden_layer.length; j++) {
			netSum2 = netSum2 + hidden_layer[j].getWeights()[index] * hidden_layer[j].getOutput();
		}
		n.applyActivationFunction(netSum2);
		index++;
	}
	accuracyCheck();
return this;
}

	 private void accuracyCheck() {
	if (predict() == getValueOfTarget())
		this.pass++;
	else this.fail++;
	System.out.println("predicted value = " +predict() + "    real value  " + getValueOfTarget());
	System.out.println("number of pass ..." + pass     + "      number of Fail ==  " +  fail);
	printAccuracy();
}
	 public void printAccuracy() {
			accuraccy = ((double) pass/((double) fail+ (double) pass) )*100;
			System.out.println( "                ||         |||          Accuracy is   % " + accuraccy  );
			
		}
	 public int predict() {
			int index=0;
			int counter=0;
			double check=output_layer[0].getOutput();
			for (Neuron n: output_layer) {
				if (n.getOutput() > check) {
					check =n.getOutput();
					counter = index;
					index++;
				}else index++;
			}
			return counter;
		}
	public int getValueOfTarget() {
		int index=0;
		int counter=3;
		double check=0.01;
		for (double n: target) {
			if (n > check) {
				check =n;
				counter = index;
				index++;
			}else index++;
		}
		return counter;
	}
	
	public String toString() { 
		 
		 return this.input_layer.toString(); 
	 }
	
	public void trainNetwork(Layers ann, String trainFile) {
		ReadFile.trianNetwork( ann, trainFile, inputLayer, outputLayer);
	}
	
	public void test(Layers ann, String trainFile ) {
		ReadFile.test(ann, trainFile);
	}
	
	public void getTheError(){
		double sum=0;
		int index=0;
		for(Neuron n: output_layer) {
			
			sum= sum + ((Math.pow((target[index] - n.getOutput()), 2))/2);
			index++;
		}
		System.out.println("  <<<<<<< Error of the ANN>>>>>>" + sum);
	}
	
	public int getEpoch() {
		return ePoch;
	}
}
