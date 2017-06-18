/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.solver.productionsystem.criterias;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import frsf.cidisi.faia.agent.productionsystem.Rule;
import frsf.cidisi.faia.solver.productionsystem.Criteria;
import frsf.cidisi.faia.solver.productionsystem.Matches;
import javafx.util.Pair;

/**
 * Clase que implementa el criterio de especificidad.
 */
public class Specificity extends Criteria {

	@Override
	public List<Pair<Rule, Matches>> apply(List<Pair<Rule, Matches>> list) {
		Integer mayor = list.stream().map(prd -> prd.getKey().getSpecificity()).max(Comparator.naturalOrder()).orElse(0);
		return list.stream().filter(prd -> prd.getKey().getSpecificity().equals(mayor)).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return "Specificity (Especificidad)";
	}

}
