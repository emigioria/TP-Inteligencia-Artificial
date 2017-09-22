/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem;

import java.util.StringTokenizer;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.EstadoAmbiente;

/**
 * This class represents the real world state.
 */
public class EstadoAmbienteDinamico extends EstadoAmbiente {

	private StringTokenizer frasesEscuchadasTokenizer;

	public EstadoAmbienteDinamico() {
		super();
		this.initState();
	}

	@Override
	public String getNextFrase() {
		if(frasesEscuchadasTokenizer.hasMoreTokens()){
			return frasesEscuchadasTokenizer.nextToken();
		}
		return null;
	}

	/**
	 * This method is used to setup the initial real world.
	 */
	@Override
	public void initState() {

	}

	@Override
	public void setFrasesDichas(String frases) {
		frasesEscuchadasTokenizer = new StringTokenizer(frases, "\n\f\r.;¿?¡!");
	}

	@Override
	public String toString() {
		return "Estado ambiente.";
	}

	// The following methods are agent-specific:
}
