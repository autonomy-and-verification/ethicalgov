!introductions.

+!introductions 
	: .my_name(Me)
<-
	.broadcast(tell, execution_agent(Me));
	.

+!act
<-
	for ( reasoner(Reasoner) ) {
		.send(Reasoner, achieve, suggest_action);
	}
	.
	
+!choice(Action)
<-
	!execute_action(Action); // this is domain specific and has to be written by the developer
	.