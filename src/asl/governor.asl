!introductions.

+!introductions 
	: .my_name(Me)
<-
	.broadcast(tell, governor(Me));
	.

+!suggest_action
	: arbiter(Arbiter) & type(GovernorType) // this is domain specific and has to be added by the developer
<-
	!make_choice(Choice, Utility); // this is domain specific and has to be written by the developer
	.send(Arbiter, tell, governor_choice(GovernorType, Choice, Utility));
	.