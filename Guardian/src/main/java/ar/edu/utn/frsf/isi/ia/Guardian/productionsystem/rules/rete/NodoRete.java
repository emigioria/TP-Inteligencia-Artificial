package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;
import java.util.stream.Collectors;

import frsf.cidisi.faia.solver.productionsystem.Matches;

public abstract class NodoRete {

	protected List<NodoRete> salidas;

	public void propagarHechos(List<Matches> hechos) {
		salidas.parallelStream().forEach(s -> s.propagarHechos(
				hechos.parallelStream()
						.map(m -> ((ReteMatches) m).clone())
						.collect(Collectors.toList())));
	}

	public void agregarSalida(NodoRete salida) {
		salidas.add(salida);
	}

}
