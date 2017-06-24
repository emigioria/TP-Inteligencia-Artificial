/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package frsf.cidisi.faia.solver.productionsystem;

import java.util.List;

/**
 * Clase que modela elas reglas del sistema de produccion.
 */
public interface Rule {

	public Integer getId();

	public Integer getSpecificity();

	public Integer getPriority();

	public Integer getNovelty();

	public List<Matches> match();

	public Boolean isActive(List<Matches> matchesList);

	public void execute(Matches unificaciones);

	public boolean finish(Matches value);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();
}
