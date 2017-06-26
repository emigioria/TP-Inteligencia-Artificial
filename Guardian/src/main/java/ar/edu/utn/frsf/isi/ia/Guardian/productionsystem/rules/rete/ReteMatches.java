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

	private List<Hecho> hechos = new ArrayList<>();

	public ReteMatches() {
		super();
	}

	public Hecho getHecho(Integer indice) {
		return hechos.get(indice);
	}

	public void addHecho(Hecho hecho) {
		this.hechos.add(hecho);
	}

	public List<Hecho> getHechos() {
		return hechos;
	}

	@Override
	public ReteMatches clone() {
		ReteMatches rm = new ReteMatches();
		rm.hechos = new ArrayList<>(this.hechos);
		return rm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hechos == null) ? 0 : hechos.hashCode());
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
		if(hechos == null){
			if(other.hechos != null){
				return false;
			}
		}
		else if(!hechos.equals(other.hechos)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String salida = "{";
		for(int i = 0; i < hechos.size(); i++){
			salida += hechos.get(i);
			if(i != hechos.size() - 1){
				salida += ", ";
			}
			else{
				salida += "}";
			}
		}
		return salida;
	}
}
