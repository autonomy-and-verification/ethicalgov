
import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class EthGovs extends Environment {

    public static final int GSize = 10; // grid size
    public static final int GOAL  = 16; // code in grid model
	public static final int HAZARD_RED = 8; // code in grid model
	public static final int HAZARD_ORANGE = 32; // code in grid model
	public static final int HAZARD_YELLOW = 64; // code in grid model
	

    static Logger logger = Logger.getLogger(EthGovs.class.getName()); // We can use this as output for our simulation

    private EGModel model; // In my project last year I had this as a separate file. Here the model is contained in the environment
    private EGView  view;
	
	public boolean blocked = false;
	
	public int step = 0;
	public int block = 0;
	public int hazards = 0;
	public int goals = 0;
	public int safety = 0;
	public int autonomy = 0;
	
	int randomWithRange(int min, int max)
    {
       int range = (max - min) + 1;     
       return (int)(Math.random() * range) + min;
    }
	
	// Locations generated randomly on the grid, to be used for the hazard locations
//	public Location hazLoc1 = new Location(randomWithRange(1, GSize-2), randomWithRange(1, GSize-2));
//	public Location hazLoc2 = new Location(randomWithRange(1, GSize-2), randomWithRange(1, GSize-2));
//	public Location hazLoc3 = new Location(randomWithRange(1, GSize-2), randomWithRange(1, GSize-2));
//	public Location hazLoc4 = new Location(randomWithRange(1, GSize-2), randomWithRange(1, GSize-2));
//	public Location hazLoc5 = new Location(randomWithRange(1, GSize-2), randomWithRange(1, GSize-2));
	public List<Location> hazardsLocations = new ArrayList<>();
//	public Location hazLoc1 = new Location(1, 3);
//	public Location hazLoc2 = new Location(2, 3);
//	public Location hazLoc3 = new Location(7, 6);
//	public Location hazLoc4 = new Location(1, 6);
//	public Location hazLoc5 = new Location(5, 4);
	
	// Locations generated randomly on the grid, to be used for the human's goals
//	public Location loc1 = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1));
//	public Location loc2 = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1));
//	public Location loc3 = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1));
//	public Location loc4 = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1));
//	public Location loc5 = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1));
	public Location loc1 = new Location(0, 8);
	public Location loc2 = new Location(9, 3);
	public Location loc3 = new Location(6, 3);
	public Location loc4 = new Location(4, 2);
	public Location loc5 = new Location(9, 5);
//	public Location loc1 = new Location(9, 8);
//	public Location loc2 = new Location(1, 9);
//	public Location loc3 = new Location(8, 8);
//	public Location loc4 = new Location(0, 1);
//	public Location loc5 = new Location(7, 9);
	
//	map 1
//	7,5 human
//	4,9 robot
//	0,8 9,3 6,3 4,2 9,5 goals
//	5,3 1,1 6,2 4,7 5,4 hazards
	
// map 2
//	0,8 human
//	7,2 robot
//	9,8 1,9 8,8 0,1 7,9 goals
//	1,3 2,3 7,6 1,6 5,4 hazards
	

    @Override
    public void init(String[] args) {
	  try {
		   File myFile = new File("results.csv");
	 
	   if (myFile.createNewFile()){
		   System.out.println("File is created!");
	   } else{
		   System.out.println("File already exists.");
	   }
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
	  
		hazardsLocations.add(new Location(5, 3));
		hazardsLocations.add(new Location(1, 1));
		hazardsLocations.add(new Location(6, 2));
		hazardsLocations.add(new Location(4, 7));
		hazardsLocations.add(new Location(5, 4));
    		 
        model = new EGModel();
        view  = new EGView(model);
        model.setView(view);
        updatePercepts();
    } //This method is required at the start of every environment. This method becomes before anything else and prevented me from user input last year.

    @Override
    public boolean executeAction(String ag, Structure action) { //This is for picking up on any literals contained in the agents files
        try {
			
            if (ag.contentEquals("human") && action.getFunctor().equals("unblock")) {
            	blocked = false;
            } else if (ag.contentEquals("human") && action.getFunctor().equals("move")) {
            	NumberTerm x = (NumberTerm) action.getTerm(0);
            	NumberTerm y = (NumberTerm) action.getTerm(1);
            	model.humanMove((int) x.solve(),(int) y.solve()); //Human can now move
            }  else if (ag.contentEquals("human") && action.getFunctor().equals("achieve")) {
            	goals++;
            } else if (ag.contentEquals("robot") && action.getFunctor().equals("move")) {
            	NumberTerm x = (NumberTerm) action.getTerm(0);
            	NumberTerm y = (NumberTerm) action.getTerm(1);
            	model.robotMove((int) x.solve(),(int) y.solve()); //Human can now move
            } else if (ag.contentEquals("robot") && action.getFunctor().equals("block")) {
            	model.robotBlock();
            } else if (ag.contentEquals("arbiter") && action.getFunctor().equals("safety")) {
            	safety++;
            } else if (ag.contentEquals("arbiter") && action.getFunctor().equals("autonomy")) {
            	autonomy++;
            }
            
            if (ag.contentEquals("human") && (action.getFunctor().equals("move") || action.getFunctor().equals("skip") || action.getFunctor().equals("achieve") )) {
        		updatePercepts(); //Update percepts ready for the next reasoning cycle
                try {
                    Thread.sleep(400); // wait o.2 seconds before next reasoning cycle
                } catch (Exception e) {}
                informAgsEnvironmentChanged();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

        return true;
    }
    
    /** creates the agents perception based on the EGModel */
    void updatePercepts() { // Method for updating the agent's beliefs - Necessary method
        clearPercepts();

        Location humanLoc = model.getAgPos(0); // Locations of agents positions
		Location robotLoc = model.getAgPos(1);
		
		// Positions of human, and robot, as percepts
        Literal pos1 = Literal.parseLiteral("pos(human," + humanLoc.x + "," + humanLoc.y + ")"); // Belief for positions will be saved in this format
		Literal pos2 = Literal.parseLiteral("pos(robot," + robotLoc.x + "," + robotLoc.y + ")");
		
		// Locations of hazards as percepts
		Set<Location> set = new HashSet<Location>();
		for(int i = 0; i < EthGovs.GSize; i++) {
			for(int j = 0; j < EthGovs.GSize; j++) {
				for(Location center : hazardsLocations) {
					Location loc = new Location(i,j);
					if(!set.contains(loc) && center.distanceManhattan(loc) < 3) {
						addPercept(Literal.parseLiteral("pos(hazard," + loc.x + "," + loc.y + "," + (center.distanceManhattan(loc) == 0 ? "red" : (center.distanceManhattan(loc) == 1 ? "orange" : "yellow")) + ")"));
						set.add(loc);
					}
				}
			}
		}
		
		// Locations of goals as percepts
		Literal gloc1 = Literal.parseLiteral("goal(1," + loc1.x + "," + loc1.y + ")");
		Literal gloc2 = Literal.parseLiteral("goal(2," + loc2.x + "," + loc2.y + ")");
		Literal gloc3 = Literal.parseLiteral("goal(3," + loc3.x + "," + loc3.y + ")");
		Literal gloc4 = Literal.parseLiteral("goal(4," + loc4.x + "," + loc4.y + ")");
		Literal gloc5 = Literal.parseLiteral("goal(5," + loc5.x + "," + loc5.y + ")");
		
		Literal newstep = Literal.parseLiteral("new_step"); //(" + step + ")");
		step++;
		logger.info("@@@@ New step "+step+" @@@@");
		
		if (step == 201) {
			Literal stop = Literal.parseLiteral("stop");
			File file = new File("results.csv");
			try {
				FileWriter fr = new FileWriter(file, true);
				BufferedWriter br = new BufferedWriter(fr);
				PrintWriter pr = new PrintWriter(br);
				pr.println(block+","+hazards+","+goals+","+safety+","+autonomy);
				pr.close();
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			logger.info("@@@@@@@@@@@@@ END @@@@@@@@@@@@@ ");
//			logger.info("Blocks "+block);
//			logger.info("Hazards "+hazards);
//			logger.info("Goals "+goals);
//            try {
//                Thread.sleep(200000); // wait o.2 seconds before next reasoning cycle
//            } catch (Exception e) {}
			addPercept("arbiter",stop);
		} else {
			// All percepts added
	        addPercept(pos1);
			addPercept(pos2);
			addPercept(gloc1);
			addPercept(gloc2);
			addPercept(gloc3);
			addPercept(gloc4);
			addPercept(gloc5);
			addPercept("safetygov",newstep);
			addPercept("autonomygov",newstep);
		}
		
    }
	
    class EGModel extends GridWorldModel {

        Random random = new Random(System.currentTimeMillis()); // Random number between 0 and System.currentTimeMillis

        private EGModel() {
            super(GSize, GSize, 2); // From GridWorldModel
            File file = new File("results.csv");
            // initial location of agents
            try {
//				Location humanLoc = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1)); //Random locations on the board
//				Location robotLoc = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1)); //Random locations on the board
				Location humanLoc = new Location(7, 5);
				Location robotLoc = new Location(4, 9);
//				Location humanLoc = new Location(0, 8);
//				Location robotLoc = new Location(7, 2);
//				try {
//					FileWriter fr = new FileWriter(file, true);
//					BufferedWriter br = new BufferedWriter(fr);
//					PrintWriter pr = new PrintWriter(br);
//					pr.println(humanLoc);
//					pr.println(robotLoc);
//					pr.close();
//					br.close();
//					fr.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				setAgPos(0, humanLoc);
				setAgPos(1, robotLoc);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
			
//			try {
//				FileWriter fr = new FileWriter(file, true);
//				BufferedWriter br = new BufferedWriter(fr);
//				PrintWriter pr = new PrintWriter(br);
//				pr.println(loc1+" "+loc2+" "+loc3+" "+loc4+" "+loc5);
//				pr.println(hazLoc1+" "+hazLoc2+" "+hazLoc3+" "+hazLoc4+" "+hazLoc5);
//				pr.close();
//				br.close();
//				fr.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

            // initial location of Goals.
            add(GOAL, loc1);
            add(GOAL, loc2);
            add(GOAL, loc3);
            add(GOAL, loc4);
            add(GOAL, loc5);
			
			//initial locations of hazards
			
			addHazards();
        }
		
		void addHazards() {
			Set<Location> set = new HashSet<Location>();
			for(Location center : hazardsLocations) {
				add(HAZARD_RED, center);
				set.add(center);
			}
			
			for(int i = 0; i < EthGovs.GSize; i++) {
				for(int j = 0; j < EthGovs.GSize; j++) {
					for(Location center : hazardsLocations) {
						Location loc = new Location(i,j);
						if(!set.contains(loc) && center.distanceManhattan(loc) == 1) {
							add(HAZARD_ORANGE, loc);
							set.add(loc);
							break;
						}
					}
				}
			}
			
			for(int i = 0; i < EthGovs.GSize; i++) {
				for(int j = 0; j < EthGovs.GSize; j++) {
					for(Location center : hazardsLocations) {
						Location loc = new Location(i,j);
						if(!set.contains(loc) && center.distanceManhattan(loc) == 2) {
							add(HAZARD_YELLOW, loc);
							set.add(loc);
							break;
						}
					}
				}
			}
			
		}
		
		
		void robotBlock() {
			Location robotLoc = getAgPos(1);
			blocked = true;
			block++;
			setAgPos(1,robotLoc);
			System.out.println("Robot is blocking human");
		}
		
		void robotMove(int x, int y) throws Exception {
			
			Location robotLoc = getAgPos(1);
			
			if (robotLoc.x == x && robotLoc.y == y) {
				System.out.println("Robot stayed at " + robotLoc.x + "," + robotLoc.y);
			}
			else {
				System.out.println("Robot moved to " + x + "," + y); //Show in the console where the robot moved to
			}
			
			robotLoc.x = x;
			robotLoc.y = y;
			
			// Set the robots position to whatever the x and y values were changed to
			setAgPos(1,robotLoc);

        }
		
		void humanMove(int x, int y) { // This will be the human's calculated movement based on the goal tiles
			Location humanLoc = getAgPos(0);
			Location robotLoc = getAgPos(1);
			humanLoc.x = x;
			humanLoc.y = y;
			for(Location h : hazardsLocations) {
				if(h.x == humanLoc.x && h.y == humanLoc.y) {
					hazards++;	
					logger.severe("HUMAN STEPPED ON HAZARD");
					break;
				}
			}
			
			setAgPos(0,humanLoc);
			setAgPos(1,robotLoc);
			//Formally change the humans location, and show in console where he moved to
			System.out.println("Human moved to " + humanLoc.x + "," + humanLoc.y);
		}
		
		boolean isInRadius(Location input, Location target) { //This method is used to test if the input is 1 square away from the target
			if((input.x == target.x-1) && (input.y == target.y+1) ||
			   (input.x == target.x) && (input.y == target.y+1) ||
			   (input.x == target.x+1) && (input.y == target.y+1) ||
			   (input.x == target.x-1) && (input.y == target.y) ||
			   (input.x == target.x+1) && (input.y == target.y) ||
			   (input.x == target.x-1) && (input.y == target.y-1) ||
			   (input.x == target.x) && (input.y == target.y-1) ||
			   (input.x == target.x+1) && (input.y == target.y-1)) {
				   return true;
			   } else {
				   return false;
			   }
		} 
    }

    class EGView extends GridWorldView {

        /**
		 * 
		 */
		private static final long serialVersionUID = -6292760541729112095L;

		public EGView(EGModel model) {
            super(model, "Ethical Governor World", 600);  // Loads the gridworld with the current model as defined before, with window size 600
            defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
            setVisible(true);
            repaint();
        }

        /** draw application objects */
        @Override
        public void draw(Graphics g, int x, int y, int object) { // Uses the methods to draw the defined objects i.e. drawGarb()
            switch (object) {
				case EthGovs.GOAL:
					drawGoal(g, x, y);
//					logger.info("Goal pos "+g+" "+x+" "+y);
					break; //Going to have to modify this bit for the hazard
				case EthGovs.HAZARD_RED:
				case EthGovs.HAZARD_ORANGE:
				case EthGovs.HAZARD_YELLOW:
					drawHazard(g, x, y, object);
//					logger.info("Hazard pos "+g+" "+x+" "+y);
					break;
            } 
        }

        @Override
        public void drawAgent(Graphics g, int x, int y, Color c, int id) { // Method for drawing the agent. This is all purely for visual purposes
			String label;
			if(id == 0) {
				if (blocked) {
					g.setColor(Color.black);
				} else {
					g.setColor(Color.pink);
				}
				label = "H";
			} else {
				g.setColor(Color.blue);				
				label = "R";
			}
			g.fillOval(x * cellSizeW + 10, y * cellSizeH + 10, cellSizeW-20, cellSizeH-20);
            g.setColor(Color.white);
            super.drawString(g, x, y, defaultFont, label);
            repaint(0);
        }

		public void drawGoal(Graphics g, int x, int y) { // To be used in the draw() method.
            g.setColor(Color.green);
			//g.fillOval(x * cellSizeW + 10, y * cellSizeH + 10, cellSizeW-20, cellSizeH-20);
			g.drawPolygon(new int[] {x * cellSizeW + 5, x * cellSizeW + ((int)(cellSizeW/2)), x * cellSizeW + cellSizeW - 5}, new int[] {y * cellSizeH + (cellSizeH / 4), y * cellSizeH  + (cellSizeH - 5), y * cellSizeH + (cellSizeH / 4)}, 3);
			g.setColor(Color.black);
            drawString(g, x, y, defaultFont, "G");
        }
		
		public void drawHazard(Graphics g, int x, int y, int lvl) { // To be used in the draw() method.
			System.out.println(lvl);
            switch(lvl) {
				case HAZARD_RED:
					g.setColor(new Color(1f, 0f, 0f, 0.5f));	
					break;
				case HAZARD_ORANGE:
					g.setColor(Color.orange);
					break;
				case HAZARD_YELLOW:
					g.setColor(Color.yellow);
			}
			for(int i = 0; i < 4; i++) {
				g.drawRect(x * cellSizeW + i, y * cellSizeH + i, cellSizeW - (i * 2), cellSizeH - (i * 2));
			}
			//g.drawRect(x * cellSizeW + 4, y * cellSizeH + 3, cellSizeW - 3, cellSizeH - 3);
			//g.drawRect(x * cellSizeW + 3, y * cellSizeH + 2, cellSizeW - 2, cellSizeH - 2);
            //drawString(g, x, y, defaultFont, "H");
        }
    }
}

