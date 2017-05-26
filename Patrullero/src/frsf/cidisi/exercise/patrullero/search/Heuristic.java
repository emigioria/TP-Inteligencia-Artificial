package frsf.cidisi.exercise.patrullero.search;

import frsf.cidisi.faia.solver.search.IEstimatedCostFunction;
import frsf.cidisi.faia.solver.search.NTree;

/**
 * This class allows to define a function to be used by any
 * informed search strategy, like A Star or Greedy.
 */
public class Heuristic implements IEstimatedCostFunction {

	private static final double VELOCIDAD_PATRULLERO = 25;

	/**
	 * It returns the estimated cost to reach the goal from a NTree node.
	 */
	@Override
	public double getEstimatedCost(NTree node) {
		EstadoPatrullero estadoPatrullero = (EstadoPatrullero) node.getAgentState();
		Double x1 = estadoPatrullero.getIncidente().getCoordenadaX();
		Double y1 = estadoPatrullero.getIncidente().getCoordenadaY();
		Double x2 = estadoPatrullero.getPosicion().getCoordenadaX();
		Double y2 = estadoPatrullero.getPosicion().getCoordenadaY();
		Double distanciaX = x1 - x2;
		Double distanciaY = y1 - y2;
		return Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY) / VELOCIDAD_PATRULLERO;
	}
}
