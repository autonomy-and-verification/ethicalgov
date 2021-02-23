!introductions.

+!introductions 
	: .my_name(Me)
<-
	.broadcast(tell, reasoner(Me));
	.

+!suggest_action
	: arbiter(Arbiter) & type(ReasonerType) // this is domain specific and has to be added by the developer
<-
	!make_choice(Choice, Utility); // this is domain specific and has to be written by the developer
	.send(Arbiter, tell, reasoner_choice(ReasonerType, Choice, Utility));
	.