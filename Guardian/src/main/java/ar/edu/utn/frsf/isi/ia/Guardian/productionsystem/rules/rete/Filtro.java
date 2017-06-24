package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Filtro<T> extends Nodo {

	private Integer indicePredicado;
	private Integer indiceHecho;
	private T filtro;
	private Function<List<List<Hecho>>, List<List<Hecho>>> filtrar;

	public Filtro(Integer indicePredicado, Integer indiceHecho, T filtro) {
		assert filtro != null;

		this.indicePredicado = indicePredicado;
		this.indiceHecho = indiceHecho;
		this.filtro = filtro;
	}

	public Filtro(Integer indiceHecho, T filtro) {
		this(0, indiceHecho, filtro);
	}

	public Filtro(Function<List<List<Hecho>>, List<List<Hecho>>> filtrar) {
		this.filtrar = filtrar;
	}

	@Override
	public void propagarHechos(List<List<Hecho>> hechos) {
		if(filtro != null){
			List<Hecho> listaFiltrada = hechos.get(indicePredicado).parallelStream().filter(h -> h.get(indiceHecho).equals(filtro)).collect(Collectors.toList());
			hechos.set(indicePredicado, listaFiltrada);
		}
		else{
			hechos = filtrar.apply(hechos);
		}
		super.propagarHechos(hechos);
	}
}
