count(0).

!introductions.

@receivelastchoice[atomic]
+reasoner_choice(ReasonerType, Choice, Utility) : .count(reasoner(_),N) & count(N-1) <- .print("Received all of the reasoners choices."); -count(_); +count(0); !arbiter_choice.
@receivechoice[atomic]
+reasoner_choice(ReasonerType, Choice, Utility) : count(N) <- .print("Received a reasoner choice, waiting for the remaining choices."); -count(N); +count(N+1).


+!introductions 
	: .my_name(Me)
<-
	.broadcast(tell, arbiter(Me));
	.

@arbiterchoice[atomic]
+!arbiter_choice : reasoning(utilitarian) & execution_agent(Agent) 
<- 
	+choice(0,0,0);
	
	for (reasoner_choice(ReasonerType, Choice, Utility)) {
		-reasoner_choice(ReasonerType, Choice, Utility)[source(_)];
		if ( type_multiplier(ReasonerType, TypeMultiplier) ) { // type multiplier has to be set by the developer
			NewUtility = TypeMultiplier * Utility;
		} else { // otherwise only the basic multiplier of the reasoner choice is used
			NewUtility = Utility;
		}
		?choice(CurrentUtility, ReasonerTypeOld, ChoiceOld);
		if (NewUtility > CurrentUtility) {
			-choice(CurrentUtility, ReasonerTypeOld, ChoiceOld);
			+choice(NewUtility, ReasonerType, Choice);
		}
	}
	
	?choice(Utility, ReasonerType, Choice);
	-choice(Utility, ReasonerType, Choice);
	
	ReasonerType;
	
	.print("Chose action proposal from ", ReasonerType, " reasoner with utility ", Utility, " and with choice: ", Choice);
	
	.send(Agent, achieve, choice(Choice));
.
