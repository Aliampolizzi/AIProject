import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Graphics extends JFrame{
	JPanel jPanel;
	
	public Graphics(){
		init();
	}
	
	private void init() {
        
        jPanel = new Panel();
        jPanel.setBackground(new java.awt.Color(255, 255, 255));
        this.setContentPane(jPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        pack();
    }
	
    class Panel extends JPanel {

        Panel() {
            setPreferredSize(new Dimension(World.WORLDWIDTH,World.WORLDHEIGHT));
        }

        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            /*DEBUG*/
            if(World.VERBOSE>2)
            System.out.println("\nPainted\n");
            /*END-DEBUG*/
            

            /*Paint Here*/
            for(int i=0; i<Population.POPULATIONSIZE; i++){
            	
            	if(World.population.get(i).sheild){
            	g.setColor(new Color(100,100,100));
            	g.fillOval(World.population.get(i).pos.x-1, World.population.get(i).pos.y-1, World.ORGANISMRADIUS+2, World.ORGANISMRADIUS+2);
            	}
            	
            	if(World.population.get(i).color==-1)
            			g.setColor(new Color(255,0,0));
            	else if(World.population.get(i).color==0)
        			g.setColor(new Color(0,255,0));
            	else if(World.population.get(i).color==1)
        			g.setColor(new Color(0,0,255));
            	g.fillOval(World.population.get(i).pos.x, World.population.get(i).pos.y, World.ORGANISMRADIUS, World.ORGANISMRADIUS);
            }
            for(int i=0; i<World.food.size(); i++){
            	g.setColor(new Color(0,0,0));
            	g.fillOval(World.food.get(i).x, World.food.get(i).y, World.FOODRADIUS, World.FOODRADIUS);
            }
            /*~~~~~~~~~~*/
            
        }
    }
}
