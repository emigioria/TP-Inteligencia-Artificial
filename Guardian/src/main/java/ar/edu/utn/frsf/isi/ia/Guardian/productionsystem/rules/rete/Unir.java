/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import frsf.cidisi.faia.solver.productionsystem.Matches;

public class Unir extends NodoRete {

	private Integer unionesEsperadas;
	private Integer lugar;
	private Map<Integer, List<Matches>> cacheHechos = new HashMap<>();

	public Unir(Integer uniones) {
		this.unionesEsperadas = uniones;
	}

	@Override
	public void propagarHechos(List<Matches> hechos) {
		throw new UnsupportedOperationException();
	}

	public synchronized void unirEn(List<Matches> hechos, Integer lugar) {
		cacheHechos.put(lugar, hechos);
		if(cacheHechos.keySet().size() != unionesEsperadas){
			return;
		}

		List<Matches> union = new ArrayList<>();
		Integer unionesPasadas = 0;
		for(Entry<Integer, List<Matches>> es: cacheHechos.entrySet()){
			if(unionesPasadas > 0){
				union = union.stream()
						.map(mu -> ((ReteMatches) mu))
						.map(rmu -> es.getValue().stream()
								.map(mh -> ((ReteMatches) mh).getHechos())
								.map(lhh -> {
									ReteMatches rmuClon = rmu.clone();
									this.lugar = es.getKey();
									lhh.forEach(h -> {
										Integer indice = this.lugar;
										while(indice >= rmuClon.getHechos().size()){
											rmuClon.getHechos().add(null);
										}
										rmuClon.getHechos().set(this.lugar++, h);
									});
									return rmuClon;
								}).collect(Collectors.toList()))
						.flatMap(List::stream)
						.collect(Collectors.toList());
			}
			else{
				union = es.getValue().stream()
						.map(m -> ((ReteMatches) m).getHechos())
						.map(lh -> {
							ReteMatches rmu = new ReteMatches();
							this.lugar = es.getKey();
							lh.forEach(h -> {
								Integer indice = this.lugar;
								while(indice >= rmu.getHechos().size()){
									rmu.getHechos().add(null);
								}
								rmu.getHechos().set(this.lugar++, h);
							});
							return rmu;
						}).collect(Collectors.toList());
			}
			unionesPasadas++;
		}

		super.propagarHechos(union);
	}

}
