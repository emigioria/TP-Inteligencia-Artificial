/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ImagenPatrullero;
import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class PatrulleroGUI extends ImageView {

	private InterseccionGUI posicion;

	public PatrulleroGUI() {
		super(new ImagenPatrullero());
		this.setFitHeight(2 * InterseccionGUI.RADIO);
		this.setFitWidth(2 * InterseccionGUI.RADIO);
	}

	public void inicializar(InterseccionGUI posicion, Arista aristaApuntada) {
		//Desplazar instantaneamente
		this.setPosicionTransicionada(
				posicion,
				Duration.ONE).stream().forEach(a -> a.play());
		//Girar instantaneamente
		this.girarImagen(
				aristaApuntada,
				Duration.ONE).stream().forEach(a -> a.play());
	}

	public InterseccionGUI getPosicion() {
		return posicion;
	}

	public List<Transition> setPosicionTransicionada(InterseccionGUI posicion, Duration duracion) {
		if(posicion.equals(this.posicion)){
			return new ArrayList<>();
		}
		this.posicion = posicion;

		TranslateTransition retorno = new TranslateTransition(duracion);
		retorno.setFromX(this.getTranslateX());
		retorno.setToX(posicion.getNode().getTranslateX());
		retorno.setFromY(this.getTranslateY());
		retorno.setToY(posicion.getNode().getTranslateY());
		retorno.setNode(this);

		return Arrays.asList(new Transition[] { retorno });
	}

	public List<Transition> girarImagen(Arista aristaApuntada, Duration duracion) {
		Double espejo;
		Double giro;

		Double horizontal = aristaApuntada.getDestino().getCoordenadaX() - aristaApuntada.getOrigen().getCoordenadaX();
		Double vertical = aristaApuntada.getOrigen().getCoordenadaY() - aristaApuntada.getDestino().getCoordenadaY();
		if(horizontal < 0){
			espejo = -1.0;
		}
		else{
			espejo = 1.0;
		}

		if(horizontal != 0){
			giro = -Math.atan(vertical / horizontal) * 360 / (2 * Math.PI);
		}
		else{
			if(vertical < 0){
				giro = 90.0;
			}
			else if(vertical > 0){
				giro = -90.0;
			}
			else{
				giro = 0.0;
			}
		}

		ScaleTransition retorno1 = new ScaleTransition(Duration.ONE);
		retorno1.setToX(espejo);
		retorno1.setNode(this);

		RotateTransition retorno2 = new RotateTransition(duracion);
		retorno2.setToAngle(giro);
		retorno2.setNode(this);

		return Arrays.asList(new Transition[] { retorno1, retorno2 });
	}

}
