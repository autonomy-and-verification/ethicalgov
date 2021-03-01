!introductions.

+!introductions 
	: .my_name(Me)
<-
	.broadcast(tell, evidential_reasoner(Me));
	.

+!suggest_action
	: arbiter(Arbiter) & type(ReasonerType) // this is domain specific and has to be added by the developer
<-
	!make_choice(Action, Statement); // this is domain specific and has to be written by the developer
	.send(Arbiter, tell, evidential_reasoner_choice(ReasonerType, Action, Statement));
	.