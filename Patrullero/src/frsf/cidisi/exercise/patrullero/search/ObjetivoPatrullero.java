
package frsf.cidisi.exercise.patrullero.search;

import frsf.cidisi.faia.agent.search.GoalTest;
import frsf.cidisi.faia.state.AgentState;

public class ObjetivoPatrullero extends GoalTest {

	@Override
	public boolean isGoalState(AgentState agentState) {
		EstadoPatrullero estado = (EstadoPatrullero) agentState;
		return estado.getPosicion().equals(estado.getIncidente()); //( posicion = incidente)
	}
}