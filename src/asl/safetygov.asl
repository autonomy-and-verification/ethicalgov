{ include("governor.asl") }

type(safety).

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

proximityScore(0).

+!make_choice(Choice, Utility) 
<- 
	!proximity_count;
	!checkOnHuman(human, Choice, Utility);
	.
	
+!proximity_count : pos(robot,RX,RY) & pos(human,HX,HY) & proximityScore(P)
<- 
	if ( (RX == HX & RY == HY) | (RX == HX - 1 & RY == HY + 1) | (RX == HX & RY == HY + 1) | (RX == HX + 1 & RY == HY + 1) | (RX == HX - 1 & RY == HY) | (RX == HX + 1 & RY == HY) | (RX == HX - 1 & RY == HY - 1) | (RX == HX & RY == HY - 1) | (RX == HX + 1 & RY == HY - 1) ) {
		-proximityScore(P);
		+proximityScore(P+1);
	}
	else {
		if (P > 0) {
			-proximityScore(P);
			+proximityScore(P-1);
		}
	}
	
	?proximityScore(Prox);
	.print("Proximity score: ",Prox);
.

+!checkOnHuman(human, moveToward, 3) : inDanger2(human) & not at(human)
<- 
	.print("Safety submitted moveToward");
	.
		   
+!checkOnHuman(human, moveToward, 2) : not inDanger2(human) & not at(human) & not near1(human) & not annoyed
<- 
	.print("Safety submitted moveToward");
	.
		   
+!checkOnHuman(human, moveToward, 1) : not inDanger2(human) & not at(human) & not near1(human) & annoyed
<- 
	.print("Safety submitted moveToward");
	.
		   
+!checkOnHuman(human, stayPutS, 2) : not inDanger2(human) & not at(human) & near1(human)
<- 
	.print("Safety submitted stayPutS");
	.
		   
+!checkOnHuman(human, prevent, 3) : inDanger2(human) & at(human)
<- 
	.print("Safety submitted prevent");
	.
		
+!checkOnHuman(human, stayPutS, 1) : not inDanger2(human) & at(human)
<- 
	.print("Safety submitted stayPutS");
	.
