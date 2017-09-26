/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class Axioma {

	private ObjectProperty<Incidente> incidente = new SimpleObjectProperty<>();

	public Axioma(Incidente incidente) {
		this.incidente.set(incidente);
	}

	public Incidente getIncidente() {
		return incidente.get();
	}

	public void setIncidente(Incidente incidente) {
		this.incidente.set(incidente);
	}

}
