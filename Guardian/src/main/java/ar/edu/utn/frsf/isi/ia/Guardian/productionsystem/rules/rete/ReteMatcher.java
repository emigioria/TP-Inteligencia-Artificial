/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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
		return productionMemory.getRules()
				.parallelStream()
				.map(r -> ((ReteRule) r).getMatches())
				.flatMap(List::stream)
				.distinct()
				.collect(Collectors.toList());
	}

}
