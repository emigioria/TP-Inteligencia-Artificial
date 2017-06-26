/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import frsf.cidisi.faia.solver.productionsystem.Matches;

public class Unir extends NodoRete {

	private List<Matches> union = new ArrayList<>();
	private Integer unionesEsperadas;
	private Integer unionesPasadas = 0;
	private Integer lugar;

	public Unir(Integer uniones) {
		this.unionesEsperadas = uniones;
	}

	@Override
	public void propagarHechos(List<Matches> hechos) {
		throw new UnsupportedOperationException();
	}

	public synchronized void unirEn(List<Matches> hechos, Integer lugar) {
		this.lugar = lugar;
		if(unionesPasadas > 0){
			union = union.stream()
					.map(m -> ((ReteMatches) m))
					.map(rm -> hechos.stream()
							.map(m -> ((ReteMatches) m).getListaHechos())
							.map(lh -> {
								ReteMatches rmu = rm.clone();
								lh.forEach(h -> {
									Integer indice = this.lugar;
									while(indice >= rmu.getListaHechos().size()){
										rmu.getListaHechos().add(null);
									}
									rmu.getListaHechos().set(this.lugar++, h);
								});
								return rmu;
							}).collect(Collectors.toList()))
					.flatMap(List::stream)
					.collect(Collectors.toList());
		}
		else{
			union = hechos.stream()
					.map(m -> ((ReteMatches) m).getListaHechos())
					.map(lh -> {
						ReteMatches rmu = new ReteMatches();
						lh.forEach(h -> {
							Integer indice = this.lugar;
							while(indice >= rmu.getListaHechos().size()){
								rmu.getListaHechos().add(null);
							}
							rmu.getListaHechos().set(this.lugar++, h);
						});
						return rmu;
					}).collect(Collectors.toList());
		}

		if(++unionesPasadas == unionesEsperadas){
			super.propagarHechos(union);
		}
	}
}
