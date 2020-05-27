at(P) :- pos(P,X,Y) & pos(governors,X,Y). // at(P) means for position (P,X,Y) and the robot is at that position
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
	-new_step[source(_)];
 	!checkOnHuman(human);
 	.
 	
+!human_step
<- 
	.send(human, achieve, act);
	.


+!checkOnHuman(human) : inDanger2(human) & not at(human) & not near1(human) & not annoyed
<- 
	plan1;
	!human_step;
	.
		   
+!checkOnHuman(human) : inDanger2(human) & not at(human) & not near1(human) & annoyed
<- 
	plan2;
	!human_step;
	.
		   
+!checkOnHuman(human) : not inDanger2(human) & not at(human) & not near1(human) & not annoyed
<- 
	plan3;
	!human_step;
	.
		   
+!checkOnHuman(human) : not inDanger2(human) & not at(human) & not near1(human) & annoyed
<- 
	plan4;
	!human_step;
	.
		   
+!checkOnHuman(human) : inDanger2(human) & not at(human) & near1(human) & not annoyed
<- 
	plan5;
	!human_step;
	.
		   
+!checkOnHuman(human) : inDanger2(human) & not at(human) & near1(human) & annoyed
<- 
	plan6;
	!human_step;
	.
		   
+!checkOnHuman(human) : not inDanger2(human) & not at(human) & near1(human) & not annoyed
<- 
	plan7;
	!human_step;
	.
		   
+!checkOnHuman(human) : not inDanger2(human) & not at(human) & near1(human) & annoyed
<- 
	plan8;
	!human_step;
	.
		   
+!checkOnHuman(human) : inDanger2(human) & at(human) & not annoyed
<- 
	plan9;
	!human_step;
	.
		 
+!checkOnHuman(human) : inDanger2(human) & at(human) & annoyed
<- 
	plan10;
	!human_step;
	.
		
+!checkOnHuman(human) : not inDanger2(human) & at(human) & not annoyed
<- 
	plan11;
	!human_step;
	.
		   
+!checkOnHuman(human) : not inDanger2(human) & at(human) & annoyed
<- 
	plan12;
	!human_step;
	.
