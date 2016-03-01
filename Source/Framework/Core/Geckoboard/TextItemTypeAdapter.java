package Framework.Core.Geckoboard;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import nl.pvanassen.geckoboard.api.json.text.TextItemType;

public class TextItemTypeAdapter extends TypeAdapter<TextItemType>{

	@Override
	public void write(JsonWriter out, TextItemType value) throws IOException {
	       if (value == null) {
	            out.nullValue();
	            return;
	        }
	        out.value(value.ordinal());
	}

	@Override
	public TextItemType read(JsonReader in) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
