/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorDialogo;
import frsf.cidisi.exercise.patrullero.search.modelo.Lugar;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import javafx.fxml.FXML;

public class AltaObstaculoController extends ControladorDialogo {

	public static final String URL_VISTA = "vistas/AltaObstaculo.fxml";

	//TODO hacer

	private Lugar lugar;

	private Obstaculo obstaculo;

	@FXML
	private void guardar() {
		//TODO hacer

		salir();
	}

	@Override
	protected void inicializar() {
		stage.setTitle("Nuevo obstaculo");
		stage.setResizable(false);

		//TODO hacer
	}

	public void setLugar(Lugar lugar) {
		this.lugar = lugar;
	}

	public Obstaculo getResultado() {
		return obstaculo;
	}
}
