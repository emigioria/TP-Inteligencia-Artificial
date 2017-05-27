package frsf.cidisi.exercise.patrullero.search;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import frsf.cidisi.exercise.patrullero.search.actions.Avanzar;
import frsf.cidisi.exercise.patrullero.search.actions.CambiarOrientacion;
import frsf.cidisi.exercise.patrullero.search.modelo.EstrategiasDeBusqueda;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import frsf.cidisi.exercise.patrullero.search.modelo.TipoIncidente;
import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.Problem;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgent;
import frsf.cidisi.faia.solver.search.Search;
import frsf.cidisi.faia.solver.search.SearchSolveParam;
import frsf.cidisi.faia.solver.search.Strategy;

public class Patrullero extends SearchBasedAgent {

	private EstrategiasDeBusqueda estrategia;
	private ChangeListenerPatrullero avisarCambios;
	private TipoIncidente tipoIncidente;
	private Action selectedAction;

	public Patrullero(Mapa mapa, Interseccion posicionPatrullero, Interseccion posicionIncidente, TipoIncidente tipoIncidente) {
		this.tipoIncidente = tipoIncidente;

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
		if(avisarCambios != null){
			//Retorna si se debe continuar
			if(!avisarCambios.cambio()){
				System.out.println("Simulación cancelada");
				return null;
			}
		}

		// Create the search strategy
		Strategy strategy = estrategia.getInstance();

		// Create a Search object with the strategy
		Search searchSolver = new Search(strategy);

		/*
		 * Generate an XML file with the search tree. It can also be generated
		 * in other formats like PDF with PDF_TREE
		 */
		searchSolver.setVisibleTree(Search.TipoArbol.EFAIA_TREE);

		// Set the Search searchSolver.
		this.setSolver(searchSolver);

		// Ask the solver for the best action
		this.selectedAction = null;
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

	@Override
	public EstadoPatrullero getAgentState() {
		return (EstadoPatrullero) super.getAgentState();
	}

	@Override
	public String getGoalString() {
		return tipoIncidente + " en " + getAgentState().getIncidente() + ". Enviando patrullero.";
	}

	public EstrategiasDeBusqueda getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(EstrategiasDeBusqueda estrategia) {
		this.estrategia = estrategia;
	}

	public void setAvisarCambios(ChangeListenerPatrullero avisar) {
		this.avisarCambios = avisar;
	}

	public String getSelectedActionStr() {
		if(selectedAction == null){
			return "Arrancando patrullero";
		}
		else{
			return selectedAction.toString();
		}
	}

	@Override
	public void fuisteExitoso() {
		//Patrullero feliz
		if(avisarCambios != null){
			avisarCambios.finSimulacionExitosa();
		}
	}

	@Override
	public void noFuisteExitoso() {
		//Patrullero triste
		if(avisarCambios != null){
			avisarCambios.finSimulacionNoExitosa();
		}
	}

}
