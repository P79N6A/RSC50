package org.basex.query.func.web;

import static org.basex.query.QueryError.INVALIDOPTION_X;
import static org.basex.query.QueryText.HTTP_PREFIX;
import static org.basex.query.QueryText.HTTP_URI;
import static org.basex.query.QueryText.OUTPUT_PREFIX;
import static org.basex.query.QueryText.OUTPUT_URI;
import static org.basex.query.QueryText.REST_PREFIX;
import static org.basex.query.QueryText.REST_URI;
import static org.basex.query.QueryText.SERIALIZATION_PARAMETERS;

import java.util.HashMap;
import java.util.Map.Entry;

import org.basex.io.serial.SerializerMode;
import org.basex.io.serial.SerializerOptions;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.func.StandardFunc;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.QNm;
import org.basex.query.value.node.FElem;
import org.basex.util.http.MediaType;
import org.basex.util.options.Options;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public final class WebResponseHeader extends StandardFunc {
  /** Cache-control string. */
  private static final String CACHE_CONTROL = "Cache-Control";
  /** Default value of cache-control string. */
  private static final String CACHE_CONTROL_DEFAULT = "max-age=3600,public";

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    return value(qc).iter();
  }

  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final HashMap<String, String> output = toOptions(0, new Options(), qc).free();
    final HashMap<String, String> http = toOptions(1, new Options(), qc).free();

    // HTTP response
    if(!http.containsKey(CACHE_CONTROL)) http.put(CACHE_CONTROL, CACHE_CONTROL_DEFAULT);

    final FElem hresp = new FElem(new QNm(HTTP_PREFIX, "response", HTTP_URI));
    for(final Entry<String, String> entry : http.entrySet()) {
      final String name = entry.getKey(), value = entry.getValue();
      if(!value.isEmpty()) hresp.add(new FElem(new QNm(HTTP_PREFIX, "header", HTTP_URI)).
          add("name", name).add("value", value));
    }

    // Serialization parameters
    if(!output.containsKey(SerializerOptions.MEDIA_TYPE.name())) output.put(
        SerializerOptions.MEDIA_TYPE.name(), MediaType.APPLICATION_OCTET_STREAM.toString());

    final SerializerOptions so = SerializerMode.DEFAULT.get();
    final FElem oseri = new FElem(new QNm(OUTPUT_PREFIX, SERIALIZATION_PARAMETERS, OUTPUT_URI));
    for(final Entry<String, String> entry : output.entrySet()) {
      final String name = entry.getKey(), value = entry.getValue();
      if(so.option(name) == null) throw INVALIDOPTION_X.get(info, name);
      if(!value.isEmpty()) oseri.add(new FElem(new QNm(OUTPUT_PREFIX, name, OUTPUT_URI)).
          add("value", value));
    }

    // REST response
    final FElem resp = new FElem(new QNm(REST_PREFIX, "response", REST_URI));
    if(hresp.children().next() != null) resp.add(hresp);
    if(oseri.children().next() != null) resp.add(oseri);
    return resp;
  }
}
