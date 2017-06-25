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

public class Unificar extends Filtro {

	private Integer indiceHecho1;
	private Integer indiceValor1;
	private Integer indiceHecho2;
	private Integer indiceValor2;

	public Unificar(Integer indiceHecho1, Integer indiceValor1, Integer indiceHecho2, Integer indiceValor2) {
		this.indiceHecho1 = indiceHecho1;
		this.indiceValor1 = indiceValor1;
		this.indiceHecho2 = indiceHecho2;
		this.indiceValor2 = indiceValor2;
	}

	@Override
	public List<Matches> filtrar(List<Matches> hechos) {
		return hechos.stream()
				.map(m -> ((ReteMatches) m))
				.filter(rm -> rm.getHecho(indiceHecho1).get(indiceValor1).equals(rm.getHecho(indiceHecho2).get(indiceValor2)))
				.collect(Collectors.toList());
	}
}
