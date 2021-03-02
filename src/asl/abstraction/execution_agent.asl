!introductions.

+!introductions 
	: .my_name(Me)
<-
	.broadcast(tell, execution_agent(Me));
	.

+!act
<-
	for ( evidential_reasoner(Reasoner) ) {
		.send(Reasoner, achieve, suggest_action);
	}
	.
	
+!choice(ActionList)
	: .list(ActionList)
<-
	!select_action(ActionList, action(Action, ReasonerType)); // this is domain specific and has to be written by the developer
	!execute_action(Action, ReasonerType). // this is domain specific and has to be written by the developer
	
+!choice(action(Action, ReasonerType))
<-
	!execute_action(Action, ReasonerType). // this is domain specific and has to be written by the developer
