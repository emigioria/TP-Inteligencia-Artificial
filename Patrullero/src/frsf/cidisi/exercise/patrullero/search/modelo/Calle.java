package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.ArrayList;
import java.util.List;

public class Calle {
	private Long id;
	private List<Arista> tramos = new ArrayList<>();
	private String nombre;

	public Calle(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}

	public List<Arista> getTramos() {
		return tramos;
	}

	public void setTramos(List<Arista> tramos) {
		this.tramos = tramos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
		Calle other = (Calle) obj;
		if(id != null && id.equals(other.id)){
			return true;
		}
		return false;
	}

}
