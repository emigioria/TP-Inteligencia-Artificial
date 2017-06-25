package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete;

import java.util.Collection;
import java.util.Map;

import frsf.cidisi.faia.solver.productionsystem.WorkingMemory;

/**
 * Represent the internal state of the Agent.
 */
public interface ReteWorkingMemory extends WorkingMemory {

	public boolean queryHasSolution(String query);

	public Collection<Map<String, String>> query(String query);

	public void addPredicate(String predicate);

	public void removePredicate(String predicate);

	public void suscribe(ReteWorkingMemoryChangeListener rwmcl);
}
