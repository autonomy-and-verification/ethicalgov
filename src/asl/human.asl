// THE HUMAN

goal_step(1).
			   
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
	
+!move(GX,GY) : blocked(true) & pos(human,HX,HY)
<-
	if (HX < GX) {
		if (pos(hazard,HX+1,HY)) {
			X = HX + 1;
			Y = HY + 1;
		}
		else {
			X = HX + 1;
		}
		unblock;
	}
	elif (HX > GX) {
		if (pos(hazard,HX-1,HY)) {
			X = HX - 1;
			Y = HY - 1;
		}
		else {
			X = HX - 1;
		}
		unblock;
	}
	if  (HY < GY) {
		if (pos(hazard,HX,HY+1)) {
			X = HX + 1;
			Y = HY + 1;
		}
		else {
			Y = HY + 1;
		}
		unblock;
		
	}
	elif (HY > GY) {
		if (pos(hazard,HX,HY-1)) {
			X = HX - 1;
			Y = HY - 1;
		}
		else {
			Y = HY - 1;
		}
		unblock;
	}
	if (not .ground(X)) {
		X = HX;
	}
	if (not .ground(Y)) {
		Y = HY;
	}
	move(X,Y);
	.
	
+!move(GX,GY) : pos(human,HX,HY)
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
	