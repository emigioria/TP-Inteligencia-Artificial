package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jpl7.Term;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteWorkingMemory;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.PrologConnector;
import frsf.cidisi.faia.exceptions.PrologConnectorException;

/**
 * Represent the internal state of the Agent.
 */
public class EstadoGuardian implements ReteWorkingMemory {

	public PrologConnector plc;

	public EstadoGuardian(String prologFile) throws PrologConnectorException {
		plc = new PrologConnector(prologFile);
		this.initState();
	}

	/**
	 * This method is optional, and sets the initial state of the agent.
	 */
	public void initState() {

	}

	/**
	 * This method is used to update the Agent State when a Perception is
	 * received by the Simulator.
	 */
	@Override
	public void updateState(Perception p) {

	}

	// The following methods are ReteWorkingMemory-specific:
	@Override
	public Collection<Map<String, String>> query(String query) {
		List<Map<String, String>> mapas = new ArrayList<>();
		for(Map<String, Term> pares: plc.query(query)){
			Map<String, String> mapa = new Hashtable<>();
			for(Entry<String, Term> entrada: pares.entrySet()){
				mapa.put(entrada.getKey(), entrada.getValue().toString());
			}
		}
		return mapas;
	}

	@Override
	public void addPredicate(String predicate) {
		plc.addPredicate(predicate);
	}

	@Override
	public void removePredicate(String predicate) {
		plc.removePredicate(predicate);
	}

	@Override
	public boolean queryHasSolution(String query) {
		return plc.queryHasSolution(query);
	}

	// The following methods are agent-specific:
}
