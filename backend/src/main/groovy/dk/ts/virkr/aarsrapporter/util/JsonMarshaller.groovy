package dk.ts.virkr.aarsrapporter.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by sorenhartvig on 22/06/16.
 */
class JsonMarshaller {

    final private Gson gson

    static String dateformat = "yyyyMMdd'T'HH:mm:ss.SSSZ"

    public JsonMarshaller(boolean indent) {
        // Build gson object
        GsonBuilder builder = new GsonBuilder().serializeNulls().disableHtmlEscaping()
        builder.setDateFormat(dateformat)
        if (indent) {
            builder.setPrettyPrinting()
        }
        gson = builder.create()
    }

    public <T> T toObject(String json, Class<T> mappedType) {
        T result = gson.fromJson(json, mappedType)
        return result
    }

    public <T> String toJson(T obj) {
        // Init
        if (obj == null) {
            return null
        }

        String json = gson.toJson(obj)

        return json
    }
}
