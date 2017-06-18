/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.agent.productionsystem;

import java.util.ArrayList;
import java.util.List;

public class ProductionMemory {

	private List<Rule> rules = new ArrayList<>();

	public ProductionMemory() {

	}

	public List<Rule> getRules() {
		return rules;
	}

}
