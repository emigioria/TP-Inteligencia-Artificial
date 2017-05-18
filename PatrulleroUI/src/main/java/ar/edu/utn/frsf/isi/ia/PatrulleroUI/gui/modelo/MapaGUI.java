/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import frsf.cidisi.exercise.patrullero.search.modelo.Calle;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;
import javafx.scene.layout.Pane;

public class MapaGUI {

	protected static Long ultimoIdAsignadoCalle = 0L;

	private List<InterseccionGUI> intersecciones = new ArrayList<>();

	private Mapa mapa = new Mapa();

	private Pane mapaPanel = new Pane();

	public MapaGUI() {
		super();
		mapaPanel.getStyleClass().add("mapaPanel");
		mapaPanel.prefHeightProperty().addListener((obs, oldV, newV) -> {
			mapa.setAlto(newV.doubleValue());
		});
		mapaPanel.prefWidthProperty().addListener((obs, oldV, newV) -> {
			mapa.setAncho(newV.doubleValue());
		});

		//Resetear IDs
		InterseccionGUI.ultimoIdAsignado = 0L;
		AristaGUI.ultimoIdAsignado = 0L;
		MapaGUI.ultimoIdAsignadoCalle = 0L;
	}

	public MapaGUI(Mapa mapa) {
		this();
		this.mapa = mapa;
		mapaPanel.setPrefHeight(mapa.getAlto());
		mapaPanel.setPrefWidth(mapa.getAncho());
		mapa.getEsquinas().stream().forEach(i -> {
			InterseccionGUI nuevaInterseccion = new InterseccionGUI(i);
			intersecciones.add(nuevaInterseccion);
			mapaPanel.getChildren().add(nuevaInterseccion.getNode());
		});
		mapa.getCalles().stream().map(c -> c.getTramos()).flatMap(List::stream).forEach(a -> mapaPanel.getChildren().add(new AristaGUI(a, intersecciones).getNode()));

		//Setear IDs
		try{
			InterseccionGUI.ultimoIdAsignado = mapa.getEsquinas().stream().max((x, y) -> x.getId().compareTo(y.getId())).get().getId();
		} catch(NoSuchElementException e){

		}
		try{
			AristaGUI.ultimoIdAsignado = mapa.getCalles().stream().map(c -> c.getTramos()).flatMap(List::stream).max((x, y) -> x.getId().compareTo(y.getId())).get().getId();
		} catch(NoSuchElementException e){

		}
		try{
			MapaGUI.ultimoIdAsignadoCalle = mapa.getCalles().stream().max((x, y) -> x.getId().compareTo(y.getId())).get().getId();
		} catch(NoSuchElementException e){

		}
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
		arista.getArista().getCalle().getTramos().remove(arista.getArista());
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

	public static Calle crearCalle(String nombrePropio) {
		return new Calle(++MapaGUI.ultimoIdAsignadoCalle, nombrePropio);
	}
}
