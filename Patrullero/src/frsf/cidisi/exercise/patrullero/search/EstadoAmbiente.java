package frsf.cidisi.exercise.patrullero.search;

import java.util.Collection;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import frsf.cidisi.faia.state.EnvironmentState;

/**
 * This class represents the real world state.
 */
public class EstadoAmbiente extends EnvironmentState {

	private Mapa mapa;
	private Interseccion posicionAgente;
	private ListIterator<Arista> orientacionAgente;
	private Set<Obstaculo> obstaculos;
	private Double hora; //TODO ver
	private boolean agenteEnCorteTotal = false;

	public EstadoAmbiente(Mapa mapa, Interseccion posicionAgente) {
		super();
		this.mapa = mapa;
		this.posicionAgente = posicionAgente;
		this.initState();
	}

	/**
	 * This method is used to setup the initial real world.
	 */
	@Override
	public void initState() {
		initOrientacion();
		this.obstaculos = mapa.getObstaculos();
		hora = 0D;
	}

	/**
	 * String representation of the real world state.
	 */
	@Override
	public String toString() {
		String str = "";
		//TODO: Complete Method
		return str;
	}

	public Collection<? extends Obstaculo> getObstaculosVisiblesAgente() {
		//TODO falta quitar los obstaculos según el tiempo
		return obstaculos.stream().filter(obs -> obs.sosVisible(posicionAgente)).collect(Collectors.toSet());
	}

	// The following methods are agent-specific:
	public Mapa getMapa() {
		return mapa;
	}

	public void setMapa(Mapa mapa) {
		this.mapa = mapa;
	}

	public Interseccion getPosicionAgente() {
		return posicionAgente;
	}

	public void setPosicionAgente(Interseccion posicionAgente) {
		this.posicionAgente = posicionAgente;
	}

	public ListIterator<Arista> getOrientacionAgente() {
		return orientacionAgente;
	}

	public void setOrientacionAgente(ListIterator<Arista> orientacionAgente) {
		this.orientacionAgente = orientacionAgente;
	}

	public Set<Obstaculo> getObstaculos() {
		return obstaculos;
	}

	public void setObstaculos(Set<Obstaculo> obstaculos) {
		this.obstaculos = obstaculos;
	}

	public Double getHora() {
		return hora;
	}

	public void setHora(Double hora) {
		this.hora = hora;
	}

	public void addHora(Double cost) {
		hora += cost;
	}

	public void initOrientacion() {
		this.orientacionAgente = posicionAgente.getSalientes().listIterator();
	}

	public void setAgenteEnCorteTotal(boolean agenteEnCorteTotal) {
		this.agenteEnCorteTotal = agenteEnCorteTotal;
	}

	public boolean estaAgenteEnCorteTotal() {
		return agenteEnCorteTotal;
	}
}
