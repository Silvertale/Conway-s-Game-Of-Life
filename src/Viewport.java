import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;



public class Viewport extends JPanel{
	private static final long serialVersionUID = 1L;//not really necessary, but avoids warning messages
	
	int panelW, panelH;//width and height of the Viewport(in pixels)
	int arrayW, arrayH;//width and height of the array (that the Organisms are held in)
	int scale;//size of each Organism on the screen(in pixels)
	int numberOfOrganisms = 0;//number of organisms alive (displayed in the GUI)
	int numberOfFrames = 0;//number of generations (times goWorldLogic has been run, displayed in GUI)
	BufferedImage image;//image that the simulation is drawn to
	Organism agents[][];//array of Organisms(main array that has the results and is drawn to image)
	boolean newBuffer[][];//buffer that changes are written into before writing to agents[][]
	int check = 0;//TODO remove this!
	
	
	public Viewport(int width, int height, int s){
		//set needed variables from constructor arguments:
		scale = s;
		arrayW = width;
		arrayH = height;
		panelW = width*scale;
		panelH = height*scale;
		
		this.setSize(panelW, panelH);
		agents = new Organism[arrayW][arrayH];//create an array of size width and height respective to scale
		newBuffer = new boolean[arrayW][arrayH];
		image = new BufferedImage(panelW, panelH, BufferedImage.TYPE_INT_ARGB);
		
		//fill the array with Organisms (set to dead)
		for(int x=0; x<arrayW; x++){
			for(int y=0; y<arrayH; y++){
				agents[x][y] = new Organism(x, y, false, this);//set all cells to be dead and give them their x and y coordinates
			}
		}
	}
	
	
	public void paint(Graphics g){//paint the simulation to the screen
		
		//get the graphics context of the image:
		Graphics imageG = image.getGraphics();
		imageG.setColor(Color.GRAY);//fill the image with gray
		imageG.fillRect(0, 0, panelW, panelH);
		//loop through the world's array:
		for(int x=0; x<arrayW; x++){//loop through each cell and paint it white or green (gaps between the cells make the grid)
			for(int y=0; y<arrayH; y++){
				
				//conditional coloring:
				if(agents[x][y].getAlive()){//paint live cells green
					imageG.setColor(new Color(0, 255, 0));//live organisms are green
					imageG.fillRect((x * scale+1), (y * scale+1), scale-1, scale-1);//+1 and -1 to accommodate for grid
				}else if(!agents[x][y].getAlive()){//paint dead cells white
					imageG.setColor(new Color(255, 255, 255));//dead organisms are white
					imageG.fillRect(x * scale+1, y * scale+1, scale-1, scale-1);//+1 and -1 to accommodate for grid
				}
			}
		}
		g.drawImage(image, 0, 0, panelW, panelH, null);//draw the image to the viewport (JPanel extension)
	}
	
		
	public void doWorldLogic(){//do the logic method for each Organism and write the results to the newBuffer:
		for(int x=0; x<arrayW; x++){
			for(int y=0; y<arrayH; y++){				
					agents[x][y].doGeneration();//do the logic move for that cell
			}
		}
		copyBufferToWorld();//copy newBuffer's results to the agents array (original array)
		numberOfFrames++;//Increment the number of generations passed
		check++;
	}
	
	public void copyBufferToWorld(){//copy the values from newBuffer to agent's alive variables 
		for(int x = 0; x < arrayW; x++){
			for(int y = 0; y < arrayH; y++){
				agents[x][y].setAlive(newBuffer[x][y]);
			}
		}
	}
	
	
	public BufferedImage getImage(){
		return image;
	}
	
	
	public Dimension getViewportSize(){
		return new Dimension(panelW, panelH);
	}
	
	
	//methods to get neighbors: TODO Error if width is greater than height in array size
	public Organism getWest(int x, int y){
		if(x > 0){
			return agents[x-1][y];
		}else if(x == 0){
			return agents[agents.length -1][y];
		}else {
			return agents[agents.length+x][y];//x will be negative
		}
	}
	
	public Organism getEast(int x, int y){
		if(x < agents.length-1){
		return agents[x+1][y];
		}else{
			return agents[x-(agents.length-1)][y];//was 0
		}
	}
	
	public Organism getNorth(int x, int y){
		if(y > 0){
		return agents[x][y-1];
		}else if(y == 0){
			return agents[x][agents.length - 1];
		}else {
			return agents[x][agents.length + y];//return wrapped around position
		}
	}
	
	public Organism getSouth(int x, int y){
		if(y < agents.length-1){
		return agents[x][y+1];
		}else{
			return agents[x][y-(agents.length-1)];//was 0
		}
	}
	
	public Organism getNorthWest(int x, int y){	
		Organism temp = getNorth(x, y);
		return getWest(temp.x, temp.y);//return west from north's perspective
	}
	
	public Organism getNorthEast(int x, int y){
		
		Organism temp = getNorth(x, y);
		
		return getEast(temp.x, temp.y);
	}
	
	public Organism getSouthWest(int x, int y){
		
		Organism temp = getSouth(x, y);

		return getWest(temp.x, temp.y);
	}
	
	public Organism getSouthEast(int x, int y){
		
		Organism temp = getSouth(x, y);
		return getEast(temp.x, temp.y);
	}	
	
}



	


	
