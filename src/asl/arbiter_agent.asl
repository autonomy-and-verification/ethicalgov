{ include("arbiter.asl") }

reasoning(utilitarian). // default reasoning, add more later
type_multiplier(autonomy, 1).
type_multiplier(safety, 1.2).

proximity_count(0).

+stop <- .stopMAS.

