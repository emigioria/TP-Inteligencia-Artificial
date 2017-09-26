/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.initValues.modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Incidente {

	private StringProperty nombre = new SimpleStringProperty();

	public Incidente(String nombre) {
		super();
		this.nombre.set(nombre);
	}

	public String getNombre() {
		return nombre.get();
	}

	public void setNombre(String nombre) {
		this.nombre.set(nombre);
	}

	@Override
	public String toString() {
		return nombre.get();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		Incidente other = (Incidente) obj;
		if(nombre == null){
			if(other.nombre != null){
				return false;
			}
		}
		else if(!nombre.equals(other.nombre)){
			return false;
		}
		return true;
	}

}
