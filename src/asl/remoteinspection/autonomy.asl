{ include("reasoner.asl") }

type(autonomy).

at(P) :- pos(P,X,Y) & pos(robot,X,Y). // at(P) means for position (P,X,Y) and the robot is at that position
near(A, B, 1, Lvl) :-  (pos(A,X,Y) | pos(A,X,Y,Lvl)) & (pos(B,X-1,Y) | pos(B,X-1,Y,Lvl)) | //May be some logical errors with this initial setof beliefs - find out during testing
			   (pos(A,X,Y) | pos(A,X,Y,Lvl)) & (pos(B,X-1,Y-1) | pos(B,X-1,Y-1,Lvl)) |
			   (pos(A,X,Y) | pos(A,X,Y,Lvl)) & (pos(B,X,Y-1) | pos(B,X,Y-1,Lvl)) |
			   (pos(A,X,Y) | pos(A,X,Y,Lvl)) & (pos(B,X+1,Y+1) | pos(B,X+1,Y+1,Lvl)) |
			   (pos(A,X,Y) | pos(A,X,Y,Lvl)) & (pos(B,X+1,Y) | pos(B,X+1,Y,Lvl)) |
			   (pos(A,X,Y) | pos(A,X,Y,Lvl)) & (pos(B,X+1,Y-1) | pos(B,X+1,Y-1,Lvl)) |
			   (pos(A,X,Y) | pos(A,X,Y,Lvl)) & (pos(B,X,Y+1) | pos(B,X,Y+1,Lvl)) |
			   (pos(A,X,Y) | pos(A,X,Y,Lvl)) & (pos(B,X-1,Y+1) | pos(B,X-1,Y+1,Lvl)).
near(A, B, Th, Lvl) :- Th > 1 & (near(A, B, 1, Lvl) | (near(A, C, 1, Lvl) & near(C, B, Th-1, Lvl))).
/*near2(A,C) :- near1(A,B) & near1(B,C) & not near1(A,C).
inDanger1(A) :- near1(hazard, A).
inDanger2(A) :- inDanger1(A) | near2(hazard, A).*/
inDanger(A, Lvl, Th) :- near(A, hazard, Th, Lvl).
annoyed :- proximityScore(A) & A > 3.
//close(A, hazard(Lvl), Th) :- pos(A, Ax, Ay) & pos(hazard, Bx, By, Lvl) & (math.abs(Ax - Bx) + math.abs(Ay - By)) < Th.
//close(A, B, Th) :- pos(A, Ax, Ay) & pos(B, Bx, By) & (math.abs(Ax - Bx) + math.abs(Ay - By)) < Th.

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

+!checkOnHuman(human, stayPutA, 1) : not at(human) & not near(human, robot, 1, _) & not annoyed 
<- 
	.print("Autonomy submitted stayPutA");
	.
		   
+!checkOnHuman(human, stayPutA, 3) : not at(human) & not near(human, robot, 1, _) & annoyed 
<- 
	.print("Autonomy submitted stayPutA");
	.
		   
+!checkOnHuman(human, moveAway, 1) : inDanger(human, _, 2) & ((not at(human) & near(human, robot, 1, _)) | at(human)) & not annoyed 
<- 
	.print("Autonomy submitted moveAway");
	.
		   
+!checkOnHuman(human, moveAway, 3) : ((not at(human) & near(human, robot, 1, _)) | at(human)) & annoyed
<- 
	.print("Autonomy submitted moveAway");
	.
		   
+!checkOnHuman(human, moveAway, 2) : ((not at(human) & near(human, robot, 1, _	)) | at(human))
<- 
	.print("Autonomy submitted moveAway");
	.
