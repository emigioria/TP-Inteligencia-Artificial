/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.Guardian.productionsystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

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

	public List<ReteWorkingMemoryChangeListener> suscriptores = new Vector<>();

	private Integer proximoIndice = 0;

	private Set<String> palabrasRelevantes = new HashSet<>();

	public EstadoGuardian(String prologFile) throws PrologConnectorException {
		plc = new PrologConnector(prologFile);
		this.initState();
	}

	public EstadoGuardian(PrologConnector prologConnector) throws PrologConnectorException {
		plc = prologConnector;
		this.initState();
	}

	/**
	 * This method is optional, and sets the initial state of the agent.
	 */
	@Override
	public void initState() {
		setPalabrasRelevantes();
	}

	/**
	 * This method is used to update the Agent State when a Perception is
	 * received by the Simulator.
	 */
	@Override
	public void updateState(Perception p) {
		GuardianPerception gPerception = (GuardianPerception) p;
		Preprocesador preprocesador;
		try{
			preprocesador = new Preprocesador(this.palabrasRelevantes);
		} catch(Exception e){
			e.printStackTrace();
			return;
		}

		List<List<String>> listaDeListasDeSinonimos = preprocesador.procesar(gPerception);

		if(listaDeListasDeSinonimos.isEmpty()){
			return;
		}

		//Agregamos las palabras escuchadas a la memoria de trabajo
		listaDeListasDeSinonimos.forEach(listaDeSinonimos -> {
			listaDeSinonimos.forEach(palabra -> this.addPredicate("escuchada(" + palabra + "," + proximoIndice + ")"));
			if(!listaDeSinonimos.isEmpty()){
				proximoIndice++;
			}
		});
	}

	/**
	 * Este método solicita a prolog todas las palabras identificables por Guardian y las retorna.
	 *
	 * @return
	 */
	private void setPalabrasRelevantes() {
		Collection<Map<String, String>> resultado = this.query("tieneRiesgo(Incidente, Palabra, Valor)");

		resultado.parallelStream().forEach(mapa -> {
			//El valor asociado a la clave "Palabra" es una palabra o un simbolo con formato palabra_palabra_palabra
			StringTokenizer fraseTokenizer = new StringTokenizer(mapa.get("Palabra"), "_");
			while(fraseTokenizer.hasMoreTokens()){
				palabrasRelevantes.add(fraseTokenizer.nextToken());
			}
		});
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
	public synchronized void addPredicate(String predicate) {
		plc.addPredicate(predicate);
		suscriptores.parallelStream().forEach(s -> s.cambio(predicate));
	}

	@Override
	public synchronized void removePredicate(String predicate) {
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
