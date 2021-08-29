package jee7.certification.preparation.sample.json.streaming;

import java.io.StringReader;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;

public class Streaming {

	public static void main(String[] args) {
		// #### Consuming JSON Using the Streaming API
		JsonParser parser = Json.createParser(new StringReader("{}"));

		// #### Producing JSON Using the Streaming API
		JsonGeneratorFactory createGeneratorFactory = Json.createGeneratorFactory(null);
		JsonGenerator createGenerator = createGeneratorFactory.createGenerator(System.out);
		createGenerator.writeStartObject().write("title", "The Matrix").write("year", "1999").writeStartArray("cast")
				.write("Keanu Reeves").write("Laurence Fishburne").writeEnd().writeEnd().close();

		// {"title":"The Matrix","year":"1999","cast":["Keanu Reeves","Laurence
		// Fishburne"]}
	}

}
