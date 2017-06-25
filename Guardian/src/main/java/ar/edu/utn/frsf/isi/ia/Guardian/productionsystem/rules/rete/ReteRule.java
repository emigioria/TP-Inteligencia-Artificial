package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.List;

import frsf.cidisi.faia.solver.productionsystem.Matches;
import frsf.cidisi.faia.solver.productionsystem.Rule;

public abstract class ReteRule extends Nodo implements Rule {

	private Integer id;
	private Integer specificity;
	private Integer priority;
	private List<Matches> matches;

	public ReteRule(Integer id, Integer specificity, Integer priority) {
		super();
		this.id = id;
		this.specificity = specificity;
		this.priority = priority;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public Integer getSpecificity() {
		return specificity;
	}

	@Override
	public Integer getPriority() {
		return priority;
	}

	@Override
	public abstract void execute(Matches unificaciones);

	@Override
	public boolean finish(Matches value) {
		return false;
	}
	
	@Override
	public boolean finishLearning(Matches value) {
		return false;
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
		ReteRule other = (ReteRule) obj;
		if(id.equals(other.id)){
			return true;
		}
		return false;
	}

	@Override
	public void agregarSalida(Nodo salida) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void propagarHechos(List<List<Hecho>> hechos) {
		matches = this.generarMatches(hechos);
	}

	public List<Matches> getMatches() {
		return matches;
	}

	public abstract List<Matches> generarMatches(List<List<Hecho>> hechos);
}
