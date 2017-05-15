/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui;

import javafx.fxml.FXML;

public abstract class ControladorDialogo extends ControladorJavaFX {

	@FXML
	protected void salir() {
		stage.hide();
	}
}
