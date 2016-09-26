import java.awt.Point;

public class Organism {
	/*Organism Constants*/
	final static int MAXENERGY=250;
	final int MAXMOVE=5;
	final int RADIUS=10;
	final double MAXROTATION=Math.PI/8,
				 BASETHRUSTERCOST=0.5,
				 SHEILDCOST=0.5,
				 PASSIVEENERGYDRAIN=0.1;
	/*~~~~~~~~~~~~~~~~~~*/
	
	Point pos,
		  eye1Pos=new Point(),
	      eye2Pos=new Point(),
	      nose1Pos=new Point(),
	      nose2Pos=new Point(),
	      sheild1Pos=new Point(),
	      sheild2Pos=new Point();
	double rotation,
		   energy=MAXENERGY,
		   speed=0;
	int age=0,
		generation,
		color,
		glowColor;
	boolean sheild=false,
			glow=false;
	Brain brain;
	

	
	//Create totally randomized Organism
	//Used in Population initialization
	public Organism(){
		init(0);
		brain=new Brain();
	}
	
	private void init(int g) {
		generation=g;

		pos = new Point((int)(Math.random()*(World.WORLDWIDTH-RADIUS)),(int)(Math.random()*(World.WORLDHEIGHT-RADIUS)));

		rotation = Math.random()*2*Math.PI;

		color=(int) Math.round(Math.random()*3);
		glowColor=(int)Math.random()*3;		

	}

	//Create Organism based on brain of another Organism
	//Used to spawn new Organism when another Organism dies
	public Organism(Brain b, int g){
		init(g);
		brain = new Brain(b);
	}

	public void tick(double[] in){
		age++;
	
		//Send inputs to the Brain and tick()
		double[] brainOut = brain.tick(in);
		/*Update information from output of the Brain*/
		
		rotation+=brainOut[0]*MAXROTATION;
		//Bound rotation to [0,2Pi)
		while(rotation>=Math.PI*2)
			rotation-=Math.PI*2;
		
		//Make Thruster Output positive
		brainOut[1]=(brainOut[1]+1)/2;
		speed=brainOut[1]*MAXMOVE;
		pos.translate((int)Math.round((Math.cos(rotation)*MAXMOVE*brainOut[1]+(Math.cos(rotation)))), (int)Math.round((Math.sin(rotation)*MAXMOVE*brainOut[1]+Math.sin(rotation))));
		//Bound position within world
		if(pos.x>World.WORLDWIDTH-RADIUS)
			pos.x=World.WORLDWIDTH-RADIUS;
		else if(pos.x<RADIUS)
			pos.x=RADIUS;
		if(pos.y>World.WORLDHEIGHT-RADIUS)
			pos.y=World.WORLDHEIGHT-RADIUS;
		else if(pos.y<RADIUS)
			pos.y=RADIUS;
		
		eye1Pos=pos;
		eye1Pos.translate((int)Math.round((Math.cos(rotation+(Math.PI/2))*RADIUS)), (int)Math.round((Math.sin(rotation+(Math.PI/2))*RADIUS)));
		eye2Pos=pos;
		eye2Pos.translate((int)Math.round((Math.cos(rotation-(Math.PI/2))*RADIUS)), (int)Math.round((Math.sin(rotation-(Math.PI/2))*RADIUS)));
		
		nose1Pos=eye1Pos;
		nose2Pos=eye2Pos;
		
		sheild1Pos=nose1Pos;
		sheild2Pos=nose2Pos;
		
		//Deduct Energy for thruster usage
		energy-=Math.sqrt(brainOut[1])*BASETHRUSTERCOST+(PASSIVEENERGYDRAIN);
		brainOut[2]=Math.round(brainOut[2]);
		color=(int) brainOut[2];
		
		glowColor=(int) Math.round(brainOut[3]);
		
		sheild=(brainOut[4]>0);
		//Deduct Energy for Sheild use
		if(sheild)
			energy-=SHEILDCOST;
		
		glow=(brainOut[5]>0);
		/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
		
		
		
	}

	
	
}
