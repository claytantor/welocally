package com.sightlyinc.ratecred.admin.util;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class UrlUtils {
    
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String NAME_VALUE_SEPARATOR = "=";
   


    /**
     * Returns a list of {@link NameValuePair NameValuePairs} as built from the
     * URI's query portion. For example, a URI of
     * http://example.org/path/to/file?a=1&b=2&c=3 would return a list of three
     * NameValuePairs, one for a=1, one for b=2, and one for c=3.
     * <p>
     * This is typically useful while parsing an HTTP PUT.
     *
     * @param uri
     *            uri to parse
     * @param encoding
     *            encoding to use while parsing the query
     */
    public static List <NameValuePair> parse (final URI uri, final String encoding) {
        List <NameValuePair> result = Collections.emptyList();
        final String query = uri.getRawQuery();
        if (query != null && query.length() > 0) {
            result = new ArrayList <NameValuePair>();
            parse(result, new Scanner(query), encoding);
        }
        return result;
    }

    /**
     * Returns a list of {@link NameValuePair NameValuePairs} as parsed from an
     * {@link HttpEntity}. The encoding is taken from the entity's
     * Content-Encoding header.
     * <p>
     * This is typically used while parsing an HTTP POST.
     *
     * @param entity
     *            The entity to parse
     * @throws IOException
     *             If there was an exception getting the entity's data.
     */
    public static List <NameValuePair> parse (
            final HttpEntity entity) throws IOException {
        List <NameValuePair> result = Collections.emptyList();

        String contentType = null;
        String charset = null;

        Header h = entity.getContentType();
        if (h != null) {
            HeaderElement[] elems = h.getElements();
            if (elems.length > 0) {
                HeaderElement elem = elems[0];
                contentType = elem.getName();
                NameValuePair param = elem.getParameterByName("charset");
                if (param != null) {
                    charset = param.getValue();
                }
            }
        }

        if (contentType != null && contentType.equalsIgnoreCase(CONTENT_TYPE)) {
            final String content = EntityUtils.toString(entity, HTTP.ASCII);
            if (content != null && content.length() > 0) {
                result = new ArrayList <NameValuePair>();
                parse(result, new Scanner(content), charset);
            }
        }
        return result;
    }

    /**
     * Returns true if the entity's Content-Type header is
     * <code>application/x-www-form-urlencoded</code>.
     */
    public static boolean isEncoded (final HttpEntity entity) {
        Header h = entity.getContentType();
        if (h != null) {
            HeaderElement[] elems = h.getElements();
            if (elems.length > 0) {
                String contentType = elems[0].getName();
                return contentType.equalsIgnoreCase(CONTENT_TYPE);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Adds all parameters within the Scanner to the list of
     * <code>parameters</code>, as encoded by <code>encoding</code>. For
     * example, a scanner containing the string <code>a=1&b=2&c=3</code> would
     * add the {@link NameValuePair NameValuePairs} a=1, b=2, and c=3 to the
     * list of parameters.
     *
     * @param parameters
     *            List to add parameters to.
     * @param scanner
     *            Input that contains the parameters to parse.
     * @param encoding
     *            Encoding to use when decoding the parameters.
     */
    public static void parse (
            final List <NameValuePair> parameters,
            final Scanner scanner,
            final String encoding) {
        scanner.useDelimiter(PARAMETER_SEPARATOR);
        while (scanner.hasNext()) {
            final String[] nameValue = scanner.next().split(NAME_VALUE_SEPARATOR);
            if (nameValue.length == 0 || nameValue.length > 2)
                throw new IllegalArgumentException("bad parameter");

            final String name = decode(nameValue[0], encoding);
            String value = null;
            if (nameValue.length == 2)
                value = decode(nameValue[1], encoding);
            parameters.add(new BasicNameValuePair(name, value));
        }
    }

    /**
     * Returns a String that is suitable for use as an <code>application/x-www-form-urlencoded</code>
     * list of parameters in an HTTP PUT or HTTP POST.
     *
     * @param parameters  The parameters to include.
     * @param encoding The encoding to use.
     */
    public static String format (
            final List <? extends NameValuePair> parameters,
            final String encoding) {
        final StringBuilder result = new StringBuilder();
        for (final NameValuePair parameter : parameters) {
            final String encodedName = encode(parameter.getName(), encoding);
            final String value = parameter.getValue();
            final String encodedValue = value != null ? encode(value, encoding) : "";
            if (result.length() > 0)
                result.append(PARAMETER_SEPARATOR);
            result.append(encodedName);
            result.append(NAME_VALUE_SEPARATOR);
            result.append(encodedValue);
        }
        return result.toString();
    }

    private static String decode (final String content, final String encoding) {
        try {
            return URLDecoder.decode(content,
                    encoding != null ? encoding : HTTP.DEFAULT_CONTENT_CHARSET);
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }

    private static String encode (final String content, final String encoding) {
        try {
            return URLEncoder.encode(content,
                    encoding != null ? encoding : HTTP.DEFAULT_CONTENT_CHARSET);
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }
    
    //requires ? before
    public static String toQueryString(Map<String, String> model)
    {
        StringBuffer buf = new StringBuffer();
        try {
            for (Iterator<Entry<String, String>> iter = model.entrySet().iterator(); iter.hasNext(); ) {
                Entry<String, String> var = iter.next();
                buf.append(URLEncoder.encode(var.getKey(), "UTF-8")+"="+URLEncoder.encode(var.getValue(), "UTF-8"));
                if(iter.hasNext())
                    buf.append("&");            
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return buf.toString();
    }
    
    public static Map<String, String> convert(List<NameValuePair> nv) {
        
        Map<String, String> map = new HashMap<String,String>();
        
        for (NameValuePair nameValue : nv) {
            map.put(nameValue.getName(), nameValue.getValue());
        }
        
        return map;
    }

}