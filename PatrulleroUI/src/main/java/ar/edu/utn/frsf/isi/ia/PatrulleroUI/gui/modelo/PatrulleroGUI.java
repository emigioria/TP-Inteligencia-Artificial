/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ImagenPatrullero;
import javafx.scene.image.ImageView;

public class PatrulleroGUI extends ImageView {

	private InterseccionGUI posicion;

	public PatrulleroGUI() {
		super(new ImagenPatrullero());
		this.setFitHeight(2 * InterseccionGUI.RADIO);
		this.setFitWidth(2 * InterseccionGUI.RADIO);
	}

	public InterseccionGUI getPosicion() {
		return posicion;
	}

	public void setPosicion(InterseccionGUI posicion) {
		this.posicion = posicion;
		this.setTranslateX(posicion.getNode().getTranslateX());
		this.setTranslateY(posicion.getNode().getTranslateY());
	}

}
