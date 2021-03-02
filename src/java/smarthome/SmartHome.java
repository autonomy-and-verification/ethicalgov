package smarthome;

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

public class SmartHome extends Environment {

    static Logger logger = Logger.getLogger(SmartHome.class.getName()); // We can use this as output for our simulation

	
	public boolean blocked = false;
	
	public int step = 0;
	public int warning = 0;
	public int red = 0;
	public int orange = 0;
	public int yellow = 0;
	public int goals = 0;
	public int safety = 0;
	public int autonomy = 0;
	public int maxprox = 0;
	public List<Integer> steps = new ArrayList<Integer>();
	
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
	public List<Location> hazardsLocations = new ArrayList<Location>();
	public List<Location> redrad = new ArrayList<Location>();
	public List<Location> orangerad = new ArrayList<Location>();
	public List<Location> yellowrad = new ArrayList<Location>();
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
	public Location loc1;
	public Location loc2;
	public Location loc3;
	public Location loc4;
	public Location loc5;
//	public Location loc1 = new Location(9, 8);
//	public Location loc2 = new Location(1, 9);
//	public Location loc3 = new Location(8, 8);
//	public Location loc4 = new Location(0, 1);
//	public Location loc5 = new Location(7, 9);
	
	public Location humanLoc;
	public Location robotLoc;
	
	public File file = new File("results.csv");
	
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
	  
	  	map1();
//	  	map2();
	  

    		 
        updatePercepts();
    } //This method is required at the start of every environment. This method becomes before anything else and prevented me from user input last year.

    private void map1() {
		hazardsLocations.add(new Location(2, 3));
		hazardsLocations.add(new Location(4, 7));
		
		loc1 = new Location(0, 8);
		loc2 = new Location(0, 5);
		loc3 = new Location(6, 2);
		loc4 = new Location(9, 3);
		loc5 = new Location(8, 7);
		
		humanLoc = new Location(7, 5);
		robotLoc = new Location(4, 9);
}
    
    private void map2() {
		hazardsLocations.add(new Location(2, 2));
		hazardsLocations.add(new Location(7, 2));
		hazardsLocations.add(new Location(2, 7));
		hazardsLocations.add(new Location(7, 7));
		
		loc1 = new Location(4, 4);
		loc2 = new Location(0, 0);
		loc3 = new Location(0, 9);
		loc4 = new Location(9, 0);
		loc5 = new Location(9, 9);
		
		humanLoc = new Location(4, 5);
		robotLoc = new Location(5, 5);
}

	@Override
    public boolean executeAction(String ag, Structure action) { //This is for picking up on any literals contained in the agents files
        try {
			
            if (ag.contentEquals("human") && action.getFunctor().equals("unblock")) {
            	blocked = false;
            } else if (ag.contentEquals("human") && action.getFunctor().equals("move")) {
            	NumberTerm x = (NumberTerm) action.getTerm(0);
            	NumberTerm y = (NumberTerm) action.getTerm(1);
            	int newX = 0;
            	int newY = 0;
            	if ((int) x.solve() >= 11) {
            		newX = (int) x.solve() - 1;
            	}
            	else if ((int) x.solve() < 0) {
            		newX = (int) x.solve() + 1;
            	}
            	else {
            		newX = (int) x.solve();
            	}
            	if ((int) y.solve() >= 11) {
            		newY = (int) y.solve() - 1;
            	}
            	else if ((int) y.solve() < 0) {
            		newY = (int) y.solve() + 1;
            	}
            	else {
            		newY = (int) y.solve();
            	}
            }  else if (ag.contentEquals("human") && action.getFunctor().equals("achieve")) {
            	goals++;
            	NumberTerm s = (NumberTerm) action.getTerm(0);
            	steps.add((int) s.solve());
            }  else if (action.getFunctor().equals("updatemaxprox")) {
            	NumberTerm s = (NumberTerm) action.getTerm(0);
            	maxprox = (int) s.solve();
            } else if (ag.contentEquals("robot") && action.getFunctor().equals("move")) {
            	blocked = false;
            	NumberTerm x = (NumberTerm) action.getTerm(0);
            	NumberTerm y = (NumberTerm) action.getTerm(1);
            	int newX = 0;
            	int newY = 0;
            	if ((int) x.solve() >= 11) {
            		newX = (int) x.solve() - 1;
            	}
            	else if ((int) x.solve() < 0) {
            		newX = (int) x.solve() + 1;
            	}
            	else {
            		newX = (int) x.solve();
            	}
            	if ((int) y.solve() >= 11) {
            		newY = (int) y.solve() - 1;
            	}
            	else if ((int) y.solve() < 0) {
            		newY = (int) y.solve() + 1;
            	}
            	else {
            		newY = (int) y.solve();
            	}
            } else if (ag.contentEquals("robot") && action.getFunctor().equals("safety")) {
            	safety++;
            } else if (ag.contentEquals("robot") && action.getFunctor().equals("autonomy")) {
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

		
		
		
		// Positions of human, and robot, as percepts
        Literal pos1 = Literal.parseLiteral("pos(human," + humanLoc.x + "," + humanLoc.y + ")"); // Belief for positions will be saved in this format
		Literal pos2 = Literal.parseLiteral("pos(robot," + robotLoc.x + "," + robotLoc.y + ")");
		
		
		// Locations of goals as percepts
		Literal gloc1 = Literal.parseLiteral("goal(1," + loc1.x + "," + loc1.y + ")");
		Literal gloc2 = Literal.parseLiteral("goal(2," + loc2.x + "," + loc2.y + ")");
		Literal gloc3 = Literal.parseLiteral("goal(3," + loc3.x + "," + loc3.y + ")");
		Literal gloc4 = Literal.parseLiteral("goal(4," + loc4.x + "," + loc4.y + ")");
		Literal gloc5 = Literal.parseLiteral("goal(5," + loc5.x + "," + loc5.y + ")");
		
		
		Literal newstep = Literal.parseLiteral("new_step"); //(" + step + ")");
		Literal oldstep = Literal.parseLiteral("new_step"); //(" + step + ")");
		oldstep.addTerm(new NumberTermImpl(step));
		step++;
		newstep.addTerm(new NumberTermImpl(step));
		logger.info("@@@@ New step "+step+" @@@@");
		
		if (step == 201) {
			Literal stop = Literal.parseLiteral("stop");
			File file = new File("results.csv");
			try {
				FileWriter fr = new FileWriter(file, true);
				BufferedWriter br = new BufferedWriter(fr);
				PrintWriter pr = new PrintWriter(br);
				pr.println(warning+","+red+","+orange+","+yellow+","+goals+","+maxprox+","+safety+","+autonomy+","+steps);
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
			addPercept("arbiter_agent",stop);
		} else {
			// All percepts added
	        addPercept(pos1);
			addPercept(pos2);
			addPercept(gloc1);
			addPercept(gloc2);
			addPercept(gloc3);
			addPercept(gloc4);
			addPercept(gloc5);
			removePercept("robot",oldstep);
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			addPercept("robot",newstep);
		}
		
    }
	

}

