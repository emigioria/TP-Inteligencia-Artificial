package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;
import java.util.stream.Collectors;

import frsf.cidisi.faia.solver.productionsystem.Matcher;
import frsf.cidisi.faia.solver.productionsystem.ProductionMemory;
import frsf.cidisi.faia.solver.productionsystem.RuleMatchesPair;
import frsf.cidisi.faia.solver.productionsystem.WorkingMemory;

public class ReteMatcher implements Matcher {

	@Override
	public List<RuleMatchesPair> match(ProductionMemory productionMemory, WorkingMemory workingMemory) {
		ReteProductionMemory reteProductionMemory = (ReteProductionMemory) productionMemory;
		reteProductionMemory.matchear();
		return productionMemory.getRules()
				.parallelStream()
				.map(r -> ((ReteRule) r).getMatches()
						.parallelStream()
						.map(m -> new RuleMatchesPair(r, m))
						.collect(Collectors.toList()))
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

}
