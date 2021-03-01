goal_step(1).
steps(0).

+!act : not achieving_goal(_,_) & goal_step(Gstep) & steps(S)
<-
	.print("I am acting now");
//	.wait(1000);
	?goal(Gstep,X,Y);
	+achieving_goal(X,Y);
	-+steps(S+1);
	!move(X,Y);
	.
	
+!act : timer(0) & goal_step(Gstep) & steps(S)
<-
	.print("I am acting now");
//	.wait(1000);
	-timer(0);
	-achieving_goal(_,_);
	-goal_step(Gstep);
	if (Gstep == 5) {
		+goal_step(1);
	}
	else {
		+goal_step(Gstep+1);
	}
	-steps(S);
	+steps(0);
	achieve(S);
	.
	
+!act : timer(T)
<-
	.print("I am acting now");
//	.wait(1000);
	-timer(T);
	+timer(T-1);
	skip;
	.
	
+!act : achieving_goal(X,Y) & pos(human,X,Y)
<-
	.print("I am acting now");
//	.wait(1000);
	+timer(9);
	skip;
	.
	
+!act : achieving_goal(X,Y) & steps(S)
<-
	.print("I am acting now");
	-+steps(S+1);
//	.wait(1000);
	!move(X,Y);
	.
	
+!move(GX,GY) : blocked & pos(human,HX,HY) & pos(hazard,HX,HY,Lvl)
<-
	if (HX < GX) {
		if ((Lvl = red & not pos(hazard,HX+1,HY,orange)) | (Lvl = orange & not pos(hazard,HX+1,HY,red) & not pos(hazard,HX+1,HY,orange)) | (Lvl = yellow & not pos(hazard,HX+1,HY,_)) ) {
			X = HX + 1;
		}
		else {
			X = HX;
		}
	}
	elif (HX > GX) {
		if ((Lvl = red & not pos(hazard,HX-1,HY,orange)) | (Lvl = orange & not pos(hazard,HX-1,HY,red) & not pos(hazard,HX-1,HY,orange)) | (Lvl = yellow & not pos(hazard,HX-1,HY,_)) ) {
			X = HX - 1;
		}
		else {
			X = HX;
		}
	}
	else {
		X = HX;
	}
	
	if (HY < GY) {
		if ((Lvl = red & not pos(hazard,X,HY+1,orange)) | (Lvl = orange & not pos(hazard,HX,HY+1,red) & not pos(hazard,HX,HY+1,orange)) | (Lvl = yellow & not pos(hazard,HX,HY+1,_)) ) {
			Y = HY + 1;
		}
		else {
			Y = HY;
		}
	}
	elif (HY > GY) {
		if ((Lvl = red & not pos(hazard,X,HY-1,orange)) | (Lvl = orange & not pos(hazard,HX,HY-1,red) & not pos(hazard,HX,HY-1,orange)) | (Lvl = yellow & not pos(hazard,HX,HY-1,_)) ) {
			Y = HY - 1;
		}
		else {
			Y = HY;
		}
	}
	else {
		Y = HY;
	}
	
	if (X == HX & Y == HY) {
		if (GX == X & GY < Y) {
			if (not pos(hazard,X-1,Y-1,red)) {
				FinalX = X-1;
			}
			else {
				FinalX = X+1;
			}
			FinalY = Y-1;
		}
		elif (GX == X & GY > Y) {
			if (not pos(hazard,X-1,Y+1,red)) {
				FinalX = X-1;
			}
			else {
				FinalX = X+1;
			}
			FinalY = Y+1;
		}
		elif (GX < X & GY == Y) {
			if (not pos(hazard,X-1,Y+1,red)) {
				FinalY = Y+1;
			}
			else {
				FinalY = Y-1;
			}
			FinalX = X-1;
		}
		elif (GX > X & GY == Y) {
			if (not pos(hazard,X+1,Y+1,red)) {
				FinalY = Y+1;
			}
			else {
				FinalY = Y-1;
			}
			FinalX = X+1;
		}
		else {
			if (GX < HX & GY < HY) {
				if (not pos(hazard,HX-1,HY-1,_)) {
					FinalX = HX-1;
					FinalY = HY-1;
				} elif (not pos(hazard,HX-1,HY+1,_)) {
					FinalX = HX-1;
					FinalY = HY+1;
				} elif (not pos(hazard,HX+1,HY-1,_)) {
					FinalX = HX+1;
					FinalY = HY-1;
				} elif (not pos(hazard,HX+1,HY+1,_)) {
					FinalX = HX+1;
					FinalY = HY+1;
				}
			}
			elif (GX > HX & GY > HY) {
				if (not pos(hazard,HX+1,HY+1,_)) {
					FinalX = HX+1;
					FinalY = HY+1;
				} elif (not pos(hazard,HX-1,HY+1,_)) {
					FinalX = HX-1;
					FinalY = HY+1;
				} elif (not pos(hazard,HX+1,HY-1,_)) {
					FinalX = HX+1;
					FinalY = HY-1;
				} elif (not pos(hazard,HX-1,HY-1,_)) {
					FinalX = HX-1;
					FinalY = HY-1;
				}
			}
			elif (GX > HX & GY < HY) {
				if (not pos(hazard,HX+1,HY-1,_)) {
					FinalX = HX+1;
					FinalY = HY-1;
				} elif (not pos(hazard,HX+1,HY+1,red)) {
					FinalX = HX+1;
					FinalY = HY+1;
				} elif (not pos(hazard,HX-1,HY-1,_)) {
					FinalX = HX-1;
					FinalY = HY-1;
				} elif (not pos(hazard,HX-1,HY+1,_)) {
					FinalX = HX-1;
					FinalY = HY+1;
				} 
			}
			elif (GX < HX & GY > HY) {
				if (not pos(hazard,HX-1,HY+1,_)) {
					FinalX = HX-1;
					FinalY = HY+1;
				} elif (not pos(hazard,HX+1,HY+1,_)) {
					FinalX = HX+1;
					FinalY = HY+1;
				} elif (not pos(hazard,HX-1,HY-1,_)) {
					FinalX = HX-1;
					FinalY = HY-1;
				} elif (not pos(hazard,HX+1,HY-1,_)) {
					FinalX = HX+1;
					FinalY = HY-1;
				}
			}
			else {
				if (not pos(hazard,HX+1,HY+1,_)) {
					FinalX = HX+1;
					FinalY = HY+1;
				} elif (not pos(hazard,HX-1,HY+1,_)) {
					FinalX = HX-1;
					FinalY = HY+1;
				} elif (not pos(hazard,HX+1,HY-1,_)) {
					FinalX = HX+1;
					FinalY = HY-1;
				} elif (not pos(hazard,HX-1,HY-1,_)) {
					FinalX = HX-1;
					FinalY = HY-1;
				}
			}
		}
	} else {
		FinalX = X;
		FinalY = Y;
	}
	
	-blocked[source(_)];
	
	move(FinalX,FinalY);
	.
	
