package com.katiforis.checkers.conf.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

public final class DateTypeAdapter
        extends TypeAdapter<Date> {

    private static final TypeAdapter<Date> dateTypeAdapter = new DateTypeAdapter();

    private DateTypeAdapter() {
    }

   public static TypeAdapter<Date> getAdapter() {
        return dateTypeAdapter;
    }

    @Override
    public Date read(final JsonReader in) throws IOException {
        return new Date(in.nextLong());
    }

    @Override
    @SuppressWarnings("resource")
    public void write(final JsonWriter out, final Date value) throws IOException {
        out.value(value.getTime());
    }

}
