count(0).

!introductions.

@receivelastchoice[atomic]
+governor_choice(GovernorType, Choice, Utility) : .count(governor(_),N) & count(N-1) <- .print("Received all of the governors choices."); -count(_); +count(0); !arbiter_choice.
@receivechoice[atomic]
+governor_choice(GovernorType, Choice, Utility) : count(N) <- .print("Received a governor choice, waiting for the remaining choices."); -count(N); +count(N+1).


+!introductions 
	: .my_name(Me)
<-
	.broadcast(tell, arbiter(Me));
	.

@arbiterchoice[atomic]
+!arbiter_choice : reasoning(utilitarian) & execution_agent(Agent) 
<- 
	+choice(0,0,0);
	
	!proximity_count;
	
	for (governor_choice(GovernorType, Choice, Utility)) {
		-governor_choice(GovernorType, Choice, Utility)[source(_)];
		if ( type_multiplier(GovernorType, TypeMultiplier) ) { // type multiplier has to be set by the developer
			NewUtility = TypeMultiplier * Utility;
		} else { // otherwise only the basic multiplier of the governor choice is used
			NewUtility = Utility;
		}
		?choice(CurrentUtility, GovernorTypeOld, ChoiceOld);
		if (NewUtility > CurrentUtility) {
			-choice(CurrentUtility, GovernorTypeOld, ChoiceOld);
			+choice(NewUtility, GovernorType, Choice);
		}
	}
	
	?choice(Utility, GovernorType, Choice);
	-choice(Utility, GovernorType, Choice);
	
	.print("Chose action proposal from ", GovernorType, " governor with utility ", Utility, " and with choice: ", Choice);
	
	.send(Agent, achieve, choice(Choice));
.
