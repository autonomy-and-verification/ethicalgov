{ include("arbiter.asl") }

reasoning(utilitarian). // default reasoning, add more later
type_multiplier(autonomy, 3.5).
type_multiplier(safety, 1).

proximity_count(0).

+stop <- .stopMAS.

