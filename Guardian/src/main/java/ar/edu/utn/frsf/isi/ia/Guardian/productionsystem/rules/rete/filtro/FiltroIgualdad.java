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

public class FiltroIgualdad extends Filtro {

	private Integer indicePredicado;
	private Integer indiceHecho;
	private Object filtro;

	public FiltroIgualdad(Integer indicePredicado, Integer indiceHecho, Object filtro) {
		this.indicePredicado = indicePredicado;
		this.indiceHecho = indiceHecho;
		this.filtro = filtro;
	}

	public FiltroIgualdad(Integer indiceHecho, Object filtro) {
		this(0, indiceHecho, filtro);
	}

	@Override
	public List<Matches> filtrar(List<Matches> hechos) {
		return hechos.stream()
				.map(h -> ((ReteMatches) h))
				.filter(rm -> rm.getHechos().get(indicePredicado).get(indiceHecho).equals(filtro))
				.collect(Collectors.toList());
	}
}
