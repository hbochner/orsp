package edu.mit.broad.jira;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;


/**
 * Abstract subclass of {@link AbstractJerseyClientService} with helpful methods for JSON-based RESTful web services.
 */
// FIXME: This code is not OOP, and would be better abstracted as a helper class instead of a base class.
public abstract class AbstractJsonJerseyClientService extends AbstractJerseyClientService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Log log = LogFactory.getLog(AbstractJsonJerseyClientService.class);

    /**
     * Write a JSON representation of the bean parameter to a {@link String} and return.
     *
     * @param bean
     * @return
     * @throws java.io.IOException
     */
    protected String writeValue(Object bean) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, bean);
        return outputStream.toString();
    }

    /**
     * Set the JSON MIME types for both request and response on the {@link com.sun.jersey.api.client.WebResource}
     *
     * @param webResource
     * @return
     */
    protected WebResource.Builder setJsonMimeTypes(WebResource webResource) {
        return webResource.type(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE);
    }

    /**
     * POST a JSON representation of the requestPojo to the specified {@link com.sun.jersey.api.client.WebResource} and return a POJO
     * representation of the response.
     *
     * @param webResource
     * @param requestPojo
     * @param responseGenericType
     * @param <T>
     * @return
     * @throws java.io.IOException
     */
    protected <T> T post(WebResource webResource, Object requestPojo, GenericType<T> responseGenericType) throws
            IOException {
        String request = writeValue(requestPojo);

        log.trace("POST request: " + request);

        try {
            T ret = setJsonMimeTypes(webResource).post(responseGenericType, request);
            log.trace("POST response: " + ret);
            return ret;
        } catch (UniformInterfaceException e) {
            //TODO SGM:  Change to a more defined exception to give the option to set in throws or even catch
            log.error("POST request: " + request, e);
            throw new RuntimeException(e.getResponse().getEntity(String.class), e);
        }
    }

    /**
     * POST a JSON representation of the requestPojo to the specified {@link com.sun.jersey.api.client.WebResource} This method is used when a
     * a post does not expect a response (HTTP Status code in the 200 range)
     *
     * @param webResource
     * @param requestPojo
     * @return
     * @throws java.io.IOException
     */
    protected void post(WebResource webResource, Object requestPojo) throws IOException {
        String request = writeValue(requestPojo);

        log.trace("POST request: " + request);

        try {
            setJsonMimeTypes(webResource).post(request);
        } catch (UniformInterfaceException e) {
            //TODO SGM:  Change to a more defined exception to give the option to set in throws or even catch
            log.error("POST request: " + request, e);
            throw new RuntimeException(e.getResponse().getEntity(String.class), e);
        }
    }

    /**
     * PUT a JSON representation of the requestPojo to the specified {@link com.sun.jersey.api.client.WebResource} and return a POJO
     * representation of the response.
     *
     * @param webResource
     * @param requestPojo
     * @throws java.io.IOException
     */
    protected void put(WebResource webResource, Object requestPojo) throws IOException {
        String request = writeValue(requestPojo);
        log.trace("PUT request: " + request);
        try {
            setJsonMimeTypes(webResource).put(request);
        } catch (UniformInterfaceException e) {
            //TODO SGM:  Change to a more defined exception to give the option to set in throws or even catch
            log.error("PUT request: " + request, e);
            throw new RuntimeException(e.getResponse().getEntity(String.class), e);
        }
    }


    /**
     * Return a JSON representation of the response to a GET issued to the specified {@link com.sun.jersey.api.client.WebResource}
     *
     * @param webResource
     * @param genericType
     * @param <T>
     * @return
     */
    protected <T> T get(WebResource webResource, GenericType<T> genericType) {
        try {
            return setJsonMimeTypes(webResource).get(genericType);
        } catch (UniformInterfaceException e) {
            //TODO SGM:  Change to a more defined exception to give the option to set in throws or even catch
            log.error("GET request" + webResource.getURI(), e);
            throw new RuntimeException(e.getResponse().getEntity(String.class), e);
        }
    }
}
