/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem;

import java.util.Map;

import org.jpl7.JPL;
import org.jpl7.Query;
import org.jpl7.Term;

import frsf.cidisi.faia.agent.PrologConnector;
import frsf.cidisi.faia.exceptions.PrologConnectorException;

public class MultiUserPrologConnector extends PrologConnector {

	private String agentId;

	public static void iniciarProlog(String prologFile) throws PrologConnectorException {
		/* Set some JPL options */
		try{
			JPL.setDefaultInitArgs(new String[] {
					"pl",
					"-G128m",
					"-L128m",
					"-T128m",
					"--quiet",
					"--nosignals"
			});

			JPL.init();

			new Query("style_check(-discontiguous)").hasSolution();

			if(prologFile != null){
				new Query("consult('" + prologFile + "')").hasSolution();
			}
		} catch(UnsatisfiedLinkError ex){
			throw new PrologConnectorException("SWI-Prolog is not installed: " + ex.getMessage(), ex);
		}
	}

	public MultiUserPrologConnector(String agentId, String prologFile) {
		super();
		this.agentId = agentId;
		this.initWithFile(prologFile);
		this.agentId = "'" + agentId + "':";
	}

	@Override
	public void initWithFile(String prologFile) {
		// Load the knowledge base
		boolean cargaExitosa = new Query("make_module('" + this.agentId + "', '" + prologFile + "')").hasSolution();
		if(!cargaExitosa){
			throw new RuntimeException();
		}
	}

	@Override
	protected String preparePredicate(String predicate) {
		return this.agentId + super.preparePredicate(predicate);
	}

	@Override
	public Map<String, Term>[] query(String query) {
		return super.query(this.agentId + query);
	}

	@Override
	public boolean queryHasSolution(String query) {
		return super.queryHasSolution(this.agentId + query);
	}
}
