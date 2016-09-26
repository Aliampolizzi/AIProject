public class Brain {
	/*Brain Constants*/
	final static int INPUTS=15;
	static final int LAYERS=3;
	final static int NEURONSPERLAYER=10;
	final int OUTPUTS=6;
	final double BIASSIZE = 3,
				 BIASMUTATION=0.1;
	/*~~~~~~~~~~~~~~~*/
	
	private Neuron[][] hiddenLayers= new Neuron[LAYERS][NEURONSPERLAYER];
	private Neuron[] outputLayer = new Neuron[OUTPUTS];
	double[] bias = new double[LAYERS+1];
	
	//Create randomly generated Brain
	public Brain(){
		
		//Randomly Generate a Bias value for each layer, including the Output Layer
		for(int l=0; l<LAYERS+1; l++)
			bias[l]=Math.random()*(BIASSIZE*2)-BIASSIZE;
		
		//Randomly Generate the Hidden Layer
		for(int l=0; l<LAYERS; l++)
			for(int n=0; n<NEURONSPERLAYER; n++)
				hiddenLayers[l][n]=new Neuron(l, bias[l]);
		
		//Randomly Generate the Output Layer
		for(int o=0; o<OUTPUTS; o++)
			outputLayer[o]=new Neuron(LAYERS+1, bias[LAYERS]);
		
		
	}
	
	//Create Brain based on existing Brain
	public Brain(Brain b){
		
		//Generate a Bias value for each layer including the Output Layer based on the Bias values in Brain b
		bias = b.bias;
		for(int l=0; l<LAYERS+1; l++)
			bias[l]*=1+(Math.random()*(BIASMUTATION*2)-BIASMUTATION);
		
		//Generate the Hidden Layer based on the Neurons from Brain b
		for(int l=0; l<LAYERS; l++)
			for(int n=0; n<NEURONSPERLAYER; n++)
				hiddenLayers[l][n]=new Neuron(b.hiddenLayers[l][n], bias[l]);

		//Generate the Output Layer based on the Neurons from Brain b
		for(int o=0; o<OUTPUTS; o++)
			outputLayer[o]=new Neuron(b.outputLayer[o],bias[LAYERS]);
		
		
		
	}
	
	public double[] tick(double[] in){
		double[] out = new double[OUTPUTS],
				 ins = new double[NEURONSPERLAYER],
				 temp= ins;
		//Hidden Layer 1 loop
		for(int l1=0; l1<NEURONSPERLAYER; l1++)
			ins[l1]=hiddenLayers[0][l1].tick(in);
		
		//Hidden Layer 2-LAYERS loop
		for(int l=1; l<LAYERS; l++){
			for(int n=0; n<NEURONSPERLAYER; n++)
				temp[n]=hiddenLayers[l][n].tick(ins);
			ins=temp;
		}
		
		//Output Layer loop
		for(int o=0; o<OUTPUTS; o++)
			out[o]=outputLayer[o].tick(ins);
			
		return out;
	}
	
	
}