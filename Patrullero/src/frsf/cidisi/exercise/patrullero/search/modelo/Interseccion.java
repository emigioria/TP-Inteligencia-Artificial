package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Interseccion extends Lugar {
	private List<Arista> entrantes = new ArrayList<>();
	private List<Arista> salientes = new ArrayList<>();

	public Interseccion(Long id, Integer peso) {
		super(id, peso);
	}

	public List<Arista> getEntrantes() {
		return entrantes;
	}

	public void setEntrantes(List<Arista> entrantes) {
		this.entrantes = entrantes;
	}

	public List<Arista> getSalientes() {
		return salientes;
	}

	public void setSalientes(List<Arista> salientes) {
		this.salientes = salientes;
	}

	@Override
	public boolean sosInterseccion() {
		return true;
	}

	public Set<Lugar> getLugaresVisibles() {
		Set<Lugar> lugaresVisibles = new HashSet<>();
		lugaresVisibles.addAll(this.getEntrantes());
		lugaresVisibles.addAll(this.getSalientes());
		lugaresVisibles.addAll(this.getEntrantes().stream().map(a -> a.getDestino()).collect(Collectors.toSet()));
		lugaresVisibles.addAll(this.getSalientes().stream().map(a -> a.getOrigen()).collect(Collectors.toSet()));
		lugaresVisibles.add(this);
		return lugaresVisibles;
	}

	@Override
	public String toString() {
		Set<String> nombresCalles = Stream.concat(
				entrantes.stream().map(e -> e.toString()),
				salientes.stream().map(s -> s.toString()))
				.collect(Collectors.toSet());
		StringBuffer str = new StringBuffer();
		nombresCalles.stream().forEach(calle -> str.append(calle + " # "));
		str.delete(str.length() - 3, str.length());
		if(nombresCalles.size() == 1){
			str.append(" (Callejón sin salida)");
		}
		return str.toString();
	}
}
