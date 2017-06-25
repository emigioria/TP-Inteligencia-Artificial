package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.Filtro;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteMatches;
import frsf.cidisi.faia.solver.productionsystem.Matches;

public class FiltroIgualdad extends Filtro {

	private Integer indicePredicado;
	private Integer indiceHecho;
	private Object filtro;

	public FiltroIgualdad(Integer indicePredicado, Integer indiceHecho, Object filtro) {
		this.indicePredicado = indicePredicado;
		this.indiceHecho = indiceHecho;
		this.filtro = filtro;
	}

	public FiltroIgualdad(Integer indiceHecho, Object filtro) {
		this(0, indiceHecho, filtro);
	}

	@Override
	public List<Matches> filtrar(List<Matches> hechos) {
		return hechos.stream()
				.map(h -> ((ReteMatches) h))
				.filter(rm -> rm.getListaHechos().get(indicePredicado).get(indiceHecho).equals(filtro))
				.collect(Collectors.toList());
	}
}
