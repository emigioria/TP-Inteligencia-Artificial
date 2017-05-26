/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ImagenIncidente;
import frsf.cidisi.exercise.patrullero.search.modelo.TipoIncidente;
import javafx.scene.image.ImageView;

public class IncidenteGUI extends ImageView {

	private InterseccionGUI posicion;

	public IncidenteGUI(TipoIncidente tipoIncidente) {
		super(new ImagenIncidente(tipoIncidente));
		this.setFitHeight(2 * InterseccionGUI.RADIO);
		this.setFitWidth(2 * InterseccionGUI.RADIO);
	}

	public void inicializar(InterseccionGUI posicion) {
		this.posicion = posicion;
		this.setTranslateX(posicion.getTranslateX());
		this.setTranslateY(posicion.getTranslateY());
	}

	public InterseccionGUI getPosicion() {
		return posicion;
	}

}
