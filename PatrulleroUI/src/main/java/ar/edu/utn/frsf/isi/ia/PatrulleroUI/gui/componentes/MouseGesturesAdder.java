/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MouseGesturesAdder {
	private double orgSceneX, orgSceneY;
	private double orgTranslateX, orgTranslateY;

	private EventHandler<MouseEvent> mousePressed;

	private EventHandler<MouseEvent> mouseDragged;

	private EventHandler<MouseEvent> extraMousePressed;

	public MouseGesturesAdder(final Pane pane) {
		mousePressed = t -> {
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();

			Node p = ((Node) (t.getSource()));
			orgTranslateX = p.getTranslateX();
			orgTranslateY = p.getTranslateY();

			if(extraMousePressed != null){
				extraMousePressed.handle(t);
			}
		};

		mouseDragged = t -> {
			double offsetX = t.getSceneX() - orgSceneX;
			double offsetY = t.getSceneY() - orgSceneY;
			double newTranslateX = orgTranslateX + offsetX;
			double newTranslateY = orgTranslateY + offsetY;

			Node p = ((Node) (t.getSource()));
			p.setTranslateX(newTranslateX);
			p.setTranslateY(newTranslateY);

			Double limiteIzquierdo = 0.0;
			Double limiteDerecho = pane.getPrefWidth() - p.getLayoutBounds().getWidth();
			Double limiteSuperior = 0.0;
			Double limiteInferior = pane.getPrefHeight() - p.getLayoutBounds().getHeight();

			if(p.getTranslateX() < limiteIzquierdo){
				p.setTranslateX(limiteIzquierdo);
			}
			else if(p.getTranslateX() > limiteDerecho){
				pane.setPrefWidth(p.getTranslateX() + p.getLayoutBounds().getWidth());
			}
			if(p.getTranslateY() < limiteSuperior){
				p.setTranslateY(limiteSuperior);
			}
			else if(p.getTranslateY() > limiteInferior){
				pane.setPrefHeight(p.getTranslateY() + p.getLayoutBounds().getHeight());
			}
		};
	}

	public void makeDraggable(Node node) {
		node.setOnMousePressed(mousePressed);
		node.setOnMouseDragged(mouseDragged);
	}

	public void setOnMousePressed(EventHandler<MouseEvent> extraMousePressed) {
		this.extraMousePressed = extraMousePressed;
	}
}
