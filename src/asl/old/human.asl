goal_step(1).
skip(0).

+!act : skip(S) & S < 2 & S > 0
<-
	-skip(S);
	+skip(S+1);
	skip
	.
	
+!act : skip(2)
<-
	-skip(2);
	+skip(0);
	skip
	.

+!act : not achieving_goal(_,_) & goal_step(Gstep)
<-
	-skip(0);
	+skip(2);
	.print("I am acting now");
//	.wait(400);
	?goal(Gstep,X,Y);
	+achieving_goal(X,Y);
	!move(X,Y);
	.
	
+!act : timer(0) & goal_step(Gstep)
<-
	.print("I am acting now");
//	.wait(400);
	-timer(0);
	-achieving_goal(_,_);
	-goal_step(Gstep);
	if (Gstep == 5) {
		+goal_step(1);
	}
	else {
		+goal_step(Gstep+1);
	}
	achieve;
	.
	
+!act : timer(T)
<-
	.print("I am acting now");
//	.wait(400);
	-timer(T);
	+timer(T-1);
	skip;
	.
	
+!act : achieving_goal(X,Y) & pos(human,X,Y)
<-
	.print("I am acting now");
//	.wait(400);
	+timer(9);
	skip;
	.
	
+!act : achieving_goal(X,Y)
<-
	-skip(0);
	+skip(2);
	.print("I am acting now");
//	.wait(400);
	!move(X,Y);
	.
	
+!move(GX,GY) : blocked & pos(human,HX,HY)
<-
	+x(HX);
	+y(HY);
	if (HX < GX) {
		if (pos(hazard,HX+1,HY)) {
			X = HX + 2;
		}
		else {
			X = HX + 1;
		}
	}
	elif (HX > GX) {
		if (pos(hazard,HX-1,HY)) {
			X = HX - 2;
		}
		else {
			X = HX - 1;
		}
	}
	else {
		X = HX;
	}
	
	if (HY < GY) {
		if (pos(hazard,X,HY+1)) {
			Y = HY + 2;
		}
		else {
			Y = HY + 1;
		}
	}
	elif (HY > GY) {
		if (pos(hazard,X,HY-1)) {
			Y = HY - 2;
		}
		else {
			Y = HY - 1;
		}
	}
	else {
		Y = HY;
	}
	
	if (not (X == HX & Y == HY)) {
		-blocked[source(_)];
		unblock;
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
	