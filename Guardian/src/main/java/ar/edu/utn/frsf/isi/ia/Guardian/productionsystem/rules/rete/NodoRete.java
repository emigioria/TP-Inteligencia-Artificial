/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import frsf.cidisi.faia.solver.productionsystem.Matches;

public abstract class NodoRete {

	protected List<NodoRete> salidas = new Vector<>();

	public void propagarHechos(List<Matches> hechos) {
		salidas.parallelStream().forEach(s -> s.propagarHechos(
				hechos.parallelStream()
						.map(m -> ((ReteMatches) m).clone())
						.collect(Collectors.toList())));
	}

	public void agregarSalida(NodoRete salida) {
		salidas.add(salida);
	}

}
