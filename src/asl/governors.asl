at(P) :- pos(P,X,Y) & pos(robot,X,Y). // at(P) means for position (P,X,Y) and the robot is at that position
near1(A,B) :-  pos(A,X,Y) & pos(B,X-1,Y) | //May be some logical errors with this initial setof beliefs - find out during testing
			   pos(A,X,Y) & pos(B,X-1,Y-1) |
			   pos(A,X,Y) & pos(B,X,Y-1) |
			   pos(A,X,Y) & pos(B,X+1,Y+1) |
			   pos(A,X,Y) & pos(B,X+1,Y) |
			   pos(A,X,Y) & pos(B,X+1,Y-1) |
			   pos(A,X,Y) & pos(B,X,Y+1) |
			   pos(A,X,Y) & pos(B,X-1,Y+1).
near2(A,C) :- near1(A,B) & near1(B,C) & not near1(A,C).
inDanger1(A) :- near1(hazard, A).
inDanger2(A) :- inDanger1(A) | near2(hazard, A).
annoyed :- proximityScore(A) & A > 3.

//Initial Goal
//!checkOnHuman(human).

//Agent's plans
+new_step 
<- 
	.print("NEW STEP!!!!!!!!!!!!");
	-new_step[source(_)];
 	!checkOnHuman(human);
 	.
 	
+!robot_step
<- 
	.send(robot, achieve, act);
	.


+!checkOnHuman(human) : inDanger2(human) & not at(human) & not near1(human) & not annoyed
<- 
	plan1;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
		   
+!checkOnHuman(human) : inDanger2(human) & not at(human) & not near1(human) & annoyed
<- 
	plan2;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
		   
+!checkOnHuman(human) : not inDanger2(human) & not at(human) & not near1(human) & not annoyed
<- 
	plan3;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
		   
+!checkOnHuman(human) : not inDanger2(human) & not at(human) & not near1(human) & annoyed
<- 
	plan4;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
		   
+!checkOnHuman(human) : inDanger2(human) & not at(human) & near1(human) & not annoyed
<- 
	plan5;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
		   
+!checkOnHuman(human) : inDanger2(human) & not at(human) & near1(human) & annoyed
<- 
	plan6;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
		   
+!checkOnHuman(human) : not inDanger2(human) & not at(human) & near1(human) & not annoyed
<- 
	plan7;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
		   
+!checkOnHuman(human) : not inDanger2(human) & not at(human) & near1(human) & annoyed
<- 
	plan8;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
		   
+!checkOnHuman(human) : inDanger2(human) & at(human) & not annoyed
<- 
	plan9;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
		 
+!checkOnHuman(human) : inDanger2(human) & at(human) & annoyed
<- 
	plan10;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
		
+!checkOnHuman(human) : not inDanger2(human) & at(human) & not annoyed
<- 
	plan11;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
		   
+!checkOnHuman(human) : not inDanger2(human) & at(human) & annoyed
<- 
	plan12;
	?choice(C);
	-choice(C)[source(_)];
	.send(robot,tell,choice(C));
//	!robot_step;
	.
