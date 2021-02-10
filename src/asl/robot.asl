{ include("execution_agent.asl") }

!start.

+!start
<-
	.wait(500); // necessary for the setup messages to go through first
	!act
	.
	
+!execute_action(Action) <- .print("Executing action ",Action).	