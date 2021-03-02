{ include("execution_agent.asl") }

!start.

@start[atomic]
+!start
<-
	.wait(500); // necessary for the setup messages to go through first
	.
	
+!execute_action(Action, ReasonerType) <- ReasonerType; !Action.
