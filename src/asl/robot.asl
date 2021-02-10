{ include("execution_agent.asl") }

!start.

@start[atomic]
+!start
<-
	.wait(500); // necessary for the setup messages to go through first
	.
	
+new_step(Step)
<- 
	-new_step(Step)[source(_)];
 	!act;
 	.
	
+!execute_action(Action) <- +choice(Action).	

+!human_step
<- 
	.send(human, achieve, act);
	.
	
	
// safety move towards human
+choice(moveToward)
<-
	.print("Choice moveToward");
	-choice(_)[source(_)];
	!move_towards;
	!human_step;
	.
	
// safety stay put
+choice(stayPutS)
<-
	.print("Choice stayPutS");
	-choice(_)[source(_)];
	!human_step;
	.
	
// safety block human
+choice(prevent)
<-
	.print("Choice prevent");
	-choice(_)[source(_)];
	block;
	.send(human,tell,blocked);
	!human_step;
	.

// autonomy stay put
+choice(stayPutA)
<-
	.print("Choice stayPutA");
	-choice(_)[source(_)];
	!human_step;
	.

// autonomy move away from human
+choice(moveAway)
<-
	.print("Choice moveAway");
	-choice(_)[source(_)];
	!move_away;
	!human_step;
	.
	
	
+!move_towards : pos(robot,RX,RY) & pos(human,HX,HY)
<-
	if (RX < HX) {
		X = RX + 1;
	}
	elif (RX > HX) {
		X = RX - 1;
	}
	else {
		X = RX;
	}
	if (RY < HY) {
		Y = RY + 1;
	}
	elif (RY > HY) {
		Y = RY - 1;
	}
	else {
		Y = RY;
	}
	move(X,Y);
	.
	
+!move_away : pos(robot,RX,RY) & pos(human,HX,HY)
<-
	if ((RX <= HX)  & (RX - 1 >= 0)) {
		X = RX - 1;
	}
	elif ((RX > HX)  & (RX + 1 >= 10)) {
		X = RX + 1;
	}
	else {
		X = RX;
	}
	if ((RY <= HY)  & (RY - 1 >= 0)) {
		Y = RY - 1;
	}
	elif ((RY > HY)  & (RY + 1 >= 10)) {
		Y = RY + 1;
	}
	else {
		Y = RY;
	}
	move(X,Y);
	.
