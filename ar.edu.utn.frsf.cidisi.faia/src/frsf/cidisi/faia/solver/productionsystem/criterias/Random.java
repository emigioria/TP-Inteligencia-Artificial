/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.solver.productionsystem.criterias;

import java.util.ArrayList;
import java.util.List;

import frsf.cidisi.faia.agent.productionsystem.Rule;
import frsf.cidisi.faia.solver.productionsystem.Criteria;
import frsf.cidisi.faia.solver.productionsystem.Matches;
import javafx.util.Pair;

/**
 * Clase que implementa el criterio aleatorio.
 */
public class Random extends Criteria {

	@Override
	public List<Pair<Rule, Matches>> apply(List<Pair<Rule, Matches>> list) {
		int valor = new java.util.Random(System.currentTimeMillis()).nextInt(list.size());

		List<Pair<Rule, Matches>> ret = new ArrayList<>();
		ret.add(list.get(valor));
		return ret;
	}

	@Override
	public String toString() {
		return "Random (Aleatorio)";
	}

}
