{ include("reasoner.asl") }

type(safety).

+!make_choice(Choice, Recommendation)
	: parents_home
<- 
	Choice = warn_parents;
	Recommendation = yes;
	.
	
+!make_choice(Choice, Recommendation)
	: not tobacco_illegal & not parents_home
<- 
	Choice = warn_parents;
	Recommendation = no;
	.

+!make_choice(Choice, Recommendation)
<- 
	Choice = warn_parents;
	Recommendation = maybe;
	.
	

	