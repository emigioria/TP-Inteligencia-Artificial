package frsf.cidisi.exercise.patrullero.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Lugar;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import frsf.cidisi.exercise.patrullero.search.modelo.Visibilidad;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;

/**
 * Represent the internal state of the Agent.
 */
public class EstadoPatrullero extends SearchBasedAgentState {

	private Mapa mapa;
	private Arista ultimaCalleRecorrida = null;
	private Interseccion posicion;
	private Interseccion incidente;
	private ListIterator<Arista> orientacion;
	private Map<Visibilidad, Set<Obstaculo>> obstaculos = new HashMap<>();

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
		PatrulleroPerception patrulleroPerception = (PatrulleroPerception) p;
		Map<Visibilidad, Set<Obstaculo>> obstaculosPercibidosPorVisibilidad = patrulleroPerception.getobstaculos_detectables().stream().collect(Collectors.groupingBy(Obstaculo::getVisibilidad, Collectors.toSet()));
		for(Visibilidad visibilidad: obstaculosPercibidosPorVisibilidad.keySet()){
			if(visibilidad.sosInformado()){
				Set<Obstaculo> obstaculosInformadosViejos = obstaculos.get(visibilidad);
				Set<Obstaculo> obstaculosInformadosNuevos = obstaculosPercibidosPorVisibilidad.get(visibilidad);
				obstaculosInformadosViejos.stream().filter(obs -> !obstaculosInformadosNuevos.contains(obs)).forEach(obs -> {
					obstaculosInformadosViejos.remove(obs);
					obs.getLugar().getObstaculos().remove(obs);
				});
				obstaculosInformadosNuevos.stream().filter(obs -> !obstaculosInformadosViejos.contains(obs)).forEach(obs -> {
					obstaculosInformadosViejos.add(obs);
					Lugar lugarObstaculo = mapa.getLugar(obs.getLugar());
					lugarObstaculo.getObstaculos().add(obs);
					obs.setLugar(lugarObstaculo);
				});
			}
			else if(visibilidad.sosVisible()){
				Set<Obstaculo> obstaculosVisiblesViejos = obstaculos.get(visibilidad);
				Set<Obstaculo> obstaculosVisiblesNuevos = obstaculosPercibidosPorVisibilidad.get(visibilidad);
				Set<Lugar> lugaresVisibles = posicion.getLugaresVisibles();
				lugaresVisibles.stream().forEach(l -> {
					l.getObstaculos().removeIf(obs -> {
						if(obs.getVisibilidad().sosVisible()){
							obstaculosVisiblesViejos.remove(obs);
							return true;
						}
						else{
							return false;
						}
					});
				});
				obstaculosVisiblesNuevos.stream().forEach(obs -> {
					obstaculosVisiblesViejos.add(obs);
					Lugar lugarObstaculo = lugaresVisibles.stream().filter(l -> l.equals(obs.getLugar())).findFirst().get();
					lugarObstaculo.getObstaculos().add(obs);
					obs.setLugar(lugarObstaculo);
				});
			}
			else if(visibilidad.sosInvisible()){
				Set<Obstaculo> obstaculosInvisiblesViejos = obstaculos.get(visibilidad);
				Set<Obstaculo> obstaculosInvisiblesNuevos = obstaculosPercibidosPorVisibilidad.get(visibilidad);

				Set<Lugar> lugaresInvisibles = new HashSet<>();
				lugaresInvisibles.add(posicion);
				if(ultimaCalleRecorrida != null){
					lugaresInvisibles.add(ultimaCalleRecorrida);
				}

				lugaresInvisibles.stream().forEach(l -> {
					l.getObstaculos().removeIf(obs -> {
						if(obs.getVisibilidad().sosInvisible()){
							obstaculosInvisiblesViejos.remove(obs);
							return true;
						}
						else{
							return false;
						}
					});
				});
				obstaculosInvisiblesNuevos.stream().forEach(obs -> {
					obstaculosInvisiblesViejos.add(obs);
					Lugar lugarObstaculo = lugaresInvisibles.stream().filter(l -> l.equals(obs.getLugar())).findFirst().get();
					lugarObstaculo.getObstaculos().add(obs);
					obs.setLugar(lugarObstaculo);
				});
			}
		}
	}

	/**
	 * This method returns the String representation of the agent state.
	 */
	@Override
	public String toString() {
		String str = "";

		//TODO: Complete Method toString

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

	public Map<Visibilidad, Set<Obstaculo>> getObstaculos() {
		return obstaculos;
	}

	public void setObstaculos(Map<Visibilidad, Set<Obstaculo>> obstaculos) {
		this.obstaculos = obstaculos;
	}

	public Arista getUltimaCalleRecorrida() {
		return ultimaCalleRecorrida;
	}

	public void setUltimaCalleRecorrida(Arista ultimaCalleRecorrida) {
		this.ultimaCalleRecorrida = ultimaCalleRecorrida;
	}

}
