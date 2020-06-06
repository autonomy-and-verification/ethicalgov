+!human_step
<- 
	.send(human, achieve, act);
	.
	
	
// safety move towards human
+choice(11)
<-
	.print("Choice 11");
	-choice(_)[source(_)];
	!move_towards;
	!human_step;
	.
	
// safety stay put
+choice(12)
<-
	.print("Choice 12");
	-choice(_)[source(_)];
	!human_step;
	.
	
// safety block human
+choice(13)
<-
	.print("Choice 13");
	-choice(_)[source(_)];
	block;
	.send(human,tell,blocked);
	!human_step;
	.

// autonomy stay put
+choice(21)
<-
	.print("Choice 21");
	-choice(_)[source(_)];
	!human_step;
	.

// autonomy move away from human
+choice(22)
<-
	.print("Choice 22");
	.print("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ To be implemented");
	.wait(999999);
	.
	
	
+!move_towards : pos(robot,RX,RY) & pos(human,HX,HY)
<-
	if (RX < HX) {
		X = RX + 1;
	}
	elif (HX > GX) {
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
