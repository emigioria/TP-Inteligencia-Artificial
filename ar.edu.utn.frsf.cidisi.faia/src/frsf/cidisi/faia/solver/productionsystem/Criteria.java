/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.solver.productionsystem;

import java.util.List;

import frsf.cidisi.faia.agent.productionsystem.Rule;
import javafx.util.Pair;

/**
 * Clase que engloba el comportamiento de los criterios del sistema de produccion.
 */
public abstract class Criteria {

	public abstract List<Pair<Rule, Matches>> apply(List<Pair<Rule, Matches>> list);

	@Override
	public abstract String toString();

}
