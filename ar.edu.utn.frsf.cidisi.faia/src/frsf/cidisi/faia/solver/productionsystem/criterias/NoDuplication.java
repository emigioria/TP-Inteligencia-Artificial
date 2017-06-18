/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.solver.productionsystem.criterias;

import java.util.List;
import java.util.stream.Collectors;

import frsf.cidisi.faia.agent.productionsystem.Rule;
import frsf.cidisi.faia.solver.productionsystem.Criteria;
import frsf.cidisi.faia.solver.productionsystem.Matches;
import javafx.util.Pair;

/**
 * Clase que implementa el criterio de no duplicacion.
 */
public class NoDuplication extends Criteria {

	private UsedRulesExpert usedRulesExpert;

	public NoDuplication(UsedRulesExpert usedRulesExpert) {
		this.usedRulesExpert = usedRulesExpert;
	}

	@Override
	public List<Pair<Rule, Matches>> apply(List<Pair<Rule, Matches>> list) {
		return list.stream().filter(prd -> !usedRulesExpert.used(prd)).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return "No Duplication (No duplicación)";
	}
}
