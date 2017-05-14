package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Interseccion extends Lugar {
	private Long id;
	private List<Arista> entrantes = new ArrayList<>();
	private List<Arista> salientes = new ArrayList<>();

	public Interseccion(Long peso) {
		super(peso);
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		Interseccion other = (Interseccion) obj;
		if(id != null && id.equals(other.id)){
			return true;
		}
		return false;
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
}
