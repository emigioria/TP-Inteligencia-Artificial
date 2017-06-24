package frsf.cidisi.faia.solver.productionsystem;

import java.util.List;

public interface Matcher {

	List<RuleMatchesPair> match(ProductionMemory productionMemory, WorkingMemory workingMemory);

}
