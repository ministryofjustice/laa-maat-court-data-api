package gov.uk.courtdata.model.laastatus;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class EmptyStringTypeAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        if (StringUtils.isEmpty())
    }

    @Override
    public String read(JsonReader in) throws IOException {
        return in.nextString();
    }
}
