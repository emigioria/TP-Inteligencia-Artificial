/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.agent.productionsystem;

import java.util.ArrayList;
import java.util.List;

import frsf.cidisi.faia.solver.productionsystem.ProductionMemory;
import frsf.cidisi.faia.solver.productionsystem.Rule;

public class SimpleProductionMemory implements ProductionMemory {

	private List<Rule> rules = new ArrayList<>();

	public SimpleProductionMemory() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see frsf.cidisi.faia.agent.productionsystem.ProductionMemor#getRules()
	 */
	@Override
	public List<Rule> getRules() {
		return rules;
	}

}
