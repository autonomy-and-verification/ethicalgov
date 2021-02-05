proximity_count(0).
protective_gear(false).
knowledge_hazard(true).
permission(false).
autonomy_multiplier(1.1).
safety_multiplier(1).

+stop <- .stopMAS.

@safety1[atomic]
+safety_choice(_, _) : not autonomy_choice(_,_) <- .print("Received safety choice. Waiting for autonomy choice.").
@safety2[atomic]
+safety_choice(_, _) <- !arbiter_choice.

@autonomy1[atomic]
+autonomy_choice(_, _) : not safety_choice(_,_) <- .print("Received autonomy choice. Waiting for safety choice.").
@autonomy2[atomic]
+autonomy_choice(_, _) <- !arbiter_choice.

@arbiter[atomic]
+!arbiter_choice : autonomy_choice(AC,AR) & safety_choice(SC,SR) & pos(robot,RX,RY) & pos(human,HX,HY) & proximity_count(P) & autonomy_multiplier(AutonomyMultiplier) & safety_multiplier(SafetyMultiplier) 
<- 
	.print("Safety choice ",SC,":",SR,". Autonomy choice ",AC,":",AR);
	-autonomy_choice(AC,AR)[source(_)];
	-safety_choice(SC,SR)[source(_)];
	if ( (RX == HX & RY == HY) | (RX == HX - 1 & RY == HY + 1) | (RX == HX & RY == HY + 1) | (RX == HX + 1 & RY == HY + 1) | (RX == HX - 1 & RY == HY) | (RX == HX + 1 & RY == HY) | (RX == HX - 1 & RY == HY - 1) | (RX == HX & RY == HY - 1) | (RX == HX + 1 & RY == HY - 1) ) {
		-proximity_count(P);
		+proximity_count(P+1);
	}
	else {
		if (P > 0) {
			-proximity_count(P);
			+proximity_count(P-1);
		}
	}
	
	?proximity_count(Prox);
	.print("Proximity score: ",Prox);
	.send(safetygov, tell, proximityScore(Prox));
	.send(autonomygov, tell, proximityScore(Prox));
	
	if ( protective_gear(true) & knowledge_hazard(true) & permission(true) ) {
		AutonomyScore = AR * 1.6;
	} elif ( protective_gear(true) & permission(true) ) {
		AutonomyScore = AR * 1.4;
		SafetyScore = SR * 1.2;
	} elif ( protective_gear(true) & knowledge_hazard(true) ) {
		AutonomyScore = AR * 1.4;
		SafetyScore = SR * 1.2;
	} elif ( permission(true) & knowledge_hazard(true) ) {
		AutonomyScore = AR * 1.4;
		SafetyScore = SR * 1.2;
	} elif ( protective_gear(true) ) {
		AutonomyScore = AR * 1.2;
		SafetyScore = SR * 1.4;
	} elif ( permission(true) ) {
		AutonomyScore = AR * 1.2;
		SafetyScore = SR * 1.4;
	} elif ( knowledge_hazard(true) ) {
		AutonomyScore = AR * 1.2;
		SafetyScore = SR * 1.4;
	} 
//	else {
//		SafetyScore = SR * 1.6;
//	}
	
	FinalAutonomyScore = AutonomyScore * AutonomyMultiplier;
	FinalSafetyScore = SafetyScore * SafetyMultiplier;
	
	if ( FinalSafetyScore >= FinalAutonomyScore ) {
		.print("Chose safety proposal.");
		safety;
		Choice = SC;
	} else {
		.print("Chose autonomy proposal.");
		autonomy;
		Choice = AC;
		
	}
	
	.send(robot,tell,choice(Choice));
.

