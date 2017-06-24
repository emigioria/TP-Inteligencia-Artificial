/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.solver.productionsystem;

import java.util.List;

/**
 * Clase que engloba el comportamiento de los criterios del sistema de produccion.
 */
public abstract class Criteria {

	public abstract List<RuleMatchesPair> apply(List<RuleMatchesPair> list);

	@Override
	public abstract String toString();

}
