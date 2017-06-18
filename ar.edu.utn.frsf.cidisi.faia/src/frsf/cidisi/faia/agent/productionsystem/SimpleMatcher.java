package frsf.cidisi.faia.agent.productionsystem;

import java.util.List;
import java.util.stream.Collectors;

import frsf.cidisi.faia.solver.productionsystem.Matcher;
import frsf.cidisi.faia.solver.productionsystem.Matches;
import frsf.cidisi.faia.solver.productionsystem.ProductionMemory;
import frsf.cidisi.faia.solver.productionsystem.Rule;
import frsf.cidisi.faia.solver.productionsystem.WorkingMemory;
import javafx.util.Pair;

public class SimpleMatcher implements Matcher {

	/**
	 * Naive algorithm for matching.
	 *
	 * @param productionMemory
	 *            Production memory with the rules
	 * @param workingMemory
	 *            Working memory with the data
	 * @return List of pair of rules with their matches
	 */
	@Override
	public List<Pair<Rule, Matches>> match(ProductionMemory productionMemory, WorkingMemory workingMemory) {
		return productionMemory.getRules()
				.parallelStream()
				.map(r -> r.match().parallelStream().map(m -> new Pair<>(r, m)).collect(Collectors.toList()))
				.flatMap(List::stream)
				.collect(Collectors.toList());
	};

}
