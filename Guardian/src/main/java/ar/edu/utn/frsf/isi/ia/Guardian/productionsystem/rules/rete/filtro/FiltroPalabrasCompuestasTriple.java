package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.Filtro;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteMatches;
import frsf.cidisi.faia.solver.productionsystem.Matches;

public class FiltroPalabrasCompuestasTriple extends Filtro {

	public FiltroPalabrasCompuestasTriple() {

	}

	@Override
	public List<Matches> filtrar(List<Matches> hechos) {
		return hechos.stream()
				.map(m -> ((ReteMatches) m))
				.filter(rm -> {
					Integer n = new Integer(rm.getHecho(0).get(1).toString());
					Integer m = new Integer(rm.getHecho(1).get(1).toString());
					return m == n + 1;
				})
				.filter(rm -> {
					Integer m = new Integer(rm.getHecho(1).get(1).toString());
					Integer l = new Integer(rm.getHecho(2).get(1).toString());
					return l == m + 1;
				})
				.collect(Collectors.toList());
	}

}
