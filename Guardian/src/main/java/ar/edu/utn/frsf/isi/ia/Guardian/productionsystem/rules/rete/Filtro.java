package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;
import java.util.stream.Collectors;

import frsf.cidisi.faia.solver.productionsystem.Matches;

public class Filtro extends NodoRete {

	private Integer indicePredicado;
	private Integer indiceHecho;
	private Object filtro;

	public Filtro(Integer indicePredicado, Integer indiceHecho, Object filtro) {
		assert filtro != null;

		this.indicePredicado = indicePredicado;
		this.indiceHecho = indiceHecho;
		this.filtro = filtro;
	}

	public Filtro(Integer indiceHecho, Object filtro) {
		this(0, indiceHecho, filtro);
	}

	@Override
	public void propagarHechos(List<Matches> hechos) {
		hechos = hechos.stream()
				.map(h -> ((ReteMatches) h))
				.filter(rm -> rm.getListaHechos().get(indicePredicado).get(indiceHecho).equals(filtro))
				.collect(Collectors.toList());

		super.propagarHechos(hechos);
	}
}
