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

import frsf.cidisi.exercise.patrullero.search.modelo.Calle;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;
import frsf.cidisi.exercise.patrullero.search.modelo.Mapa;

public class MapaGsonAdapter implements JsonSerializer<Mapa>, JsonDeserializer<Mapa> {

	@Override
	public JsonElement serialize(Mapa mapa, Type tipo, JsonSerializationContext contexto) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("alto", mapa.getAlto());
		jsonObject.addProperty("ancho", mapa.getAncho());

		JsonArray jsonInterseccionesArray = new JsonArray();
		mapa.getEsquinas().forEach(i -> jsonInterseccionesArray.add(contexto.serialize(i)));
		jsonObject.add("intersecciones", jsonInterseccionesArray);

		JsonArray jsonCallesArray = new JsonArray();
		mapa.getCalles().forEach(c -> jsonCallesArray.add(contexto.serialize(c)));
		jsonObject.add("calles", jsonCallesArray);

		return jsonObject;
	}

	@Override
	public Mapa deserialize(JsonElement mapaJSON, Type tipo, JsonDeserializationContext contexto) throws JsonParseException {
		JsonObject jsonObject = mapaJSON.getAsJsonObject();

		Double alto = jsonObject.get("alto").getAsDouble();
		Double ancho = jsonObject.get("ancho").getAsDouble();

		JsonArray jsonInterseccionesArray = jsonObject.get("intersecciones").getAsJsonArray();
		ArrayList<Interseccion> esquinas = new ArrayList<>();
		jsonInterseccionesArray.forEach(jsonInterseccion -> esquinas.add(contexto.deserialize(jsonInterseccion.getAsJsonObject(), Interseccion.class)));

		JsonArray jsonCallesArray = jsonObject.get("calles").getAsJsonArray();
		ArrayList<Calle> calles = new ArrayList<>();
		jsonCallesArray.forEach(jsonCalle -> calles.add(contexto.deserialize(jsonCalle.getAsJsonObject(), Calle.class)));

		Mapa mapa = new Mapa();
		mapa.setAlto(alto);
		mapa.setAncho(ancho);
		mapa.setEsquinas(esquinas);
		mapa.setCalles(calles);
		return mapa;
	}

}
