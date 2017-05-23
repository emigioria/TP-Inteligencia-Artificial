/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import frsf.cidisi.exercise.patrullero.search.modelo.CasoDePrueba;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;
import frsf.cidisi.exercise.patrullero.search.modelo.TipoIncidente;

public class CasoDePruebaGsonAdapter implements JsonSerializer<CasoDePrueba>, JsonDeserializer<CasoDePrueba> {

	@Override
	public JsonElement serialize(CasoDePrueba casoDePrueba, Type tipo, JsonSerializationContext contexto) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("posicionInicialPatrullero", casoDePrueba.getPosicionInicialPatrullero().getId());
		jsonObject.addProperty("tipoIncidente", casoDePrueba.getTipoIncidente().name());
		jsonObject.addProperty("posicionIncidente", casoDePrueba.getPosicionIncidente().getId());

		JsonArray jsonObstaculosArray = new JsonArray();
		casoDePrueba.getObstaculos().forEach(o -> jsonObstaculosArray.add(contexto.serialize(o, Obstaculo.class)));
		jsonObject.add("obstaculos", jsonObstaculosArray);

		return jsonObject;
	}

	@Override
	public CasoDePrueba deserialize(JsonElement casoDePruebaJSON, Type tipo, JsonDeserializationContext contexto) throws JsonParseException {
		JsonObject jsonObject = casoDePruebaJSON.getAsJsonObject();

		Long pipId = jsonObject.get("posicionInicialPatrullero").getAsLong();
		Interseccion posicionInicialPatrullero = new Interseccion(pipId, null, null, null);
		TipoIncidente tipoIncidente = TipoIncidente.valueOf(jsonObject.get("tipoIncidente").getAsString());
		Long piId = jsonObject.get("posicionIncidente").getAsLong();
		Interseccion posicionIncidente = new Interseccion(piId, null, null, null);

		JsonArray jsonObstaculosArray = jsonObject.get("obstaculos").getAsJsonArray();
		ArrayList<Obstaculo> obstaculos = new ArrayList<>();
		jsonObstaculosArray.forEach(jsonObstaculo -> obstaculos.add(contexto.deserialize(jsonObstaculo.getAsJsonObject(), Obstaculo.class)));

		CasoDePrueba casoDePrueba = new CasoDePrueba();
		casoDePrueba.setPosicionInicialPatrullero(posicionInicialPatrullero);
		casoDePrueba.setTipoIncidentes(tipoIncidente);
		casoDePrueba.setPosicionIncidente(posicionIncidente);
		casoDePrueba.setObstaculos(obstaculos);
		return casoDePrueba;
	}

}
