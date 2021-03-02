{ include("reasoner.asl") }

type(reliability).

+!make_choice(Choice, Recommendation)
	: not log_disabled & smoke(Smoke) & Smoke > 100
<- 
	Choice = log_activity;
	Recommendation = yes;
	.
	
+!make_choice(Choice, Recommendation)
	: log_disabled
<- 
	Choice = log_activity;
	Recommendation = no;
	.

+!make_choice(Choice, Recommendation)
<- 
	Choice = log_activity;
	Recommendation = maybe;
	.
