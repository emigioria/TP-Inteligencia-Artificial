/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui;

import java.io.IOException;

import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.StyleCSS;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.ventanas.PresentadorVentanas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class ControladorPatrullero extends ControladorJavaFX implements ControladorApilable {

	private PilaJavaFX apilador;

	private void setApilador(PilaJavaFX apilador) {
		this.apilador = apilador;
	}

	protected ControladorPatrullero nuevaScene(String URLVista) {
		return nuevaCambiarScene(URLVista, apilador, false);
	}

	protected ControladorPatrullero cambiarScene(String URLVista) {
		return nuevaCambiarScene(URLVista, apilador, true);
	}

	protected ControladorPatrullero nuevaCambiarScene(String URLVista, PilaJavaFX apilador, Boolean cambiar) {
		try{
			//Crear el cargador de la pantalla
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(URLVista));

			//Cargar vista
			Parent pantallaSiguiente = (Parent) loader.load();

			//Cargar controlador
			ControladorPatrullero controlador = loader.getController();

			//Setear estilo si no tiene
			if(pantallaSiguiente.getStylesheets().isEmpty()){
				pantallaSiguiente.getStylesheets().add(new StyleCSS().getDefaultStyle());
			}

			if(cambiar){
				apilador.cambiarPantalla(pantallaSiguiente, controlador);
			}
			else{
				apilador.apilarPantalla(pantallaSiguiente, controlador);
			}
			controlador.setStage(stage);
			controlador.setApilador(apilador);
			return controlador;
		} catch(IOException e){
			new PresentadorVentanas().presentarExcepcionInesperada(e, stage);
			return null;
		}
	}

	@Override
	@FXML
	public void salir() {
		apilador.desapilarPantalla();
	}

	@Override
	public void dejarDeMostrar() {

	}

	@Override
	public void actualizar() {

	}

	@Override
	public Boolean sePuedeSalir() {
		return true;
	}

	public static PilaScene crearYMostrarPrimeraVentana(Stage primaryStage, String URL_Vista) {
		Scene primaryScene = new Scene(new Pane());
		primaryStage.setScene(primaryScene);
		PilaScene apilador = new PilaScene(primaryScene);
		ControladorPatrullero pantallaMock = new ControladorPatrullero() {
			@Override
			public void actualizar() {
			}

			@Override
			public void inicializar() {
			}

			@Override
			public void dejarDeMostrar() {

			}
		};
		pantallaMock.setApilador(apilador);
		pantallaMock.setStage(primaryStage);
		pantallaMock.nuevaScene(URL_Vista);
		primaryStage.setMinHeight(400);
		primaryStage.setMinWidth(650);
		Platform.runLater(() -> {
			primaryStage.show();
		});
		return apilador;
	}
}
