/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.Calle;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class AristaGUI extends Group {

	protected static Long ultimoIdAsignado = 0L;

	private static final Double ANCHO_FLECHA = 10.0;

	private static final Double ALTO_FLECHA = 5.0;

	private Arista arista;

	private InterseccionGUI origen;

	private InterseccionGUI destino;

	private ObjectProperty<Color> colorArista = new SimpleObjectProperty<>(Color.BLACK);

	private Set<Obstaculo> obstaculosActivos;

	public AristaGUI(Arista arista, List<InterseccionGUI> intersecciones) {
		this(arista, findInterseccionGUI(arista.getOrigen(), intersecciones), findInterseccionGUI(arista.getDestino(), intersecciones));
	}

	private static InterseccionGUI findInterseccionGUI(Interseccion interseccion, List<InterseccionGUI> intersecciones) {
		return intersecciones.stream().filter(i -> i.getInterseccion().equals(interseccion)).findFirst().get();
	}

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

		this.getOrigen().translateXProperty().addListener((obs, oldV, newV) -> {
			this.getChildren().clear();
			dibujarArista();
		});
		this.getOrigen().translateYProperty().addListener((obs, oldV, newV) -> {
			this.getChildren().clear();
			dibujarArista();
		});
		this.getDestino().translateXProperty().addListener((obs, oldV, newV) -> {
			this.getChildren().clear();
			dibujarArista();
		});
		this.getDestino().translateYProperty().addListener((obs, oldV, newV) -> {
			this.getChildren().clear();
			dibujarArista();
		});

		dibujarArista();
	}

	private void dibujarArista() {
		double x1 = this.getOrigen().getTranslateX() + InterseccionGUI.RADIO;
		double y1 = this.getOrigen().getTranslateY() + InterseccionGUI.RADIO;
		double x2 = this.getDestino().getTranslateX() + InterseccionGUI.RADIO;
		double y2 = this.getDestino().getTranslateY() + InterseccionGUI.RADIO;
		dibujarLineaConFlecha(this, x1, y1, x2, y2);
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

	public Set<Obstaculo> getObstaculosActivos() {
		return obstaculosActivos;
	}

	public void setObstaculosActivos(Set<Obstaculo> obstaculosActivos) {
		this.obstaculosActivos = obstaculosActivos;
	}

	@Override
	public String toString() {
		return arista.toString();
	}

	public void actualizarObstaculos() {
		if(arista.getObstaculos().isEmpty()){
			colorArista.set(Color.BLACK);
		}
		else{
			if(arista.getObstaculos().stream().reduce(1, (x, y) -> y.getPeso(x), (x, y) -> x * y) < 0){
				colorArista.set(Color.RED);
			}
			else{
				colorArista.set(Color.ORANGE);
			}
		}
	}

	public void actualizarObstaculos(Long hora) {
		obstaculosActivos = arista.getObstaculos().stream().filter(obs -> obs.getTiempoInicio() <= hora && obs.getTiempoFin() > hora).collect(Collectors.toSet());
		if(obstaculosActivos.isEmpty()){
			colorArista.set(Color.BLACK);
		}
		else{
			if(obstaculosActivos.stream().filter(obs -> obs.getTiempoInicio() <= hora && obs.getTiempoFin() > hora).reduce(1, (x, y) -> y.getPeso(x), (x, y) -> x * y) < 0){
				colorArista.set(Color.RED);
			}
			else{
				colorArista.set(Color.ORANGE);
			}
		}
	}

	public static Arista crearArista(Integer peso, Interseccion origen, Interseccion destino, Calle calle) throws Exception {
		return new Arista(++AristaGUI.ultimoIdAsignado, peso, origen, destino, calle);
	}

	public void mostrarTooltips() {
		Tooltip tooltip = new Tooltip();
		VBox pantalla = new VBox();
		pantalla.getChildren().add(new Label(arista + " Peso: " + arista.getPeso()));
		tooltip.setGraphic(pantalla);
		Tooltip.install(this, tooltip);
	}

}
