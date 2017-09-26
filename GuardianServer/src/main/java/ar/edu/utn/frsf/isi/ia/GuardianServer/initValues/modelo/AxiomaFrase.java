/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

public class AxiomaFrase {

	private String palabra1;

	private String palabra2;

	public AxiomaFrase(String palabra1, String palabra2) {
		super();
		this.palabra1 = palabra1;
		this.palabra2 = palabra2;
	}

	public String getPalabra1() {
		return palabra1;
	}

	public void setPalabra1(String palabra) {
		this.palabra1 = palabra;
	}

	public String getPalabra2() {
		return palabra2;
	}

	public void setPalabra2(String palabra) {
		this.palabra2 = palabra;
	}

	@Override
	public String toString() {
		return "frase(" + palabra1 + ", " + palabra2 + ")";
	}
}
