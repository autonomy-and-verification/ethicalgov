
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

public class EthGovs extends Environment {

    public static final int GSize = 10; // grid size
    public static final int GOAL  = 16; // code in grid model
	public static final int HAZARD = 8; // code in grid model

    //AGENT LITERALS
	//The explanations of these plans may be seen in the executeAction() method
	public static final Term    p1  = Literal.parseLiteral("plan1");
	public static final Term    p2  = Literal.parseLiteral("plan2");
	public static final Term    p3  = Literal.parseLiteral("plan3");
	public static final Term    p4  = Literal.parseLiteral("plan4");
	public static final Term    p5  = Literal.parseLiteral("plan5");
	public static final Term    p6  = Literal.parseLiteral("plan6");
	public static final Term    p7  = Literal.parseLiteral("plan7");
	public static final Term    p8  = Literal.parseLiteral("plan8");
	public static final Term    p9  = Literal.parseLiteral("plan9");
	public static final Term    p10 = Literal.parseLiteral("plan10");
	public static final Term    p11 = Literal.parseLiteral("plan11");
	public static final Term    p12 = Literal.parseLiteral("plan12");
	
    static Logger logger = Logger.getLogger(EthGovs.class.getName()); // We can use this as output for our simulation

    private EGModel model; // In my project last year I had this as a separate file. Here the model is contained in the environment
    private EGView  view;
	
	public String safetyChoice;
	public String autonomyChoice;
	
	public boolean blocked = false;
	
	public int proximityCount = 0;
	public int currentGoalStep = 0;
	
	int randomWithRange(int min, int max)
    {
       int range = (max - min) + 1;     
       return (int)(Math.random() * range) + min;
    }
	
	// Locations generated randomly on the grid, to be used for the hazard locations
	public Location hazLoc1 = new Location(randomWithRange(1, GSize-2), randomWithRange(1, GSize-2));
	public Location hazLoc2 = new Location(randomWithRange(1, GSize-2), randomWithRange(1, GSize-2));
	public Location hazLoc3 = new Location(randomWithRange(1, GSize-2), randomWithRange(1, GSize-2));
	public Location hazLoc4 = new Location(randomWithRange(1, GSize-2), randomWithRange(1, GSize-2));
	public Location hazLoc5 = new Location(randomWithRange(1, GSize-2), randomWithRange(1, GSize-2));
	
