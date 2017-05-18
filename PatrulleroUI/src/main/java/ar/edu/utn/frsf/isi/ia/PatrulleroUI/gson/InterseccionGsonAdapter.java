package ar.edu.utn.frsf.isi.ia.PatrulleroUI.gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import frsf.cidisi.exercise.patrullero.search.modelo.Interseccion;

public class InterseccionGsonAdapter implements JsonSerializer<Interseccion>, JsonDeserializer<Interseccion> {

	private final ThreadLocal<Map<Long, Interseccion>> cache = new ThreadLocal<Map<Long, Interseccion>>() {
		@Override
		protected Map<Long, Interseccion> initialValue() {
			return new HashMap<>();
		}
	};

	@Override
	public JsonElement serialize(Interseccion interseccion, Type tipo, JsonSerializationContext contexto) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", interseccion.getId());
		jsonObject.addProperty("peso", interseccion.getPeso());
		jsonObject.addProperty("coordenadaX", interseccion.getCoordenadaX());
		jsonObject.addProperty("coordenadaY", interseccion.getCoordenadaY());
		return jsonObject;
	}

	@Override
	public Interseccion deserialize(JsonElement interseccionJSON, Type tipo, JsonDeserializationContext contexto) throws JsonParseException {
		// Only the ID is available
		if(interseccionJSON.isJsonPrimitive()){
			JsonPrimitive primitive = interseccionJSON.getAsJsonPrimitive();
			return this.getOrCreate(primitive.getAsLong());
		}

		// The whole object is available
		if(interseccionJSON.isJsonObject()){
			JsonObject jsonObject = interseccionJSON.getAsJsonObject();

			Long id = jsonObject.get("id").getAsLong();
			Integer peso = jsonObject.get("peso").getAsInt();
			Double coordenadaX = jsonObject.get("coordenadaX").getAsDouble();
			Double coordenadaY = jsonObject.get("coordenadaY").getAsDouble();

			Interseccion interseccion = this.getOrCreate(id);
			interseccion.setPeso(peso);
			interseccion.setCoordenadaX(coordenadaX);
			interseccion.setCoordenadaY(coordenadaY);
			return interseccion;
		}

		throw new JsonParseException("Unexpected JSON type: " + interseccionJSON.getClass().getSimpleName());
	}

	private Interseccion getOrCreate(Long id) {
		Interseccion interseccion = cache.get().get(id);
		if(interseccion == null){
			interseccion = new Interseccion(id, null, null, null);
			interseccion.setId(id);
			cache.get().put(id, interseccion);
		}
		return interseccion;
	}

}
