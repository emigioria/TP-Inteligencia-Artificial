/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.List;

import frsf.cidisi.faia.solver.productionsystem.Matches;

public class ReteMatches implements Matches, Cloneable {

	private List<Hecho> listaHechos;

	public ReteMatches() {
		super();
		this.listaHechos = new ArrayList<>();
	}

	public Hecho getHecho(Integer indice) {
		return listaHechos.get(indice);
	}

	public void addHecho(Hecho hecho) {
		this.listaHechos.add(hecho);
	}

	public List<Hecho> getListaHechos() {
		return listaHechos;
	}

	@Override
	public ReteMatches clone() {
		ReteMatches rm = new ReteMatches();
		rm.listaHechos = new ArrayList<>(this.listaHechos);
		return rm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((listaHechos == null) ? 0 : listaHechos.hashCode());
		return result;
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
		ReteMatches other = (ReteMatches) obj;
		if(listaHechos == null){
			if(other.listaHechos != null){
				return false;
			}
		}
		else if(!listaHechos.equals(other.listaHechos)){
			return false;
		}
		return true;
	}

}
