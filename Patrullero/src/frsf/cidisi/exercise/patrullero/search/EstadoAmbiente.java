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
	private Arista ultimaCalleRecorridaPorElAgente = null;
	private Interseccion posicionAgente;
	private ListIterator<Arista> orientacionAgente;
	private Set<Obstaculo> obstaculos;
	private Long hora = 0L;
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
	}

	/**
	 * String representation of the real world state.
	 */
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("\n\tTiempo: " + hora + "\n");
		str.append("\tObstáculos de ahora:\n");
		obstaculos.stream().filter(obs -> obs.getTiempoInicio() < hora && obs.getTiempoFin() > hora).forEach(obs -> str.append("\t\t" + obs.toString() + "\n"));
		if(obstaculos.isEmpty()){
			str.append("\t\tNo hay obstaculos a esta hora.\n");
		}
		if(agenteEnCorteTotal){
			str.append("\tEl agente se ha encontrado con un corte total!\n");
		}
		return str.substring(0, str.length() - 1);
	}

	public Collection<? extends Obstaculo> getObstaculosVisiblesAgente() {
		return obstaculos.stream().filter(obs -> obs.getTiempoInicio() <= hora && obs.getTiempoFin() > hora && obs.sosVisible(posicionAgente, ultimaCalleRecorridaPorElAgente))
				.map(obs -> obs.clone())
				.collect(Collectors.toSet());
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

	public Long getHora() {
		return hora;
	}

	public void setHora(Long hora) {
		this.hora = hora;
	}

	public void addHora(Long cost) {
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

	public Arista getUltimaCalleRecorridaPorElAgente() {
		return ultimaCalleRecorridaPorElAgente;
	}

	public void setUltimaCalleRecorridaPorElAgente(Arista ultimaCalleRecorridaPorElAgente) {
		this.ultimaCalleRecorridaPorElAgente = ultimaCalleRecorridaPorElAgente;
	}
}
