package frsf.cidisi.exercise.patrullero.search.modelo;

import java.util.ArrayList;
import java.util.List;

public abstract class Lugar {
	private Long id;
	private Integer peso;
	private List<Obstaculo> obstaculos = new ArrayList<>();

	public Lugar(Long id, Integer peso) {
		this.id = id;
		this.peso = peso;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public List<Obstaculo> getObstaculos() {
		return obstaculos;
	}

	public void setObstaculos(List<Obstaculo> obstaculos) {
		this.obstaculos = obstaculos;
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
		Lugar other = (Lugar) obj;
		if(id != null && id.equals(other.id)){
			return true;
		}
		return false;
	}

	public boolean sosArista() {
		return false;
	}

	public boolean sosInterseccion() {
		return false;
	}
}
