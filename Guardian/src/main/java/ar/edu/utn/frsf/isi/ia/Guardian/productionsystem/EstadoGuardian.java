/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jpl7.Term;

import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteWorkingMemory;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.rules.rete.ReteWorkingMemoryChangeListener;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.PrologConnector;
import frsf.cidisi.faia.agent.productionsystem.ProductionSystemBasedAgentState;
import frsf.cidisi.faia.exceptions.PrologConnectorException;

/**
 * Represent the internal state of the Agent.
 */
public class EstadoGuardian extends ProductionSystemBasedAgentState implements ReteWorkingMemory {

	public PrologConnector plc;

	public List<ReteWorkingMemoryChangeListener> suscriptores = new ArrayList<>();

	public EstadoGuardian(String prologFile) throws PrologConnectorException {
		plc = new PrologConnector(prologFile);
		this.initState();
	}

	/**
	 * This method is optional, and sets the initial state of the agent.
	 */
	@Override
	public void initState() {

	}

	/**
	 * This method is used to update the Agent State when a Perception is
	 * received by the Simulator.
	 */
	@Override
	public void updateState(Perception p) {

	}

	@Override
	public boolean queryHasSolution(String query) {
		return plc.queryHasSolution(query);
	}

	@Override
	public Collection<Map<String, String>> query(String query) {
		List<Map<String, String>> mapas = new ArrayList<>();
		for(Map<String, Term> pares: plc.query(query)){
			Map<String, String> mapa = new Hashtable<>();
			for(Entry<String, Term> entrada: pares.entrySet()){
				mapa.put(entrada.getKey(), entrada.getValue().toString());
			}
			mapas.add(mapa);
		}
		return mapas;
	}

	@Override
	public void addPredicate(String predicate) {
		plc.addPredicate(predicate);
		suscriptores.parallelStream().forEach(s -> s.cambio(predicate));
	}

	@Override
	public void removePredicate(String predicate) {
		plc.removePredicate(predicate);
		suscriptores.parallelStream().forEach(s -> s.cambio(predicate));
	}

	@Override
	public void suscribe(ReteWorkingMemoryChangeListener rwmcl) {
		suscriptores.add(rwmcl);
	}

	@Override
	public String toString() {
		return "Working Memory";
	}

	// The following methods are agent-specific:
}
