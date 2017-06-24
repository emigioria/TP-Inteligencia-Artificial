package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Nodo {

	protected List<Nodo> salidas;

	public void propagarHechos(List<List<Hecho>> hechos) {
		salidas.parallelStream().forEach(s -> s.propagarHechos(
				hechos.stream()
						.map(lh -> lh.stream().collect(Collectors.toList()))
						.collect(Collectors.toList())));
	}

	public void agregarSalida(Nodo salida) {
		salidas.add(salida);
	}

}