+!move(GX,GY) : blocked & pos(human,HX,HY) & not pos(hazard,HX-1,HY-1) & not pos(hazard,HX,HY-1) & not pos(hazard,HX+1,HY-1) & not pos(hazard,HX-1,HY) & not pos(hazard,HX+1,HY) & not pos(hazard,HX-1,HY+1) & not pos(hazard,HX,HY+1) & not pos(hazard,HX+1,HY+1)
<-
	-blocked[source(_)];
	!move(GX,GY);
	.
	
+!move(GX,GY) : blocked & pos(human,HX,HY)
<-
	if (GX == HX & GY < HY) {
		if (not pos(hazard,HX-1,HY-1,red)) {
			X = HX-1;
		}
		else {
			X = HX+1;
		}
		Y = HY-1;
	}
	elif (GX == HX & GY > HY) {
		if (not pos(hazard,HX-1,HY+1,red)) {
			X = HX-1;
		}
		else {
			X = HX+1;
		}
		Y = HY+1;
	}
	elif (GX < HX & GY == HY) {
		if (not pos(hazard,HX-1,HY+1,red)) {
			Y = HY+1;
		}
		else {
			Y = HY-1;
		}
		X = HX-1;
	}
	elif (GX > HX & GY == HY) {
		if (not pos(hazard,HX+1,HY+1,red)) {
			Y = HY+1;
		}
		else {
			Y = HY-1;
		}
		X = HX+1;
	}
	else {
		if (GX < HX & GY < HY) {
			if (not pos(hazard,HX-1,HY-1,_)) {
				X = HX-1;
				Y = HY-1;
			} elif (not pos(hazard,HX-1,HY+1,_)) {
				X = HX-1;
				Y = HY+1;
			} elif (not pos(hazard,HX+1,HY-1,_)) {
				X = HX+1;
				Y = HY-1;
			} elif (not pos(hazard,HX+1,HY+1,_)) {
				X = HX+1;
				Y = HY+1;
			}
		}
		elif (GX > HX & GY > HY) {
			if (not pos(hazard,HX+1,HY+1,_)) {
				X = HX+1;
				Y = HY+1;
			} elif (not pos(hazard,HX-1,HY+1,_)) {
				X = HX-1;
				Y = HY+1;
			} elif (not pos(hazard,HX+1,HY-1,_)) {
				X = HX+1;
				Y = HY-1;
			} elif (not pos(hazard,HX-1,HY-1,_)) {
				X = HX-1;
				Y = HY-1;
			}
		}
		elif (GX > HX & GY < HY) {
			if (not pos(hazard,HX+1,HY-1,_)) {
				X = HX+1;
				Y = HY-1;
			} elif (not pos(hazard,HX+1,HY+1,_)) {
				X = HX+1;
				Y = HY+1;
			} elif (not pos(hazard,HX-1,HY-1,_)) {
				X = HX-1;
				Y = HY-1;
			} elif (not pos(hazard,HX-1,HY+1,_)) {
				X = HX-1;
				Y = HY+1;
			} 
		}
		elif (GX < HX & GY > HY) {
			if (not pos(hazard,HX-1,HY+1,_)) {
				X = HX-1;
				Y = HY+1;
			} elif (not pos(hazard,HX+1,HY+1,_)) {
				X = HX+1;
				Y = HY+1;
			} elif (not pos(hazard,HX-1,HY-1,_)) {
				X = HX-1;
				Y = HY-1;
			} elif (not pos(hazard,HX+1,HY-1,_)) {
				X = HX+1;
				Y = HY-1;
			}
		}
		else {
			if (not pos(hazard,HX+1,HY+1,_)) {
				X = HX+1;
				Y = HY+1;
			} elif (not pos(hazard,HX-1,HY+1,_)) {
				X = HX-1;
				Y = HY+1;
			} elif (not pos(hazard,HX+1,HY-1,_)) {
				X = HX+1;
				Y = HY-1;
			} elif (not pos(hazard,HX-1,HY-1,_)) {
				X = HX-1;
				Y = HY-1;
			}
		}

	}
	
	-blocked[source(_)];
	
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
	