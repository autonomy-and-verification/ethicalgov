// THE HUMAN

goal_step(1).

at(P) :- pos(P,X,Y) & pos(human,X,Y). // at(P) means for position (P,X,Y) and the human is at that position
near1(A,B) :-  pos(A,X,Y) & pos(B,X-1,Y) | //May be some logical errors with this initial setof beliefs - find out during testing
			   pos(A,X,Y) & pos(B,X-1,Y-1) |
			   pos(A,X,Y) & pos(B,X,Y-1) |
			   pos(A,X,Y) & pos(B,X+1,Y+1) |
			   pos(A,X,Y) & pos(B,X+1,Y) |
			   pos(A,X,Y) & pos(B,X+1,Y-1) |
			   pos(A,X,Y) & pos(B,X,Y+1) |
			   pos(A,X,Y) & pos(B,X-1,Y+1).
			   
//+!act <- move.

+blocked(true) <- -blocked(false)[source(_)].
+blocked(false) <- -blocked(true)[source(_)].

+!act : not achieving_goal(_,_) & goal_step(Gstep)
<-
	?goal(Gstep,X,Y);
	+achieving_goal(X,Y);
	!move(X,Y);
	.
	
+!act : timer(0) & goal_step(Gstep)
<-
	-timer(0);
	-achieving_goal(_,_);
	-goal_step(Gstep);
	if (Gstep == 5) {
		+goal_step(1);
	}
	else {
		+goal_step(Gstep+1);
	}
	skip;
	.
	
+!act : timer(T)
<-
	-timer(T);
	+timer(T-1);
	skip;
	.
	
+!act : achieving_goal(X,Y) & pos(human,X,Y)
<-
	+timer(9);
	skip;
	.
	
+!act : achieving_goal(X,Y)
<-
	!move(X,Y);
	.
	
+!move(GX,GY) : blocked(false) & pos(human,HX,HY)
<-
	if (HX < GX) {
		X = HX + 1;
	}
	elif (HX > GX) {
		X = HX - 1;
	}
	else {
		X = HX;
	}
	if (HY < GY) {
		Y = HY + 1;
	}
	elif (HY > GY) {
		Y = HY - 1;
	}
	else {
		Y = HY;
	}
	move(X,Y);
	.
	
+!move(GX,GY) : blocked(true) & pos(human,HX,HY)
<-
	.print("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//	move(X,Y);
	.
	
	
	