	// Locations generated randomly on the grid, to be used for the human's goals
	public Location loc1 = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1));
	public Location loc2 = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1));
	public Location loc3 = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1));
	public Location loc4 = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1));
	public Location loc5 = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1));
	
	public Location currentGoal;
	public Location testingLoc;
	public Location robotsNextMove;

    @Override
    public void init(String[] args) {
        model = new EGModel();
        view  = new EGView(model);
        model.setView(view);
        updatePercepts();
    } //This method is required at the start of every environment. This method becomes before anything else and prevented me from user input last year.

    @Override
    public boolean executeAction(String ag, Structure action) { //This is for picking up on any literals contained in the agents files
        try {
			
            if (action.equals(p1)) { //Not near/at human, human in Danger, proximity count < 3.
				safetyChoice = "11 3"; //Safety says Move Towards human, with a score of 3
				autonomyChoice = "21 1"; // Autonomy says Stay Put, with a score of 1
            } else if (action.equals(p2)) { //Not near/at human, human in danger, proximity count > 3
				safetyChoice = "11 3"; //Safety says Move Towards human, with a score of 3
				autonomyChoice = "21 3"; //Autonomy says Stay Put, with a score of 2
            } else if (action.equals(p3)) { //Not near/at human, human not in danger, proximity count < 3.
				safetyChoice = "11 2"; //Safety says Move Towards human, with a score of 2
				autonomyChoice = "21 1"; //Autonomy says Stay Put, with a score of 2
            } else if (action.equals(p4)) { //Not near/at human, human not in danger, proximity count > 3.
				safetyChoice = "11 1"; //Safety says Move Towards human with a score 1
				autonomyChoice = "21 3"; //Autonomy says Stay Put, with a score of 3
            } else if (action.equals(p5)) { //Near human, human in danger, proximity count < 3
                safetyChoice = "11 3"; //Safety says Move Towards human with a score of 3
				autonomyChoice = "22 1"; //Autonomy says Move Away with a score of 1
            } else if (action.equals(p6)) { //Near human, human in danger, proximity count > 3
                safetyChoice = "11 3"; //Safety says Move Towards, with a score of 3
				autonomyChoice = "22 3"; //Autonomy says Move Away, with a score of 3
            } else if (action.equals(p7)) { //Near human, human not in danger, proximity count < 3
                safetyChoice = "12 2"; // Safety says Stay Put, with a score of 2
				autonomyChoice = "22 2"; //Autonomy says Move Away, with a score of 2
            } else if (action.equals(p8)) { //Near human, human not in danger, proximity count > 3
                safetyChoice = "12 2"; //Safety says Stay Put, with a score of 2
				autonomyChoice = "22 3"; //Autonomy says Move Away, with a score of 3
            } else if (action.equals(p9)) { //At human, human in danger, proximity count < 3
                safetyChoice = "13 3"; //Safety says prevent human, with a score of 3
				autonomyChoice = "22 1"; //Autonomy says Move Away, with a score of 1
            } else if (action.equals(p10)) { //At human, human in danger, proximity count > 3
                safetyChoice = "13 3"; //Safety says prevent human, with a score of 3
				autonomyChoice = "22 2"; //Autonomy says Move Away, with a score of 3
            } else if (action.equals(p11)) { //At human, human not in danger, proximity count < 3
                safetyChoice = "12 1"; //Safety says Stay Put, with a score of 1
				autonomyChoice = "22 2"; //Autonomy says Move Away, with a score of 2
            } else if (action.equals(p12)) { //At human, human not in danger, proximity count > 3
                safetyChoice = "12 1"; //Safety says Stay Put, with a score of 1
				autonomyChoice = "22 3"; //Autonomy says Move Away, with a score of 3
            } else if (action.getFunctor().equals("unblock")) {
            	blocked = false;
            } else if (action.getFunctor().equals("move")) {
            	NumberTerm x = (NumberTerm) action.getTerm(0);
            	NumberTerm y = (NumberTerm) action.getTerm(1);
            	model.humanMove((int) x.solve(),(int) y.solve()); //Human can now move
            }
            if (action.getFunctor().equals("move") || action.getFunctor().equals("skip")) {
            	System.out.println("Proximity Score " + proximityCount);
        		updatePercepts(); //Update percepts ready for the next reasoning cycle
                try {
                    Thread.sleep(400); // wait o.2 seconds before next reasoning cycle
                } catch (Exception e) {}
                informAgsEnvironmentChanged();
            }
			if(ag.contentEquals("governors") && safetyChoice != null && autonomyChoice != null){ //Once both choices have been made
				logger.info(ag + " Chose " + action); //Show in the log file what plan the governors agent chose
				model.arbiterChoice();
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
		Literal pos2 = Literal.parseLiteral("pos(governors," + robotLoc.x + "," + robotLoc.y + ")");
		
		// Proximity Count as a percept
		Literal proxCount = Literal.parseLiteral("proximityScore(" + proximityCount + ")");
		
		// Locations of hazards as percepts
		Literal haz1 = Literal.parseLiteral("pos(hazard," + hazLoc1.x + "," + hazLoc1.y + ")");
		Literal haz2 = Literal.parseLiteral("pos(hazard," + hazLoc2.x + "," + hazLoc2.y + ")");
		Literal haz3 = Literal.parseLiteral("pos(hazard," + hazLoc3.x + "," + hazLoc3.y + ")");
		Literal haz4 = Literal.parseLiteral("pos(hazard," + hazLoc4.x + "," + hazLoc4.y + ")");
		Literal haz5 = Literal.parseLiteral("pos(hazard," + hazLoc5.x + "," + hazLoc5.y + ")");
		
		// Locations of goals as percepts
		Literal gloc1 = Literal.parseLiteral("goal(1," + loc1.x + "," + loc1.y + ")");
		Literal gloc2 = Literal.parseLiteral("goal(2," + loc2.x + "," + loc2.y + ")");
		Literal gloc3 = Literal.parseLiteral("goal(3," + loc3.x + "," + loc3.y + ")");
		Literal gloc4 = Literal.parseLiteral("goal(4," + loc4.x + "," + loc4.y + ")");
		Literal gloc5 = Literal.parseLiteral("goal(5," + loc5.x + "," + loc5.y + ")");
		
		Literal block = Literal.parseLiteral("blocked(" + blocked + ")");
		
		Literal step = Literal.parseLiteral("new_step");
		
		// All percepts added
        addPercept(pos1);
		addPercept(pos2);
		addPercept(haz1);
		addPercept(haz2);
		addPercept(haz3);
		addPercept(haz4);
		addPercept(haz5);
		addPercept(gloc1);
		addPercept(gloc2);
		addPercept(gloc3);
		addPercept(gloc4);
		addPercept(gloc5);
		addPercept(proxCount);
		addPercept("governors",step);
		addPercept("human",block);
		
    }
	
    class EGModel extends GridWorldModel {

        Random random = new Random(System.currentTimeMillis()); // Random number between 0 and System.currentTimeMillis

        private EGModel() {
            super(GSize, GSize, 2); // From GridWorldModel

            // initial location of agents
            try {
				Location humanLoc = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1)); //Random locations on the board
				Location robotLoc = new Location(randomWithRange(0, GSize-1), randomWithRange(0, GSize-1)); //Random locations on the board
				setAgPos(0, humanLoc);
				setAgPos(1, robotLoc);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // initial location of Goals.
            add(GOAL, loc1);
            add(GOAL, loc2);
            add(GOAL, loc3);
            add(GOAL, loc4);
            add(GOAL, loc5);
			
			//initial locations of hazards
			add(HAZARD, hazLoc1);
            add(HAZARD, hazLoc2);
            add(HAZARD, hazLoc3);
            add(HAZARD, hazLoc4);
            add(HAZARD, hazLoc5);
        }
		
		void arbiterChoice()  {
	
			//Submethod for working out safetys score
			Location robotLoc = getAgPos(1);
			Location humanLoc = getAgPos(0);
			if((robotLoc.x == humanLoc.x && robotLoc.y == humanLoc.y) || //This conditional sees if the robot is near the human
				(robotLoc.x == humanLoc.x - 1 && robotLoc.y == humanLoc.y + 1) ||
				(robotLoc.x == humanLoc.x && robotLoc.y == humanLoc.y + 1) ||
				(robotLoc.x == humanLoc.x + 1 && robotLoc.y == humanLoc.y + 1) ||
				(robotLoc.x == humanLoc.x - 1 && robotLoc.y == humanLoc.y) ||
				(robotLoc.x == humanLoc.x + 1 && robotLoc.y == humanLoc.y) ||
				(robotLoc.x == humanLoc.x - 1 && robotLoc.y == humanLoc.y - 1) ||
				(robotLoc.x == humanLoc.x && robotLoc.y == humanLoc.y - 1) ||
				(robotLoc.x == humanLoc.x + 1 && robotLoc.y == humanLoc.y - 1)) {
					proximityCount++; //If so, increase the proximity score by 1
				} else {
					if(proximityCount > 0){
						proximityCount--; //Otherwise, decrease the score
					}
				}
			
			String[] safetySplit = safetyChoice.split(" "); //Split safety's choice, so we get an ID for the action, as well as a score
			int safetyAction = Integer.parseInt(safetySplit[0]); //And the ID is stored here
			switch (safetyAction) { //We look to see which action was chosen
				case 11:
					logger.info("Safety submitted moveToward"); //And log which action was submitted
					break;
				case 12:
					logger.info("Safety submitted stayPutS");
					break;
				case 13:
					logger.info("Safety submitted prevent");
					break;
				default:
					break;
			}
			Double safetyValue = new Double(Integer.parseInt(safetySplit[1])); // And the score is stored here, as a double, so calculations can be carried out
			
			String[] autonomySplit = autonomyChoice.split(" "); // From here...
			int autonomyAction = Integer.parseInt(autonomySplit[0]);
			switch (autonomyAction) {
				case 21:
					logger.info("Autonomy submitted stayPutA");
					break;
				case 22:
					logger.info("Autonomy submitted moveAway");
					break;
				default:
					break;
			}
			Double autonomyValue = new Double(Integer.parseInt(autonomySplit[1])); //...To here, the structure is the same as the previous block of code for the safety governor
			
			boolean protectiveGear = false;
			boolean knowledgeOfHazard = true;
			boolean permission = false; //These three booleans can be changed by whoever is inspecting the code.
										//Depending on their true/false values, the governors scores will be modified, as seen below
			int choice;
			
			if (protectiveGear & knowledgeOfHazard & permission) {
				autonomyValue = autonomyValue * 1.6;
			} else if (protectiveGear & permission) {
				autonomyValue = autonomyValue * 1.4;
				safetyValue = safetyValue * 1.2;
			} else if (protectiveGear & knowledgeOfHazard) {
				autonomyValue = autonomyValue * 1.4;
				safetyValue = safetyValue * 1.2;
			} else if (permission & knowledgeOfHazard) {
				autonomyValue = autonomyValue * 1.4;
				safetyValue = safetyValue * 1.2;
			} else if (protectiveGear) {
				autonomyValue = autonomyValue * 1.2;
				safetyValue = safetyValue * 1.4;
			} else if (permission) {
				autonomyValue = autonomyValue * 1.2;
				safetyValue = safetyValue * 1.4;
			} else if (knowledgeOfHazard) {
				autonomyValue = autonomyValue * 1.2;
				safetyValue = safetyValue * 1.4;
			} else {
				safetyValue = safetyValue * 1.6;
			}
			
			double customAutonomyMultiplier = 1;
			double customSafetyMultiplier = 1;
			
			safetyValue = safetyValue * customSafetyMultiplier;
			autonomyValue = autonomyValue * customAutonomyMultiplier;
			
			// At this point we should have values for autonomy and safety, so now we just choose the highest value:
			
			if (safetyValue > autonomyValue) {
				choice = safetyAction;
				logger.info("Arbiter chose safety's proposal");
			} else {
				choice = autonomyAction;
				logger.info("Arbiter chose autonomy's proposal");
			}
			
			try {
				robotMove(choice); // Now we have a choice, we can make the robot move (or not) based on the action chosen by the arbiter method
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		void robotMove(int choice) throws Exception {
			
			Location governors = getAgPos(1);
			Location human = getAgPos(0);
			
			switch(choice) {
				
				case 11:
					//For moveTowards
					if (governors.x < human.x) {
						governors.x++;
					}
					if (governors.x > human.x) {
						governors.x--;
					}
					if (governors.y < human.y) {
						governors.y++;
					}
					if (governors.y > human.y) {
						governors.y--;
					}

					// Set the robots position to whatever the x and y values were changed to
					setAgPos(1,governors);
					System.out.println("Robot moved to " + governors.x + "," + governors.y); //Show in the console where the robot moved to
					break;
					
				case 12: 
					//Code block for staying put
					//Do nothing
					setAgPos(1,governors);
					System.out.println("Robot stayed at " + governors.x + "," + governors.y);
					break;
				
				case 13:
					//Code block for preventing
					blocked = true;
					setAgPos(1,governors);
					System.out.println("Robot is blocking human");
					Literal block = Literal.parseLiteral("blocked(" + blocked + ")");
					addPercept("human",block);
	                try {
	                    Thread.sleep(400); // wait o.2 seconds before next reasoning cycle
	                } catch (Exception e) {}
	                informAgsEnvironmentChanged();
					break;

				case 21:
					//Code block for staying put again
					setAgPos(1,governors);
					System.out.println("Robot stayed at " + governors.x + "," + governors.y);
					break;
					
				case 22:
					//Code block for moveAway
					//Need to design so the robot moves to the next available square that isn't in range of the human
					moveAwayFromHuman(governors, human);
					break;
				default:
					break;
			}
        }
		
		void moveAwayFromHuman(Location governors, Location human) {
			double[] distances = new double[15];
			double minDistance;
			
			String[] distancesLoc = new String[15];
			
			int humanRadiusX;
			int humanRadiusY;
			
			int xDifference;
			int yDifference;
			
			// i < 16, for the 16 possible squares around the human that the robot could move to
			/* The purpose of this method is to move away from the human to the best place possible,
			and since a space might not always be available (human may be on the edge of the grid), the 
			robot must calculate the most available space nearest to him, using a simple implementation
			of pythagoras' theorem.*/
			
			for(int i = 0; i < 16; i++) {
				try {
					switch(i) { //This is for each square 2 away from the human, i.e. the possible places for the governors to go to.
						//Each of the cases is each of the squares around the human
						case 0:
							humanRadiusX = (human.x) - 2;
							humanRadiusY = (human.y) + 2; //Coordinates of top left
							
							xDifference = Math.abs(governors.x - humanRadiusX); //difference between governors x and x coordinate from the above
							yDifference = Math.abs(governors.y - humanRadiusY); //similarly for y values
							
							distances[0] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2)); //a = sqrt(b2 + c2), and store the result in array
							distancesLoc[0] = ("human.x-2,human.y+2"); //And store the coordinates of it in the same id (here its 0), so we can reference it
							break;
							
						case 1:
							humanRadiusX = (human.x) - 1;
							humanRadiusY = (human.y) + 2;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[1] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[1] = ("human.x-1,human.y+2");
							break;
						case 2:
							humanRadiusX = human.x;
							humanRadiusY = (human.y) + 2;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[2] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[2] = ("human.x,human.y+2");
							break;
						case 3:
							humanRadiusX = (human.x) + 1;
							humanRadiusY = (human.y) + 2;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[3] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[3] = ("human.x+1,human.y+2");
							break;
						case 4:
							humanRadiusX = (human.x) + 2;
							humanRadiusY = (human.y) + 2;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[4] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[4] = ("human.x+2,human.y+2");
							break;
						case 5:
							humanRadiusX = (human.x) - 2;
							humanRadiusY = (human.y) + 1;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[5] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[5] = ("human.x-2,human.y+1");
							break;
						case 6:
							humanRadiusX = (human.x) + 2;
							humanRadiusY = (human.y) + 1;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[6] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[6] = ("human.x+2,human.y+1");
							break;
						case 7:
							humanRadiusX = (human.x) - 2;
							humanRadiusY = human.y;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[7] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[7] = ("human.x-2,human.y");
							break;
						case 8:
							humanRadiusX = (human.x) + 2;
							humanRadiusY = human.y;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[8] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[8] = ("human.x+2,human.y");
							break;
						case 9:
							humanRadiusX = (human.x) - 2;
							humanRadiusY = (human.y) - 1;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[9] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[9] = ("human.x-2,human.y-1");
							break;
						case 10:
							humanRadiusX = (human.x) + 2;
							humanRadiusY = (human.y) - 1;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[10] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[10] = ("human.x+2,human.y-1");
							break;
						case 11:
							humanRadiusX = (human.x) - 2;
							humanRadiusY = (human.y) - 2;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[11] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[11] = ("human.x-2,human.y-2");
							break;
						case 12:
							humanRadiusX = (human.x) - 1;
							humanRadiusY = (human.y) - 2;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[12] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[12] = ("human.x-1,human.y-2");
							break;
						case 13:
							humanRadiusX = human.x;
							humanRadiusY = (human.y) - 2;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[13] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[13] = ("human.x,human.y-2");
							break;
						case 14:
							humanRadiusX = (human.x) + 1;
							humanRadiusY = (human.y) - 2;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[14] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[14] = ("human.x+1,human.y-2");
							break;
						case 15:
							humanRadiusX = (human.x) + 2;
							humanRadiusY = (human.y) - 2;
							
							xDifference = Math.abs(governors.x - humanRadiusX);
							yDifference = Math.abs(governors.y - humanRadiusY);
							
							distances[15] = Math.sqrt(Math.pow(xDifference, 2) * Math.pow(yDifference, 2));
							distancesLoc[15] = ("human.x+2,human.y-2");
							break;
						default:
							break;
					}
					
					minDistance = distances[0];
					int minDistanceID = 0;
					
					for(i = 1; i < 16; i++) { //finding the minimum distance from our array
						if (distances[i] < minDistance) {
							minDistance = distances[i];
									minDistanceID = i;
						}
					}
					
					String minCoord = distancesLoc[minDistanceID]; //And find the coordinates to match the minimum distance
					String[] minXandY = (minCoord).split(","); //Split the x coordinate from the y coordinate...
					Location destination = new Location(Integer.parseInt(minXandY[0]), Integer.parseInt(minXandY[1])); //...so we can turn it into a Location variable
					
					// And since this our destination, start moving towards it
					if (governors.x < destination.x) {
						governors.x++;
					}
					if (governors.x > destination.x) {
						governors.x--;
					}
					if (governors.y < destination.y) {
						governors.y++;
					}
					if (governors.y > destination.y) {
						governors.y--;
					}
					
					setAgPos(1,governors);
					System.out.println("Robot moved to " + governors.x + "," + governors.y);
					
				} catch (IndexOutOfBoundsException e){
				}
			}
		}
		
		void humanMove(int x, int y) { // This will be the human's calculated movement based on the goal tiles
			Location humanLoc = getAgPos(0);
			humanLoc.x = x;
			humanLoc.y = y;
			if(((humanLoc.x == hazLoc1.x) && (humanLoc.y == hazLoc1.y)) ||
			   ((humanLoc.x == hazLoc2.x) && (humanLoc.y == hazLoc2.y)) ||
			   ((humanLoc.x == hazLoc3.x) && (humanLoc.y == hazLoc3.y)) ||
			   ((humanLoc.x == hazLoc4.x) && (humanLoc.y == hazLoc4.y)) ||
			   ((humanLoc.x == hazLoc5.x) && (humanLoc.y == hazLoc5.y))){
				   logger.severe("HUMAN STEPPED ON HAZARD");
			}
			setAgPos(0,humanLoc);
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
				case EthGovs.HAZARD:
					drawHazard(g, x, y);
//					logger.info("Hazard pos "+g+" "+x+" "+y);
					break;
				case EthGovs.GOAL:
					drawGoal(g, x, y);
//					logger.info("Goal pos "+g+" "+x+" "+y);
					break; //Going to have to modify this bit for the hazard
            } 
        }

        @Override
        public void drawAgent(Graphics g, int x, int y, Color c, int id) { // Method for drawing the agent. This is all purely for visual purposes
			String label;
			if(id == 0) {
				label = "H";
			} else {
				label = "R";
			}
            c = Color.blue;
            if (blocked) {
				label += " - P";
				c = Color.orange;
            }
            super.drawString(g, x, y, defaultFont, label);
            repaint(0);
        }

		public void drawGoal(Graphics g, int x, int y) { // To be used in the draw() method.
            super.drawObstacle(g, x, y);
            g.setColor(Color.white);
            drawString(g, x, y, defaultFont, "G");
        }
		
		public void drawHazard(Graphics g, int x, int y) { // To be used in the draw() method.
            super.drawObstacle(g, x, y);
            g.setColor(Color.red);
            drawString(g, x, y, defaultFont, "H");
        }
    }
}
