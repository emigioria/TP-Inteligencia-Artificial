/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.List;

import frsf.cidisi.faia.solver.productionsystem.Matches;
import frsf.cidisi.faia.solver.productionsystem.ProductionMemory;
import frsf.cidisi.faia.solver.productionsystem.Rule;

public class ReteProductionMemory extends NodoRete implements ProductionMemory {

	private List<Rule> reglas;

	public ReteProductionMemory(List<Rule> reglas) {
		this.reglas = reglas;
	}

	public void inicializar() {
		super.propagarHechos(new ArrayList<>());
	}

	@Override
	public List<Rule> getRules() {
		return reglas;
	}

	@Override
	public void propagarHechos(List<Matches> hechos) {
		throw new UnsupportedOperationException();
	}
}
