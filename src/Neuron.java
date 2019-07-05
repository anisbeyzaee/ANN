import java.util.Random;

public class Neuron {
	
	private double learningRate;	
	private String layerType;
	private double[] weights; 
	private int weight_Number; //number of the following layer(defines how many weight each neuron will have)
	private double output;
	private double error ;
	
	public Neuron( String layer_type, double learningRate) {
		this.learningRate= learningRate;
		this.layerType = layer_type;
	}
	public Neuron(String layer_type, int number, double min, double max, double learningRate) {
		this.learningRate = learningRate;
		this.layerType = layer_type;
		this.weight_Number = number;
		weights = new double[weight_Number];
		setWeights(min, max);
	}
	public void applyActivationFunction(double netSum) {
		
		this.output =1.0/(1+(java.lang.Math.exp(-1.0*learningRate* netSum)));
		
	}
	public void setWeight(int j, double newWeight) {
		this.weights[j]= newWeight;
	}
	public  double derivative() { 
		return output * (1.0 - output);
		}
	public String  getLayerType(){ 
		return layerType;
		}
	public double[] getWeights() { 
		return weights;
		}
	public void setWeights(double min, double max) {
		for(int i=0; i<weights.length; i++) {
			Random r = new Random();
			weights[i] =(min + (max *2) * r.nextDouble());
			System.out.println(weights[i]);
		}
	}
	public double getOutput() { 
		return output;
		}
	public void setOutput(double object) { 
		this.output =  object;
		}
	public double getError() { 
		return error;
		}
	public void setError( double error) {
		this.error = error;
		}
	public String toString() {
		String returnValue= null;
		if (layerType.equals("I")) 
			returnValue ="(" + layerType + " : "+ String.format("%.16f",output)+ " )";
		else returnValue = "("+ layerType+ ", "+ String.format("%.16f", error)+" )";
	return returnValue;}
	
}
