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
	
+!execute_action(Action, ReasonerType) <- ReasonerType; !Action.

+!human_step
<- 
	.send(human, achieve, act);
	.
	
	
// safety move towards human
+!moveToward
<-
	.print("Choice moveToward");
	!move_towards;
	!human_step;
	.
	
// safety stay put
+!stayPutS
<-
	.print("Choice stayPutS");
	!human_step;
	.
	
// safety block human
+!prevent
<-
	.print("Choice prevent");
	block;
	.send(human,tell,blocked);
	!human_step;
	.

// autonomy stay put
+!stayPutA
<-
	.print("Choice stayPutA");
	!human_step;
	.

// autonomy move away from human
+!moveAway
<-
	.print("Choice moveAway");
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
	if (RX < HX) {
		X = RX - 1;
	}
	elif (RX > HX) {
		X = RX + 1;
	}
	else {
		X = RX;
	}
	if (RY < HY) {
		Y = RY - 1;
	}
	elif (RY > HY)  {
		Y = RY + 1;
	}
	else {
		Y = RY;
	}
	move(X,Y);
	.
