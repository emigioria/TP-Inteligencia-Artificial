package frsf.cidisi.exercise.patrullero.search;

import java.util.ListIterator;

import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;

/**
 * Represent the internal state of the Agent.
 */
public class EstadoPatrullero extends SearchBasedAgentState {

	private Mapa mapa;
	private Interseccion posicion;
	private Interseccion incidente;
	private ListIterator<Arista> orientacion;

	public EstadoPatrullero(Mapa mapa, Interseccion posicionAgente, Interseccion posicionIncidente) {
		this.mapa = mapa;
		this.posicion = posicionAgente;
		this.incidente = posicionIncidente;
		this.initState();
	}

	/**
	 * This method is optional, and sets the initial state of the agent.
	 */
	@Override
	public void initState() {
		initOrientacion();
	}

	/**
	 * This method clones the state of the agent. It's used in the search
	 * process, when creating the search tree.
	 */
	@Override
	public EstadoPatrullero clone() {
		EstadoPatrullero estadoPatrullero = new EstadoPatrullero(mapa, posicion, incidente);
		estadoPatrullero.orientacion = estadoPatrullero.posicion.getSalientes().listIterator(orientacion.nextIndex());
		return estadoPatrullero;
	}

	/**
	 * This method is used to update the Agent State when a Perception is
	 * received by the Simulator.
	 */
	@Override
	public void updateState(Perception p) {
		//Poner los obstaculos nuevos en el mapa y ver como borrar los viejos
		//TODO: Complete Method
	}

	/**
	 * This method returns the String representation of the agent state.
	 */
	@Override
	public String toString() {
		String str = "";

		//TODO: Complete Method

		return str;
	}

	/**
	 * This method is used in the search process to verify if the node already
	 * exists in the actual search.
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		EstadoPatrullero other = (EstadoPatrullero) obj;
		if(orientacion == null){
			if(other.orientacion != null){
				return false;
			}
		}
		else if(!orientacion.equals(other.orientacion)){
			return false;
		}
		if(posicion == null){
			if(other.posicion != null){
				return false;
			}
		}
		else if(!posicion.equals(other.posicion)){
			return false;
		}
		return true;
	}

	// The following methods are agent-specific:
	public Mapa getMapa() {
		return mapa;
	}

	public void setMapa(Mapa mapa) {
		this.mapa = mapa;
	}

	public Interseccion getPosicion() {
		return posicion;
	}

	public void setPosicion(Interseccion posicion) {
		this.posicion = posicion;
	}

	public Interseccion getIncidente() {
		return incidente;
	}

	public void setIncidente(Interseccion incidente) {
		this.incidente = incidente;
	}

	public ListIterator<Arista> getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(ListIterator<Arista> orientacion) {
		this.orientacion = orientacion;
	}

	public void initOrientacion() {
		this.orientacion = posicion.getSalientes().listIterator();
	}
}
