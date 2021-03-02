{ include("arbiter.asl") }

reasoning(deontic). // default reasoning, add more later
type_rank(autonomy, 2).
type_rank(safety, 1).

+stop <- .stopMAS.
