/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.StackPaneWithTag;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class InterseccionGUI {

	protected static Long ultimoIdAsignado = 0L;

	public static final Integer RADIO = 20;

	private Interseccion interseccion;

	private StackPaneWithTag<InterseccionGUI> node;

	private List<AristaGUI> entrantes = new ArrayList<>();

	private List<AristaGUI> salientes = new ArrayList<>();

	private ObjectProperty<Color> colorTexto = new SimpleObjectProperty<>(Color.BLACK);

	private ObjectProperty<Color> colorInterseccion = new SimpleObjectProperty<>(Color.CYAN);

	public InterseccionGUI() {
		this(new Interseccion(++InterseccionGUI.ultimoIdAsignado, 1));
	}

	public InterseccionGUI(Interseccion interseccion) {
		this.interseccion = interseccion;

		node = new StackPaneWithTag<>();
		node.setTag(this);

		dibujarInterseccion();
	}

	private void dibujarInterseccion() {
		Text textoId = new Text();
		textoId.setText(interseccion.getId().toString());

		textoId.fillProperty().bind(colorTexto);

		Circle circulo = new Circle();
		circulo.fillProperty().bind(colorInterseccion);
		circulo.setRadius(RADIO);

		circulo.translateXProperty().addListener((obs, oldV, newV) -> {
			interseccion.setCoordenadaX(newV.doubleValue());
		});
		circulo.setTranslateX(interseccion.getCoordenadaX());

		circulo.translateYProperty().addListener((obs, oldV, newV) -> {
			interseccion.setCoordenadaY(newV.doubleValue());
		});
		circulo.setTranslateY(interseccion.getCoordenadaY());

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
}
