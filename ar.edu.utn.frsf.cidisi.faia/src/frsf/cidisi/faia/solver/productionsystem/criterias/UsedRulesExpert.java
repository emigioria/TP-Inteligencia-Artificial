/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.solver.productionsystem.criterias;

import frsf.cidisi.faia.solver.productionsystem.RuleMatchesPair;

public interface UsedRulesExpert {

	public boolean used(RuleMatchesPair prd);

}
