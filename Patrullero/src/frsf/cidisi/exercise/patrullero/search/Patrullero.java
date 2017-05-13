package frsf.cidisi.exercise.patrullero.search;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import frsf.cidisi.exercise.patrullero.search.actions.Avanzar;
import frsf.cidisi.exercise.patrullero.search.actions.CambiarOrientacion;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.Problem;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgent;
import frsf.cidisi.faia.solver.search.DepthFirstSearch;
import frsf.cidisi.faia.solver.search.Search;
import frsf.cidisi.faia.solver.search.SearchSolveParam;

public class Patrullero extends SearchBasedAgent {

	public Patrullero(Mapa mapa, Interseccion posicionPatrullero, Interseccion posicionIncidente) {

		// The Agent Goal
		ObjetivoPatrullero agGoal = new ObjetivoPatrullero();

		// The Agent State
		EstadoPatrullero agState = new EstadoPatrullero(mapa, posicionPatrullero, posicionIncidente);
		this.setAgentState(agState);

		// Create the operators
		Vector<SearchAction> operators = new Vector<>();
		operators.addElement(new Avanzar());
		operators.addElement(new CambiarOrientacion());

		// Create the Problem which the agent will resolve
		Problem problem = new Problem(agGoal, agState, operators);
		this.setProblem(problem);
	}

	/**
	 * This method is executed by the simulator to ask the agent for an action.
	 */
	@Override
	public Action selectAction() {

		// Create the search strategy
		DepthFirstSearch strategy = new DepthFirstSearch();

		// Create a Search object with the strategy
		Search searchSolver = new Search(strategy);

		/*
		 * Generate an XML file with the search tree. It can also be generated
		 * in other formats like PDF with PDF_TREE
		 */
		searchSolver.setVisibleTree(Search.GRAPHVIZ_TREE);

		// Set the Search searchSolver.
		this.setSolver(searchSolver);

		// Ask the solver for the best action
		Action selectedAction = null;
		try{
			selectedAction =
					this.getSolver().solve(new SearchSolveParam(this.getProblem()));
		} catch(Exception ex){
			Logger.getLogger(Patrullero.class.getName()).log(Level.SEVERE, null, ex);
		}

		// Return the selected action
		return selectedAction;

	}

	/**
	 * This method is executed by the simulator to give the agent a perception.
	 * Then it updates its state.
	 *
	 * @param p
	 */
	@Override
	public void see(Perception p) {
		this.getAgentState().updateState(p);
	}
}
