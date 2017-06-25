/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;

import frsf.cidisi.faia.solver.productionsystem.Matches;

public abstract class Filtro extends NodoRete {

	public Filtro() {

	}

	@Override
	public void propagarHechos(List<Matches> hechos) {
		super.propagarHechos(this.filtrar(hechos));
	}

	public abstract List<Matches> filtrar(List<Matches> hechos);
}
