/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.Filtro;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteMatches;
import frsf.cidisi.faia.solver.productionsystem.Matches;

public class FiltroPalabrasCompuestas extends Filtro {

	public FiltroPalabrasCompuestas() {

	}

	@Override
	public List<Matches> filtrar(List<Matches> hechos) {
		return hechos.stream()
				.map(m -> ((ReteMatches) m))
				.filter(rm -> {
					Integer n = new Integer(rm.getHecho(0).get(1).toString());
					Integer m = new Integer(rm.getHecho(1).get(1).toString());
					return m == n + 1;
				})
				.collect(Collectors.toList());
	}

}
