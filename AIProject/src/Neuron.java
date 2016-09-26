import java.util.ArrayList;

public class Neuron {
	/*Neuron Constants*/
	final double MUTATIONTRATE = 0.075,
				 MUTATIONDEGREE= 0.0015;
	/*~~~~~~~~~~~~~~~~*/
	
	
	double[] weights;
	int layer;
	ArrayList<Integer> inputs = new ArrayList<Integer>();
	double bias;
	
	//Create completely random Neuron
	public Neuron(int l, double b){
		bias=b;
		
		if(l==1)//Make all Brain.INPUTS an Input for a first layer Neuron
			for(int n=0; n<Brain.INPUTS; n++)
				inputs.add(1);
		else for(int n=0; n<Brain.NEURONSPERLAYER; n++) //Randomly choose to make each Neuron in the previous Layer an Input
				if(Math.random()>=0.5)
					inputs.add(1);
				else inputs.add(0);
		if(!inputs.contains(1)) //If no inputs were chosen, randomly select one Neuron from the previous Layer as an Input
			inputs.set((int)Math.random()*Brain.NEURONSPERLAYER, 1);
		
		weights= new double[inputs.size()]; //Create an array with a randomly 1 generated weight between -5 and 5 for each Input 
		for(int i=0; i<inputs.size(); i++)
			weights[i]=Math.random()*10-5;
		
	}
	
	//Create Neuron based on existing Neuron
	public Neuron(Neuron n, double b){
		//Set this Neuron to the Neuron on which to base it
		inputs=n.inputs;
		weights=n.weights;
		layer=n.layer;
		bias=b;
		//Make a MUTATIONRATE % chance to change the state of each input
		/*for(int i=0; i<inputs.size(); i++)
			if(Math.random()<=MUTATIONTRATE)
				if(inputs.get(i)==1)inputs.set(i, 0);
				else inputs.set(i, 1);*/
		
		//Change each weight by +/- MUTATIONDEGREE %
		for(int i=0; i<weights.length; i++)
			weights[i]*=1+(Math.random()*MUTATIONDEGREE-(MUTATIONDEGREE/2));
	}
	
	public double tick(double[] in){
		
		double weightedSum= bias;
		
		for(int i=0; i<in.length; i++)
			if(inputs.contains(i))
			weightedSum+=weights[i]*in[i];
		
		return Math.tanh(weightedSum);
		
	}
	
}
