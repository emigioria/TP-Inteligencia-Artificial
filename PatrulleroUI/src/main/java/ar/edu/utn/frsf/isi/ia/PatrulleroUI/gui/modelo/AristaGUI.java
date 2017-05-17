package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo;

import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class AristaGUI {

	public static Long ultimoIdAsignado = 0L;

	private static final Double ANCHO_FLECHA = 10.0;

	private static final Double ALTO_FLECHA = 5.0;

	private Arista arista;

	private Group node;

	private InterseccionGUI origen;

	private InterseccionGUI destino;

	private ObjectProperty<Color> colorArista = new SimpleObjectProperty<>(Color.BLACK);

	public AristaGUI(Arista arista, InterseccionGUI origen, InterseccionGUI destino) {
		this.origen = origen;
		if(origen != null){
			origen.getSalientes().add(this);
		}
		this.destino = destino;
		if(origen != null){
			destino.getEntrantes().add(this);
		}
		this.arista = arista;

		node = new Group();

		this.getOrigen().getNode().translateXProperty().addListener((obs, oldV, newV) -> {
			node.getChildren().clear();
			dibujarArista();
		});
		this.getOrigen().getNode().translateYProperty().addListener((obs, oldV, newV) -> {
			node.getChildren().clear();
			dibujarArista();
		});
		this.getDestino().getNode().translateXProperty().addListener((obs, oldV, newV) -> {
			node.getChildren().clear();
			dibujarArista();
		});
		this.getDestino().getNode().translateYProperty().addListener((obs, oldV, newV) -> {
			node.getChildren().clear();
			dibujarArista();
		});

		dibujarArista();
	}

	private void dibujarArista() {
		double x1 = this.getOrigen().getNode().getTranslateX() + InterseccionGUI.RADIO;
		double y1 = this.getOrigen().getNode().getTranslateY() + InterseccionGUI.RADIO;
		double x2 = this.getDestino().getNode().getTranslateX() + InterseccionGUI.RADIO;
		double y2 = this.getDestino().getNode().getTranslateY() + InterseccionGUI.RADIO;
		dibujarLineaConFlecha(node, x1, y1, x2, y2);
	}

	private void dibujarLineaConFlecha(Group pane, double x1, double y1, double x2, double y2) {
		double restaH = InterseccionGUI.RADIO;
		double dx = x2 - x1;
		double dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double sen = dy / D;
		double cos = dx / D;
		double restaX = restaH * cos;
		double restaY = restaH * sen;

		x1 += restaX;
		y1 += restaY;
		x2 -= restaX;
		y2 -= restaY;

		Line linea = new Line(x1, y1, x2, y2);
		linea.setStrokeWidth(1);
		linea.strokeProperty().bind(colorArista);
		pane.getChildren().add(linea);

		dibujarFlecha(pane, x1, y1, x2, y2);
	}

	private void dibujarFlecha(Group pane, double x1, double y1, double x2, double y2) {
		double d = AristaGUI.ANCHO_FLECHA;
		double h = AristaGUI.ALTO_FLECHA;

		double dx = x2 - x1;
		double dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - d;
		double xn = xm;
		double ym = h;
		double yn = -h;
		double x;
		double sen = dy / D;
		double cos = dx / D;

		x = xm * cos - ym * sen + x1;
		ym = xm * sen + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sen + x1;
		yn = xn * sen + yn * cos + y1;
		xn = x;

		Polygon poligono = new Polygon(x2, y2, xm, ym, xn, yn);
		poligono.fillProperty().bind(colorArista);
		pane.getChildren().add(poligono);
	}

	public Arista getArista() {
		return arista;
	}

	public void setArista(Arista arista) {
		this.arista = arista;
	}

	public Node getNode() {
		return node;
	}

	public InterseccionGUI getOrigen() {
		return origen;
	}

	public void setOrigen(InterseccionGUI origen) {
		this.origen = origen;
	}

	public InterseccionGUI getDestino() {
		return destino;
	}

	public void setDestino(InterseccionGUI destino) {
		this.destino = destino;
	}

	public void desactivarArista(Pane mapaPanel) {
		this.getArista().getOrigen().getSalientes().remove(this.getArista());
		this.getArista().getDestino().getEntrantes().remove(this.getArista());
		this.getOrigen().getSalientes().remove(this);
		this.getDestino().getEntrantes().remove(this);
		mapaPanel.getChildren().remove(this.getNode());
	}

	@Override
	public String toString() {
		return arista.toString();
	}
}
