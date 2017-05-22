/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.controladores;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorPatrullero;

public class VerSimulacionController extends ControladorPatrullero {

	public static final String URL_VISTA = "vistas/VerSimulacion.fxml";

	@Override
	protected void inicializar() {

	}

	@Override
	public Boolean sePuedeSalir() {
		return presentadorVentanas.presentarConfirmacion("Salir", "Se perderan los datos simulados ¿Seguro que desea salir?", stage).acepta();
	}
}
