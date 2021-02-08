{ include("governor.asl") }

type(autonomy).

+!make_choice(Choice, Utility) 
<- 
	Choice = a; 
	Utility = 1;
	.