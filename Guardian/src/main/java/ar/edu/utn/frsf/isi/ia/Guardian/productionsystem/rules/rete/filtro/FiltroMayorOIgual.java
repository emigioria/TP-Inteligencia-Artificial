package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.filtro;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.FiltroFuncion;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteMatches;
import frsf.cidisi.faia.solver.productionsystem.Matches;

public class FiltroMayorOIgual extends FiltroFuncion {

	public FiltroMayorOIgual() {

	}

	@Override
	public List<Matches> filtrar(List<Matches> hechos) {
		return hechos.stream()
				.map(m -> ((ReteMatches) m))
				.filter(rm -> {
					Integer limite = new Integer(rm.getHecho(0).get(1).toString());
					Integer nivel = new Integer(rm.getHecho(1).get(1).toString());
					return nivel >= limite;
				})
				.collect(Collectors.toList());
	}

}
