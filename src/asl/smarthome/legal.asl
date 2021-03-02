{ include("reasoner.asl") }

type(legal).

+!make_choice(Choice, Recommendation)
	: tobacco_illegal & repeated_occurence & not parents_home
<- 
	Choice = warn_authorities;
	Recommendation = yes;
	.
	
+!make_choice(Choice, Recommendation)
	: not tobacco_illegal
<- 
	Choice = warn_authorities;
	Recommendation = no;
	.

+!make_choice(Choice, Recommendation)
<- 
	Choice = warn_authorities;
	Recommendation = maybe;
	.
	