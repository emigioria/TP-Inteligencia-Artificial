package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import frsf.cidisi.exercise.patrullero.search.modelo.Obstaculo;

public class ObstaculoGsonAdapter implements JsonSerializer<Obstaculo>, JsonDeserializer<Obstaculo> {

	@Override
	public JsonElement serialize(Obstaculo obstaculo, Type tipo, JsonSerializationContext contexto) {
		JsonObject jsonObject = contexto.serialize(obstaculo, obstaculo.getClass()).getAsJsonObject();

		jsonObject.addProperty("TipoObstaculo", obstaculo.getClass().getCanonicalName());

		return jsonObject;
	}

	@Override
	public Obstaculo deserialize(JsonElement jsonObstaculo, Type tipo, JsonDeserializationContext contexto) throws JsonParseException {
		JsonObject jsonObj = jsonObstaculo.getAsJsonObject();
		String className = jsonObj.get("TipoObstaculo").getAsString();
		try{
			Class<?> clz = Class.forName(className);
			return contexto.deserialize(jsonObstaculo, clz);
		} catch(ClassNotFoundException e){
			throw new JsonParseException(e);
		}
	}

}
