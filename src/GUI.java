import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


//writen by Nicholas Sallis (n.sallis@hotmail.com)



public class GUI extends JFrame implements MouseListener, ActionListener{
	private static final long serialVersionUID = 1L;//not really necessary, but avoids warnings
	
	private
	int width, height, scale;//just holds the sizes returned from the optionPanes until they are used to construct the Viewport
	Viewport viewport;//extend viewport and overload paint function
	JPanel guiPanel;//holds GUI (buttons...)
	long startTime, endTime;//for framerate smoothing
	boolean editMode = true;//pauses the simulation so  organisms can be placed
	int framerate = 15;//framerate in frames per second
	
	//GUI components that will be added to guiPanel:
	JButton startStop;//start/pause the simulation 
	JButton screenShot;//takes an image of the simulation
	JTextField tAgents;//display the number of agents living
	JTextField tGens;//display the number of generations that have passed
	JTextField tFrameRate;//input a framerate
	JTextField tPath;
	
	
	//labels for the GUI
	JLabel lAgents;
	JLabel lGens;
	JLabel lFrameRate;
	JLabel lPath;
	JLabel info;
	
	public GUI(){
		
		//setup the modal dialog to input the width, height, and scale for the viewport:
		JPanel inputPanel = new JPanel();//panel that will hold the text fields for the dialog box
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		
		JLabel lWidth = new JLabel("Width: ");
		lWidth.setPreferredSize(new Dimension(140, 30));
		inputPanel.add(lWidth);
		
		JTextField tWidth = new JTextField("15");//default value of the width textfield
		tWidth.setPreferredSize(new Dimension(140, 30));
		inputPanel.add(tWidth);
		
		JLabel lHeight = new JLabel("Height: ");
		lHeight.setPreferredSize(new Dimension(140, 30));
		inputPanel.add(lHeight);
		
		JTextField tHeight = new JTextField("15");//default value of the height field
		tHeight.setPreferredSize(new Dimension(140, 30));
		inputPanel.add(tHeight);
		
		JLabel lScale = new JLabel("Scale: ");
		lScale.setPreferredSize(new Dimension(140, 30));
		inputPanel.add(lScale);
		
		JTextField tScale = new JTextField("30");//default value of the scale field		
		tScale.setPreferredSize(new Dimension(140, 30));
		inputPanel.add(tScale);
		
		String[] options = {"GO!"};//string array for the options on the dialog
			
			int selectedOption = JOptionPane.showOptionDialog(null, inputPanel, "Set Sizes", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);

			if(selectedOption == 0)
			{
				//set the values from the main window's "width", "height", and "scale" variables to the values in the dialog's textboxes
				this.width = Integer.valueOf(tWidth.getText());
			    this.height = Integer.valueOf(tHeight.getText());
			    this.scale = Integer.valueOf(tScale.getText());
			}
			//done with the dialog box
		
			//setup layout for window and it's viewport:
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		this.setBackground(Color.LIGHT_GRAY);
		viewport = new Viewport(width, height, scale);//size and scale of simulation (see Viewport constructor for more)15, 15, 30//TODO Width cannot be larger than height!
		viewport.setPreferredSize(new Dimension(viewport.panelW, viewport.panelH));
		viewport.setMinimumSize(new Dimension(viewport.panelW, viewport.panelH));
		viewport.addMouseListener(this);

		//setup the guiPanel (that holds the GUI)
		guiPanel = new JPanel();
		guiPanel.setPreferredSize(new Dimension(200, viewport.panelH));
		guiPanel.setMinimumSize(new Dimension(200, viewport.panelH));
		
		
		//setup the gui components:
		Dimension guiDimension = new Dimension(140, 30);
		
		lAgents = new JLabel("Number of live agents:");
		guiPanel.add(lAgents);
		
		tAgents = new JTextField();
		tAgents.setPreferredSize(guiDimension);
		tAgents.setDisabledTextColor(Color.BLACK);
		tAgents.setEnabled(false);//set the agents textfield to e un-editable
		guiPanel.add(tAgents);
		
		lGens = new JLabel("Number of generations: ");
		guiPanel.add(lGens);
		
		tGens = new JTextField();
		tGens.setPreferredSize(guiDimension);
		tGens.setDisabledTextColor(Color.BLACK);
		tGens.setEnabled(false);
		guiPanel.add(tGens);
		
		lFrameRate = new JLabel("Framerate: ");
		guiPanel.add(lFrameRate);
		
		
		tFrameRate = new JTextField("15");//set default frame rate to 15
		tFrameRate.setPreferredSize(guiDimension);
		guiPanel.add(tFrameRate);
		
		lPath = new JLabel("Absolute Path for screenshots: ");
		lPath.setPreferredSize(guiDimension);
		guiPanel.add(lPath);
		
		tPath = new JTextField("/home/silvertale/Pictures/ALifeImages/CLife_");//sets path for writing image out to file
		tPath.setPreferredSize(guiDimension);
		guiPanel.add(tPath);
		
		screenShot = new JButton("Screenshot...");
		screenShot.setPreferredSize(guiDimension);
		screenShot.addActionListener(this);
		guiPanel.add(screenShot);
		
		
		startStop = new JButton("start/stop");
		startStop.setPreferredSize(guiDimension);
		startStop.addActionListener(this);
		guiPanel.add(startStop);
		
		info = new JLabel("<html><center>Written by Nicholas Sallis <br>(n.sallis@hotmail.com)</center></html>");
		info.setPreferredSize(new Dimension(160, 70));
		guiPanel.add(info);
		
		
		//build window:
		this.add(viewport);
		this.add(guiPanel);
		this.setMinimumSize(new Dimension(viewport.panelW + 200, viewport.panelH));//add 200 to side for gui
		this.setSize(viewport.getViewportSize().width, viewport.getViewportSize().height + 30);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Conway's Game of Life");
		this.setVisible(true);
		
		
		while(true){//main  loop:
			startTime = System.currentTimeMillis() % 1000;
			viewport.repaint();
			if(!editMode){
			viewport.doWorldLogic();	
			}
			//check for changes in the textfields:
			tAgents.setText(String.valueOf(viewport.numberOfOrganisms));
			tGens.setText(String.valueOf(viewport.numberOfFrames));
			
			if(!tFrameRate.getText().isEmpty()){
				framerate = Integer.valueOf(tFrameRate.getText());
			}else{
				framerate = 5;//secretly set the framerate to 5, but don't show it. wait for the user to enter a new framerate. 
			}
			
			//smooth framerate:
			endTime = System.currentTimeMillis() % 1000;
			try {
				if((1000/framerate)-(endTime - startTime) > 0){//check for bad framerates dynamically(on the fly)
				Thread.sleep((1000/framerate) - (endTime - startTime));
				}else{
					//create a warning dialog that the framerate is too high:
					JOptionPane.showMessageDialog(this, "The framerate is too high to be handled by your computer! Please lower it and try again. ");
					tFrameRate.setText("15");//reset the framerate to a reasonable value
				}
			} catch (InterruptedException e) {
				System.out.println("There was a problem with the frame control!");
				e.printStackTrace();
			}
			}
		
	}
	
	
	public static void main(String args[]){
		new GUI();//start the application
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {//add or delete a cell when mouse is clicked
		//translate mouse position to viewport area and register that a new Organism has been spawned in the viewport.agents array
		int x = arg0.getXOnScreen() - this.getX();
		int y = arg0.getYOnScreen() - this.getY();//-30 to accommodate the ribbon
		
	
		if(!arg0.isControlDown()){//add a cell		
			viewport.agents[((int)Math.ceil(((double)x/(double)viewport.scale)))-1][((int)Math.ceil(((double)y/(double)viewport.scale)))-1].setAlive(true);
		}else if(arg0.isControlDown()){//delete a cell
			viewport.agents[((int)Math.ceil(((double)x/(double)viewport.scale)))-1][((int)Math.ceil(((double)y/(double)viewport.scale)))-1].setAlive(false);//undo a life
		}
		}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(arg0.getSource() == startStop){//switch to/from editmode
			editMode = !editMode;
			//equence();//TODO do sequence
		}else 
			
			if(arg0.getSource() == screenShot){//save the image from viewport to the disk (named with date)
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
			Date date = new Date();
			String sDate = dateFormat.format(date);
			
			try {
			BufferedImage bi = viewport.getImage();
			String path = tPath.getText() + sDate + ".png";//directory where the screenshot is saved
			System.out.println("PATH: " + path);
		    File outputfile = new File(path);//make output file string
		    ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
		    System.out.println("There was a problem saving the image!");
		}
		}
		
	}
	
	public void doSequence(){//goes through size^2 generations and then displays the results
		long start = System.currentTimeMillis();
		for(int i = 0; i < (Math.pow(2, Math.pow(viewport.arrayW, 2)))/100000; i++){//uses width as size (must have a square world for this!)
			viewport.doWorldLogic();	
			
		}
		long end = System.currentTimeMillis();
		System.out.println("it took: " + (end - start) + " ms for 1 gen");
		System.out.println("check: "+viewport.check);
		viewport.repaint();
		
	}

	
	
//methods that have to have a stub (because they belong to an abstract class or are abstract methods of implemented interfaces):
	

	@Override
	public void mouseEntered(MouseEvent arg0) {
		//Auto-generated method stub		
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		//Auto-generated method stub		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		//Auto-generated method stub		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		//Auto-generated method stub		
	}
}
	
