/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;

public class Hecho {

	private List<Object> valores;

	public Hecho(List<Object> valores) {
		assert valores != null;

		this.valores = valores;
	}

	public Object get(Integer indice) {
		return valores.get(indice);
	}
}
