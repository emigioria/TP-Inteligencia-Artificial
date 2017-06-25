package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;
import java.util.stream.Collectors;

import frsf.cidisi.faia.solver.productionsystem.Matches;

public class Unificar extends NodoRete {

	private Integer indiceHecho1;
	private Integer indiceValor1;
	private Integer indiceHecho2;
	private Integer indiceValor2;

	public Unificar(Integer indiceHecho1, Integer indiceValor1, Integer indiceHecho2, Integer indiceValor2) {
		this.indiceHecho1 = indiceHecho1;
		this.indiceValor1 = indiceValor1;
		this.indiceHecho2 = indiceHecho2;
		this.indiceValor2 = indiceValor2;
	}

	@Override
	public void propagarHechos(List<Matches> hechos) {
		hechos = hechos.stream()
				.map(m -> ((ReteMatches) m))
				.filter(rm -> rm.getHecho(indiceHecho1).get(indiceValor1).equals(rm.getHecho(indiceHecho2).get(indiceValor2)))
				.collect(Collectors.toList());

		super.propagarHechos(hechos);
	}
}
