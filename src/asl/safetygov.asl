{ include("governor.asl") }

type(safety).

+!make_choice(Choice, Utility) 
<- 
	Choice = b; 
	Utility = 2;
	.