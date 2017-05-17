package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo;

import java.util.ArrayList;
import java.util.List;

import frsf.cidisi.exercise.patrullero.search.modelo.Calle;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import javafx.scene.layout.Pane;

public class MapaGUI {
	public static Long ultimoIdAsignadoCalle = 0L;

	private List<InterseccionGUI> intersecciones = new ArrayList<>();

	private Mapa mapa = new Mapa();

	private Pane mapaPanel = new Pane();

	public MapaGUI() {
		super();
		mapaPanel.getStyleClass().add("mapaPanel");
	}

	public List<InterseccionGUI> getIntersecciones() {
		return intersecciones;
	}

	public void agregarInterseccionGUI(InterseccionGUI nuevaInterseccion) {
		intersecciones.add(nuevaInterseccion);
		mapa.getEsquinas().add(nuevaInterseccion.getInterseccion());
		mapaPanel.getChildren().add(nuevaInterseccion.getNode());
	}

	public void quitarInterseccionGUI(InterseccionGUI interseccionAQuitar) {
		(new ArrayList<>(interseccionAQuitar.getEntrantes())).stream().forEach(a -> this.desactivarArista(a));
		(new ArrayList<>(interseccionAQuitar.getSalientes())).stream().forEach(a -> this.desactivarArista(a));
		intersecciones.remove(interseccionAQuitar);
		mapa.getEsquinas().remove(interseccionAQuitar.getInterseccion());
		mapaPanel.getChildren().remove(interseccionAQuitar.getNode());
	}

	public void desactivarArista(AristaGUI arista) {
		arista.getArista().getOrigen().getSalientes().remove(arista.getArista());
		arista.getArista().getDestino().getEntrantes().remove(arista.getArista());
		arista.getOrigen().getSalientes().remove(this);
		arista.getDestino().getEntrantes().remove(this);
		mapaPanel.getChildren().remove(arista.getNode());
	}

	public void agregarAristaGUI(AristaGUI nuevaArista) {
		mapaPanel.getChildren().add(nuevaArista.getNode());
		Calle nuevaCalle = nuevaArista.getArista().getCalle();
		if(!mapa.getCalles().contains(nuevaCalle)){
			mapa.getCalles().add(nuevaCalle);
		}
	}

	public void quitarAristaGUI(AristaGUI aristaAQuitar) {
		this.desactivarArista(aristaAQuitar);
	}

	public Mapa getMapa() {
		return mapa;
	}

	public void setMapa(Mapa mapa) {
		this.mapa = mapa;
	};

	public Pane getNode() {
		return mapaPanel;
	}
}
