/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.StackPaneWithTag;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class InterseccionGUI {

	protected static Long ultimoIdAsignado = 0L;

	public static final Double RADIO = 20.0;

	private Interseccion interseccion;

	private StackPaneWithTag<InterseccionGUI> node;

	private List<AristaGUI> entrantes = new ArrayList<>();

	private List<AristaGUI> salientes = new ArrayList<>();

	private ObjectProperty<Color> colorTexto = new SimpleObjectProperty<>(Color.BLACK);

	private ObjectProperty<Color> colorInterseccion = new SimpleObjectProperty<>(Color.CYAN);

	public InterseccionGUI() {
		this(new Interseccion(++InterseccionGUI.ultimoIdAsignado, 1, RADIO, RADIO));
	}

	public InterseccionGUI(Interseccion interseccion) {
		this.interseccion = interseccion;

		node = new StackPaneWithTag<>();
		node.setTag(this);

		node.translateXProperty().addListener((obs, oldV, newV) -> {
			interseccion.setCoordenadaX(newV.doubleValue() + RADIO);
		});

		node.translateYProperty().addListener((obs, oldV, newV) -> {
			interseccion.setCoordenadaY(newV.doubleValue() + RADIO);
		});

		dibujarInterseccion();

		Platform.runLater(() -> {
			//Solo se puede transformar despues de dibujarse
			node.setTranslateX(interseccion.getCoordenadaX() - RADIO);
			node.setTranslateY(interseccion.getCoordenadaY() - RADIO);
		});
	}

	private void dibujarInterseccion() {
		Text textoId = new Text();
		textoId.setText(interseccion.getId().toString());

		textoId.fillProperty().bind(colorTexto);

		Circle circulo = new Circle();
		circulo.fillProperty().bind(colorInterseccion);
		circulo.setRadius(RADIO);

		node.getChildren().addAll(circulo, textoId);
	}

	public Node getNode() {
		return node;
	}

	public Interseccion getInterseccion() {
		return interseccion;
	}

	public List<AristaGUI> getEntrantes() {
		return entrantes;
	}

	public List<AristaGUI> getSalientes() {
		return salientes;
	}

	@Override
	public String toString() {
		return interseccion.getId().toString();
	}

	public void actualizarObstaculos() {
		if(interseccion.getObstaculos().isEmpty()){
			colorTexto.set(Color.BLACK);
			colorInterseccion.set(Color.CYAN);
		}
		else{
			if(interseccion.getObstaculos().stream().reduce(1, (x, y) -> y.getPeso(x), (x, y) -> x * y) < 0){
				colorTexto.set(Color.WHITE);
				colorInterseccion.set(Color.RED);
			}
			else{
				colorTexto.set(Color.WHITE);
				colorInterseccion.set(Color.ORANGE);
			}
		}
	}

	public void actualizarObstaculos(Long hora) {
		Set<Obstaculo> obstaculosActivos = interseccion.getObstaculos().stream().filter(obs -> obs.getTiempoInicio() <= hora && obs.getTiempoFin() > hora).collect(Collectors.toSet());
		if(obstaculosActivos.isEmpty()){
			colorTexto.set(Color.BLACK);
			colorInterseccion.set(Color.CYAN);
		}
		else{
			if(obstaculosActivos.stream().reduce(1, (x, y) -> y.getPeso(x), (x, y) -> x * y) < 0){
				colorTexto.set(Color.WHITE);
				colorInterseccion.set(Color.RED);
			}
			else{
				colorTexto.set(Color.WHITE);
				colorInterseccion.set(Color.ORANGE);
			}
		}
	}

}
