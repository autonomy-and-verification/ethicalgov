{ include("arbiter.asl") }

reasoning(utilitarian). // default reasoning, add more later
type_multiplier(autonomy, 1).
type_multiplier(safety, 1.2).

proximity_count(0).

+stop <- .stopMAS.

+!proximity_count : pos(robot,RX,RY) & pos(human,HX,HY) & proximity_count(P)
<- 
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
.

