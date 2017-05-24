/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gui.componentes;

import frsf.cidisi.exercise.patrullero.search.modelo.TipoIncidente;
import javafx.scene.image.Image;

/**
 * Representa el ícono de la aplicación
 */
public class ImagenIncidente extends Image {

	/**
	 * Constructor. Genera la imagen del ícono
	 */
	public ImagenIncidente(TipoIncidente tipo) {
		super(getURLImagen(tipo));
	}

	private static String getURLImagen(TipoIncidente tipo) {
		switch(tipo) {
		case ALARMA_DE_CASA_DE_FAMILIA:
			return "imagenes/alarma_de_casa_de_familia_icono.png";
		case ALARMA_DE_PANICO_POR_VIOLENCIA_DE_GENERO:
			return "imagenes/alarma_de_panico_por_violencia_de_genero_icono.png";
		case ALARMA_DE_VEHICULO:
			return "imagenes/alarma_de_vehiculo_icono.png";
		case ALARMA_VECINAL_SUCESO_CALLEJERO:
			return "imagenes/alarma_vecinal_suceso_callejero_icono.png";
		case CAMARA_EN_HOGARES:
			return "imagenes/camara_en_hogares_icono.png";
		case CAMARA_EN_LA_VIA_PUBLICA:
			return "imagenes/camara_en_la_via_publica_icono.png";
		}
		return null;
	}
}
