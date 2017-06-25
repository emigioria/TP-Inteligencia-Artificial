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
								lh.forEach(h -> rmu.getListaHechos().set(this.lugar++, h));
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
						lh.forEach(h -> rmu.getListaHechos().set(this.lugar++, h));
						return rmu;
					}).collect(Collectors.toList());
		}

		if(++unionesPasadas == unionesEsperadas){
			super.propagarHechos(union);
		}
	}
}
