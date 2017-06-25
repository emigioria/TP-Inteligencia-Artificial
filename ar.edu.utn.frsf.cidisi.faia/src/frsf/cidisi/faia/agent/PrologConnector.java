/*
 * Copyright 2007-2009 Georgina Stegmayer, Milagros Gutiérrez, Jorge Roa,
 * Luis Ignacio Larrateguy y Milton Pividori.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package frsf.cidisi.faia.agent;

import java.util.Map;

import org.jpl7.JPL;
import org.jpl7.PrologException;
import org.jpl7.Query;
import org.jpl7.Term;

import frsf.cidisi.faia.exceptions.PrologConnectorException;

/**
 * This is the knowledge base used by the agent. It offers some methods
 * to easily consult for the agent's state, and adding new knowledge.
 */
public class PrologConnector {

	/**
	 * The knowledge base file written by the user.
	 */
	private String prologFile;

	public PrologConnector(String prologFile) throws PrologConnectorException {

		this.prologFile = prologFile;

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
		} catch(UnsatisfiedLinkError ex){
			throw new PrologConnectorException("SWI-Prolog is not installed: " +
					ex.getMessage(), ex);
		}

		// Load the knowledge base
		Query prologQuery;
		prologQuery = new Query("consult('" + this.prologFile + "')");

		try{
			new Query("style_check(-discontiguous)").hasSolution();

			prologQuery.hasSolution();
		} catch(PrologException e){
			throw new PrologConnectorException("Load of prolog file failed ('" +
					this.prologFile + "').", e);
		}
	}

	public void executeNonQuery(String query) {
		queryHasSolution(query);
	}

	private String preparePredicate(String predicate) {
		String procesedPredicate = predicate;
		if(predicate.endsWith(".")){
			procesedPredicate = predicate.substring(0, predicate.length() - 1);
		}

		return procesedPredicate;
	}

	public void addPredicate(String predicate) {
		String preparedPredicate = this.preparePredicate(predicate);

		Query query = new Query("asserta(" + preparedPredicate + ")");
		query.hasSolution();
	}

	public void removePredicate(String predicate) {
		String preparedPredicate = this.preparePredicate(predicate);

		Query query = new Query("retract(" + preparedPredicate + ")");
		query.hasSolution();
	}

	public Map<String, Term>[] query(String query) {
		Query prologQuery = new Query(query);
		return prologQuery.allSolutions();
	}

	public boolean queryHasSolution(String query) {
		Query prologQuery = new Query(query);
		return prologQuery.hasSolution();
	}
}
