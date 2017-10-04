/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.GuardianServer.productionsystem;

import ar.edu.utn.frsf.isi.ia.Guardian.gui.controladores.VerSimulacionAutomaticaController;
import ar.edu.utn.frsf.isi.ia.Guardian.productionsystem.Guardian;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.ControladorJavaFXApilable;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.PilaScene;
import ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes.IconoAplicacion;
import javafx.application.Platform;
import javafx.stage.Stage;

public abstract class GuardianServer extends Guardian {

	public abstract void enviarAccion(String message);

	public GuardianServer(String agentId) throws Exception {
		super(new EstadoGuardianServer(agentId));
	}

	public GuardianServer(String agentId, String archivoPl) throws Exception {
		super(new EstadoGuardianServer(agentId, archivoPl));
	}

	@Override
	protected void mostrarAccion(String accion) {
		super.mostrarAccion(accion);
		this.enviarAccion(accion);
	}

	@Override
	protected void mandarPatruIA() {
		super.mandarPatruIA();
		Platform.runLater(() -> {
			//Inicializar parametros
			Stage stage = new Stage();

			//Iniciar el stage en el centro de la pantalla
			stage.centerOnScreen();

			//Setear icono y titulo de aplicacion
			stage.getIcons().add(new IconoAplicacion());
			stage.setTitle("Inteligencia Artificial - Sistema PatruIA");

			PilaScene apilador = ControladorJavaFXApilable.crearYMostrarPrimeraVentana(stage, VerSimulacionAutomaticaController.URL_VISTA);

			//Setear acción de cierre
			stage.setOnCloseRequest((e) -> {
				if(!apilador.sePuedeSalir()){
					e.consume();
				}
			});
		});
	}
}
