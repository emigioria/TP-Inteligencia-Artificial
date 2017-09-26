/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

public class AxiomaTieneRiesgo extends Axioma {

	private String palabra;

	private String valor;

	public AxiomaTieneRiesgo(Incidente incidente, String palabra, String valor) {
		super(incidente);
		this.palabra = palabra;
		this.valor = valor;
	}

	public String getPalabra() {
		return palabra;
	}

	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "tieneRiesgo(" + this.getIncidente() + ", " + palabra + ", " + valor + ")";
	}
}
