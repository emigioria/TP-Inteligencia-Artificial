/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.comun.ConversorTiempos;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.comun.FormateadorString;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.PresentadorVentanas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public abstract class ControladorJavaFX {

	protected Stage stage;

	protected ConversorTiempos conversorTiempos = new ConversorTiempos();

	protected FormateadorString formateadorString = new FormateadorString();

	protected PresentadorVentanas presentadorVentanas = new PresentadorVentanas();

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	private final void initialize() {
		Platform.runLater(() -> {
			inicializar();
		});
	}

	protected abstract void inicializar();

	@FXML
	protected abstract void salir();

}
