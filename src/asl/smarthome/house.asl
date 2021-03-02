{ include("execution_agent.asl") }

!start.

@start[atomic]
+!start
<-
	.wait(500); // necessary for the setup messages to go through first
	!act;
	.

+!execute_action(null, null) <- .print("No action has been suggested, better to do nothing for now.").

+!execute_action(Action, ReasonerType) <- !Action.

+!select_action(ActionList, action(Action, ReasonerType))
<-
	.nth(0, ActionList, action(Action, ReasonerType));
.

+!warn_authorities <- .print("Executing action from the legal evidential reasoner to warn the police.").

+!warn_parents <- .print("Executing action from the safety evidential reasoner to warn the parents.").

+!log_activity <- .print("Executing action from the reliability evidential reasoner to log the activity.").

+!warn_teenager <- .print("Executing action from the privacy evidential reasoner to warn the teenager.").