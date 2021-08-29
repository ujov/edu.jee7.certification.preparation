package jee7.certification.preparation.sample.json.streaming;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

public class ObjectModelAPI {

	public static void main(String[] args) {

		// Consuming JSON Using the Object Model API

		JsonReader createReader = Json.createReader(new StringReader("{\"apple\":\"red\"}"));
		JsonObject readObject = createReader.readObject();
		String value = readObject.getString("apple");
		System.out.println(value); // --> red

		/* JsonArray readArray = createReader.readArray(); */

		// Producing JSON Using the Object Model API

		JsonBuilderFactory createBuilderFactory = Json.createBuilderFactory(null);
		JsonArrayBuilder createArrayBuilder = createBuilderFactory.createArrayBuilder();
		JsonObjectBuilder createObjectBuilder = createBuilderFactory.createObjectBuilder();

		JsonObject jsonObject = createObjectBuilder.add("apple", "red").build();
		JsonWriter createWriter = Json.createWriter(System.out);
		createWriter.writeObject(jsonObject);
		createWriter.close();

		JsonArray jsonArray = createArrayBuilder.add(Json.createObjectBuilder().add("apple", "red"))
				.add(Json.createObjectBuilder().add("banana", "yellow")).build();
		
		createWriter = Json.createWriter(System.out);
		createWriter.writeArray(jsonArray);
		createWriter.close();

	}

}
