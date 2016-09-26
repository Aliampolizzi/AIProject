import java.util.ArrayList;

public class Population {
	
	/*Population Constants*/
	final static int POPULATIONSIZE = 100;
	/*~~~~~~~~~~~~~~~~~~~~*/	
	
	private ArrayList<Organism> population= new ArrayList<Organism>();
	
	
	
	public Population(){
		//Initialize population
		for(int i=0; i<POPULATIONSIZE; i++)
			population.add(new Organism());
	}//End Population()

	//Returns the age, generation, and index of the oldest Organism in the Population
	public int[] oldestOrganism(){
		int[] oldest = {0,-1,0};
		for(int i=0; i<POPULATIONSIZE-1; i++)
			if(population.get(i).age>oldest[0]){
				oldest[0]=population.get(i).age;
				oldest[1]=i;
				oldest[2]=population.get(i).generation;
			}
		return oldest;
	}//End oldestOrganism()
	public int[][] oldestOrganisms(){
		int[][] top10 = new int[POPULATIONSIZE/10][2];
		
		for(int i=0; i<POPULATIONSIZE-1; i++)
			for(int e=0; e<top10.length; e++)
				if(top10[e][0]<population.get(i).age){
					top10[e][0]=population.get(i).age;
					top10[e][1]=i;}
		
		return top10;		
	}
	
	//Accessor for Organisms in the Population
	public Organism get(int i){return population.get(i);}
	
	//Remove an Organism from the Population and replace it with one generated from a randomly selected living Organism's Brain
	public void kill(int i){
		
		
		/*DEBUG*/
		if(World.VERBOSE>0){
		System.out.println("Organism Died at "+population.get(i).pos+"\nAge: "+population.get(i).age+" Generation: "+population.get(i).generation+"\nColor: "+population.get(i).color);
		
		}
		/*END-DEBUG*/
		
		
		population.remove(i);
		double t = Math.random();
		if(t>0.97){
			population.add(new Organism());	
		}else if(t>0.7){
		int temp = (int)(Math.random()*99);
		population.add(new Organism( population.get(temp).brain, population.get(temp).generation+1 ));	}
		else {
			int[][] e =oldestOrganisms();
			int r = (int)Math.round(Math.random()*(e.length-1));
			population.add(new Organism( population.get(e[r][1]).brain, population.get(e[r][1]).generation+1 ));
		}
	}//End kill()

}//End Population
