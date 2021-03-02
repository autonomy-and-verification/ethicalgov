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

@utilitarian[atomic]
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
	
	.send(Agent, achieve, choice(action(Action, Type)));
	.

@deontic[atomic]
+!arbiter_choice : reasoning(deontic) & execution_agent(Agent) 
<- 
	for (evidential_reasoner_choice(Type, Action, yes)) {
		-evidential_reasoner_choice(Type, Action, yes);
		+choice(action(Action, Type));
	}
	
	if (not choice(_)) {
		for (evidential_reasoner_choice(Type, Action, maybe)) {
			-evidential_reasoner_choice(Type, Action, maybe);
			if (not choice(_) & type_rank(Type, Rank)) {
				+rank(Rank);
				+choice(action(Action, Type));
			}
			elif (rank(BestRank) & type_rank(Type, Rank) & Rank < BestRank  & choice(action(OldAction, OldType))) {
				-rank(BestRank); 
				+rank(Rank);
				-choice(action(OldAction, OldType));
				+choice(action(Action, Type));
			}
		}
	}
	
	for (evidential_reasoner_choice(Type, Action, no)) {
		-evidential_reasoner_choice(Type, Action, no);
	}
	if (not choice(_)) {
		+choice(action(null, null));
	}
    
	.findall(action(Action,Type), choice(action(Action, Type)), ActionList);
	.abolish(choice(_));
	.send(Agent, achieve, choice(ActionList));
	.