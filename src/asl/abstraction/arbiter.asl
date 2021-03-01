count(0).

!introductions.

@receivelastchoice[atomic]
+evidential_reasoner_choice(ReasonerType, Action, Statement) : .count(evidential_reasoner(_),N) & count(N-1) <- .print("Received all of the reasoners choices."); -count(_); +count(0); !arbiter_choice.
@receivechoice[atomic]
+evidential_reasoner_choice(ReasonerType, Action, Statement) : count(N) <- .print("Received a reasoner choice, waiting for the remaining choices."); -count(N); +count(N+1).


+!introductions 
	: .my_name(Me)
<-
	.broadcast(tell, arbiter(Me));
	.

@arbiterchoice[atomic]
+!arbiter_choice : reasoning(utilitarian) & execution_agent(Agent) 
<- 
	+choice(0,0,0);
	
	for (evidential_reasoner_choice(Type, Action, Utility)) {
		-evidential_reasoner_choice(Type, Action, Utility)[source(_)];
		if ( type_multiplier(Type, TypeMultiplier) ) { // type multiplier has to be set by the developer
			NewUtility = TypeMultiplier * Utility;
		} else { // otherwise only the basic multiplier of the reasoner choice is used
			NewUtility = Utility;
		}
		?choice(BestUtility, BestType, BestAction);
		if (NewUtility > BestUtility) {
			-choice(BestUtility, BestType, BestAction);
			+choice(NewUtility, Type, Action);
		}
	}
	
	?choice(Utility, Type, Action);
	-choice(Utility, Type, Action);
	
	.print("Chose action proposal from ", Type, " reasoner with utility ", Utility, " and with choice: ", Action);
	
	.send(Agent, achieve, choice(Action, Type));
.
