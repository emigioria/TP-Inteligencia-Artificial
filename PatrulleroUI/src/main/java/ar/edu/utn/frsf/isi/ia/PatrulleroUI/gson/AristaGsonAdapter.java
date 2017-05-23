/**
 * Copyright (c) 2017, Emiliano Gioria - Andres Leonel Rico - Esteban Javier Rebechi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import frsf.cidisi.exercise.patrullero.search.modelo.Arista;
import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;

public class AristaGsonAdapter implements JsonSerializer<Arista>, JsonDeserializer<Arista> {

	@Override
	public JsonElement serialize(Arista arista, Type tipo, JsonSerializationContext contexto) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", arista.getId());
		jsonObject.addProperty("peso", arista.getPeso());
		jsonObject.addProperty("origen", arista.getOrigen().getId());
		jsonObject.addProperty("destino", arista.getDestino().getId());
		return jsonObject;
	}

	@Override
	public Arista deserialize(JsonElement aristaJSON, Type tipo, JsonDeserializationContext contexto) throws JsonParseException {
		JsonObject jsonObject = aristaJSON.getAsJsonObject();

		Long id = jsonObject.get("id").getAsLong();
		Integer peso = jsonObject.get("peso").getAsInt();
		Interseccion origen = contexto.deserialize(jsonObject.get("origen"), Interseccion.class);
		Interseccion destino = contexto.deserialize(jsonObject.get("destino"), Interseccion.class);

		Arista arista;
		try{
			arista = new Arista(id, peso, origen, destino, null);
		} catch(Exception e){
			throw new JsonParseException(e);
		}
		return arista;
	}

}
