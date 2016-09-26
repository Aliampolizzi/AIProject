
/*          (***)  <-- KNOWN BUG          */
/*~~~~~~~~~~Project To-Do List~~~~~~~~~~*//*
 * Rework Brain
 * Write console to .txt file
 *** Absurd amounts of food spawn with time due to organisms' ability to generate it too easily
*//*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/



import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends Thread {
	/*World Constants*/
	final int FOODVALUE=30,
			  		 BASEENERGYTRANSFERONHIT=170,
			  		 FOODRESPAWNTIME=2500;
	static final int WORLDWIDTH=1000;
	final static int WORLDHEIGHT=800,
					 VERBOSE=0;
	final static int MAXFOODINWORLD=125;
	final static int FOODRADIUS=3;
	final static double FOODOFFSET = 1.65,
						FOODAREA = Math.PI*Math.pow(FOODRADIUS, 2);
	final double ORGANISMAREA = Math.PI*Math.pow(10, 2);
	final static int ORGANISMRADIUS = 10;
	final int FRAMESPERSECOND =300;
	final int TICKDELAY=1000/FRAMESPERSECOND;
	
	/*~~~~~~~~~~~~~~~*/
	
	static Population population;
	
	private ArrayList<Integer> foodRespawnQueue = new ArrayList<Integer>();
	
	static ArrayList<Point> food = new ArrayList<Point>();
	
	private boolean running = false,
					graphics=true;
	
	private int tick = 0,
				oldestOrganism=0;
	
	
	public static void main(String[] args) {
		
		
		/*DEBUG*/
		System.out.println("Main Thread Started");
		/*END-DEBUG*/
		
		(new World()).start();
		
		/*DEBUG*/
		System.out.println("World initialized");
		/*END-DEBUG*/
		
		

	}
	
	
	public void run(){
		/*DEBUG*/
		System.out.println("World Thread Started");
		/*END-DEBUG*/
		running = true;
		
		population = new Population();		
		foodInit();
		long time = System.currentTimeMillis();
		Graphics g = new Graphics();
		
		//Start World Main Loop
		while(running){
			if(graphics&&TICKDELAY-(System.currentTimeMillis()-time)<=0){
				//Increment Tick Counter
				tick++;

				/*DEBUG*/
				if(VERBOSE>1)
					System.out.println("Tick: "+tick);
				/*END-DEBUG*/
				if(tick%1000==0){
				int tempi=population.oldestOrganism()[0];
				if(oldestOrganism<tempi){oldestOrganism=tempi;
				System.out.println("Oldest Organism Ever: "+oldestOrganism);}}
				organismLoop();
				respawnFood();
				g.jPanel.repaint();
				
				
				time = System.currentTimeMillis();
			}else if(graphics){
				try {Thread.sleep(TICKDELAY-(System.currentTimeMillis()-time));} catch (InterruptedException e) {}
			}else if(!graphics){
				//Increment Tick Counter
				tick++;
				
				/*DEBUG*/
				if(VERBOSE>1)
					System.out.println("Tick: "+tick);
				/*END-DEBUG*/

				organismLoop();
				respawnFood();
			}
		}//End World Main Loop	
		
	}//End run()


	private void foodInit() {
		//Initialize Food
		for(int i=0; i<MAXFOODINWORLD; i++)
			food.add(new Point((int)(Math.random()*WORLDWIDTH)-FOODRADIUS,(int)(Math.random()*WORLDHEIGHT)-FOODRADIUS));
	}


	private void organismLoop() {
		//Generate input for each Organism in the Population
		double[]input=new double[Brain.INPUTS];			
		for(int o=0; o<population.POPULATIONSIZE; o++){
			
			
			
			if(population.get(o).energy<=0){
				population.kill(o);
				System.out.println("Tick: "+tick);
				o--;
				continue;
			}
			/*DEBUG*/
			if(VERBOSE>1)
			System.out.println("Organism "+o);
			/*END-DEBUG*/
			
			input=new double[Brain.INPUTS];
			
			input[0]=population.get(o).rotation;
			input[1]=population.get(o).speed;
			input[2]=population.get(o).energy;
			input[3]=population.get(o).color;
			input[4]=population.get(o).glowColor;
			
			Point p1=population.get(o).eye1Pos,
				  p2=population.get(o).eye2Pos;
			
			input[5]=0;
			input[6]=0;
			input[7]=0;
			input[8]=0;
			input[9]=0;
			input[10]=0;
			input[11]=0;
			input[12]=0;
			input[13]=0;
			input[14]=0;
			
			//Get Color and Scent information from Food
			for(int i=0; i<food.size(); i++){
				
				if(food.get(i).distance(population.get(o).pos)<ORGANISMRADIUS+FOODRADIUS){
					food.remove(i);
					i--;
					if(i<0)i=0;
					
					if(population.get(o).sheild){
					foodRespawnQueue.add(FOODRESPAWNTIME);
					foodRespawnQueue.add(FOODRESPAWNTIME);
					continue;} else {
						population.get(o).energy+=FOODVALUE;
						if(population.get(o).energy>Organism.MAXENERGY)population.get(o).energy=Organism.MAXENERGY;
						foodRespawnQueue.add(FOODRESPAWNTIME);
					}
				}
				
				
				double temp1=(1/Math.pow(p1.distance(food.get(i)),2))*100*FOODAREA;
				
				
				double temp2=(1/Math.pow(p2.distance(food.get(i)),2))*100*FOODAREA;
				
				//Red
				input[5]+=temp1;
				input[6]+=temp2;
				//Green
				input[7]+=temp1;
				input[8]+=temp2;
				//Blue
				input[9]+=temp1;
				input[10]+=temp2;
				
				//Nose
				input[11]+=temp1;
				input[12]+=temp2;
				
			}
			//Eyes and Sheild input for each organism
			for(int i=0; i<population.POPULATIONSIZE; i++){
				
				doCollide(o, i);
				
				double temp1=1/Math.pow(p1.distance(population.get(i).pos),2),
						temp2=1/Math.pow(p2.distance(population.get(i).pos),2);
				int g = population.get(i).glow ? 1:2;
					//Color
					if(population.get(i).color==-1){
						input[5]+=temp1*ORGANISMAREA*50*g;
						input[6]+=temp2*ORGANISMAREA*50*g;
					}
					else if(population.get(i).color==0){
						input[7]+=temp1*ORGANISMAREA*50*g;
						input[8]+=temp2*ORGANISMAREA*50*g;
					}
					else {
						input[9]+=temp1*ORGANISMAREA*50*g;
						input[10]+=temp2*ORGANISMAREA*50*g;
					}
					//Glow Color
					if(population.get(i).glow){
						if(population.get(i).glowColor==-1){
							input[5]+=temp1*ORGANISMAREA*50;
							input[6]+=temp2*ORGANISMAREA*50;
						}
						else if(population.get(i).glowColor==0){
							input[7]+=temp1*ORGANISMAREA*50;
							input[8]+=temp2*ORGANISMAREA*50;
						}
						else {
							input[9]+=temp1*ORGANISMAREA*50;
							input[10]+=temp2*ORGANISMAREA*50;
						}
					}
					
					//Sheild
					if(population.get(i).sheild){
						input[13]+=temp1;
						input[14]+=temp2;
					}
					
			}//End Organism i loop
			
			population.get(o).tick(input);
						
		}//End Organism o Loop
	}


	private void respawnFood() {
		//Respawn Food
		ArrayList<Integer> tempFoodRespawnQueue = new ArrayList<Integer>();
		for(Integer i:foodRespawnQueue){
			i--;
			if(i<0){
				food.add(new Point((int)(Math.random()*WORLDWIDTH)-FOODRADIUS,(int)(Math.random()*WORLDHEIGHT)-FOODRADIUS));
				continue;
			}
			tempFoodRespawnQueue.add(i);
		} foodRespawnQueue = tempFoodRespawnQueue;
	}


	private void doCollide(int o, int i) {
		/*Collisions between Organisms*/
		if(population.get(o).pos.distance(population.get(i).pos)<ORGANISMRADIUS*2){
			//if Organism o transfers energy to Organism i
			if((population.get(o).color==-1&&population.get(i).color==1)||
					(population.get(o).color==0&&population.get(i).color==-1)||
					(population.get(o).color==1&&population.get(i).color==0)){
				
				population.get(o).color=population.get(i).color;
				population.get(o).energy-=Math.sqrt(Math.pow(BASEENERGYTRANSFERONHIT,2)*(population.get(o).speed/5));
				population.get(i).energy+=Math.sqrt(Math.pow(BASEENERGYTRANSFERONHIT,2)*(population.get(i).speed/5));
				
				if(population.get(i).energy>Organism.MAXENERGY)
					population.get(i).energy=Organism.MAXENERGY;
			
			} else { //If Organism i transfers energy to Organism o
				
				population.get(i).color=population.get(o).color;
				population.get(i).energy-=Math.sqrt(Math.pow(BASEENERGYTRANSFERONHIT,2)*(population.get(i).speed/5));
				population.get(o).energy+=Math.sqrt(Math.pow(BASEENERGYTRANSFERONHIT,2)*(population.get(o).speed/5));
				
				if(population.get(o).energy>Organism.MAXENERGY)
					population.get(o).energy=Organism.MAXENERGY;
			
			}

		}/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
	}
	

}//End World Class
