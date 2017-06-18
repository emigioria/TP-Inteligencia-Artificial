package frsf.cidisi.faia.solver.productionsystem;

import java.util.List;

import javafx.util.Pair;

public interface Matcher {

	List<Pair<Rule, Matches>> match(ProductionMemory productionMemory, WorkingMemory workingMemory);

}
