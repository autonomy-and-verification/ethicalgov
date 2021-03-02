{ include("reasoner.asl") }

type(privacy).

+!make_choice(Choice, Recommendation)
	: not repeated_occurence
<- 
	Choice = warn_teenager;
	Recommendation = yes;
	.

+!make_choice(Choice, Recommendation)
	: repeated_occurence & parents_home
<- 
	Choice = warn_teenager;
	Recommendation = no;
	.

+!make_choice(Choice, Recommendation)
<- 
	Choice = warn_teenager;
	Recommendation = maybe;
	.
	