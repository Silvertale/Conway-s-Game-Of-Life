
public class Organism {
	protected int x, y;//should NEVER be changed. Only denotes the cells own address in the world
	private boolean alive;//holds if the cell is alive or not
	protected Viewport world;//the Viewport that is the parent to this Organism
	private boolean neighbors[];//starts at top left = 0, goes clockwise (top right = 2, bottom right = 4, bottom left = 6)
	
	/*
	 neighbors setup:
	 
	 0 1 2
	 7 x 3
	 6 5 4
	 
	 x=self
	 */
	
	
	public Organism(int xIn, int yIn, boolean live, Viewport parent){//ONLY for initializing! 
		
		//set needed variables
		alive = live;
		world = parent;
		x = xIn;
		y = yIn;
		neighbors = new boolean[8];//gets filled out each time doGeneration is run
	}
	
	public void doGeneration(){//do the logic loop	

		fillNeighbors();//get new list of Moore's neighbors
		
		//0, 1 die
		//2 stay alive
		//3 new birth
		//4+ die
		

		
		if(getNumberOfNeighbors() < 2 && this.getAlive() == true){
			world.newBuffer[x][y] = false;//kill cell
		}
		if(getNumberOfNeighbors() == 2 && this.getAlive() == true){
			world.newBuffer[x][y] = true;//stay alive
		}
		if(getNumberOfNeighbors() == 3 && this.getAlive() == true){
			world.newBuffer[x][y] = true;//stay alive
		}
		if(getNumberOfNeighbors() > 3 && this.getAlive() == true){
			world.newBuffer[x][y] = false;//kill cell
		}		
		if(getNumberOfNeighbors() == 3 && this.getAlive() == false){
			world.newBuffer[x][y] = true;//make dead cell alive(doesn't actually create a new object, just changes the alive variable!)
		}
		
	}
	
	private void fillNeighbors(){//fill neighbors with Moore's neighborhood
		neighbors[0] = world.getNorthWest(this.x,  this.y).getAlive();
		neighbors[1] = world.getNorth(this.x,  this.y).getAlive();
		neighbors[2] = world.getNorthEast(this.x,  this.y).getAlive();
		neighbors[3] = world.getEast(this.x,  this.y).getAlive();
		neighbors[4] = world.getSouthEast(this.x,  this.y).getAlive();
		neighbors[5] = world.getSouth(this.x,  this.y).getAlive();
		neighbors[6] = world.getSouthWest(this.x,  this.y).getAlive();
		neighbors[7] = world.getWest(this.x,  this.y).getAlive();		
	}
	
	private int getNumberOfNeighbors(){
		int alivecount = 0;
		for(int i = 0; i<neighbors.length; i++){
			
			if(neighbors[i]){//if the neighbor is alive:
				alivecount++;
			}
		}
		return alivecount;//return the number of neighbors the cell has
	}
	
	
	public void setAlive(boolean a){
		
		if(a && !alive){
			world.numberOfOrganisms++;//increment number of organisms (displayed in GUI)
		}else if(!a && alive){
			world.numberOfOrganisms--;//subtract from number of organisms, increment number of organisms (displayed in GUI)
		}
		alive = a;//set alive to true or false
	}
	
	public boolean getAlive(){//return if cell is alive or not
		return alive;
	}
	
	
	
	
	
}
