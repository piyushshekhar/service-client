/*
 * ###
 * Phresco Service Client
 * %%
 * Copyright (C) 1999 - 2012 Photon Infotech Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ###
 */
package com.photon.phresco.service.client.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.exception.PhrescoException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.MultiPartMediaTypes;

public class RestClient<E> {
	
	private static final Logger S_LOGGER= Logger.getLogger(RestClient.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	private WebResource resource = null;
	private Builder builder = null;
	private String path = null;
	private static final Map<String, String> HEADER = new HashMap<String, String>();

	public RestClient(String serverUrl) {
		Client client = ClientHelper.createClient();
		resource = client.resource(serverUrl);
	}
	
	/**
	 * Given additional path added to the URI of the web resource 
	 * @param id
	 */
	public void setPath(String path) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.setPath(String path)" + path);
	    }
	
	    this.path = path;
	}
	
	/**
	 * Add an HTTP header and value
	 * @param key
	 * @param value
	 */
	public void addHeader(String key, String value) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.addHeader(String key, String value)" + value);
	    }
		
		HEADER.put(key, value);
	}
	
	/**
	 * Create a new WebResource from this web resource with an additional query parameter added to the URI of this web resource
	 * @param key
	 * @param value
	 */
	public void queryString(String key, String value) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.addQueryString(String key, String value)" + value);
	    }
		
		resource = resource.queryParam(key, value);
	}
	
	/**
	 * Create a new WebResource from this web resource with additional query parameters added to the URI of this web resource
	 * @param headers
	 */
	public void queryStrings(Map<String, String> headers) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.addQueryStrings(Map<String, String> headers)");
	    }
		
		Set<String> keySet = headers.keySet();
		MultivaluedMap<String, String> queryStrings = new MultivaluedMapImpl();
		for (String key : keySet) {
			queryStrings.add(key, headers.get(key));
		}
		resource = resource.queryParams(queryStrings);
	}
	
	/**
     * To update the builder with header and path
     */
	private void updateBuilder() {
	    if (isDebugEnabled) {
            S_LOGGER.debug("Entered into RestClient.updateBuilder()");
        }
	    
		if (StringUtils.isNotEmpty(path)) {
			resource = resource.path(path);
		}
		
		builder = resource.getRequestBuilder();
		
		Set<String> keySet = HEADER.keySet();
		for (String key : keySet) {
			builder = builder.header(key, HEADER.get(key));
		}
	}
	
	/**
	 * Get List of objects for the specified generic type object
	 * @param genericType
	 * @return
	 */
	public List<E> get(GenericType<List<E>> genericType) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.get(GenericType<List<E>> genericType)");
	    }
		
		return get(genericType, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
	}
	
	/**
	 * Get List of objects for the specified generic type object
	 * @param genericType
	 * @return
	 */
	public List<E> get(GenericType<List<E>> genericType, String accept, String type) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.get(GenericType<List<E>> genericType, String accept, String type)");
	    }
		
		updateBuilder();
		builder = builder.accept(accept).type(type);
		return builder.get(genericType);
	}
	
	/**
	 * Get object for the specified generic type object using the given id 
	 * @param genericType
	 * @return
	 */
	public E getById(GenericType<?> genericType) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.getById(GenericType<?> genericType)");
	    }
		
		return getById(genericType, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
	}
	
	/**
	 * Get object for the specified generic type object using the given id 
	 * @param genericType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E getById(GenericType<?> genericType, String accept, String type) {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.getById(GenericType<?> genericType, String accept, String type)");
	    }
		
		updateBuilder();
		builder = builder.accept(accept).type(type);
		return (E) builder.get(genericType);
	}
	
	/**
	 * Creates List of objects
	 * @param infos
	 * @throws PhrescoException
	 */
	public ClientResponse create(List<E> infos) throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.create(List<E> infos)");
	    }
		
		return create(infos, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
	}
	
    /**
     * @param multiPart
     * @return
     * @throws PhrescoException
     */
    public ClientResponse create(MultiPart multiPart) throws PhrescoException {
        if (isDebugEnabled) {
            S_LOGGER.debug("Entered into RestClient.create(List<E> infos)");
        }
        return create(multiPart, MediaType.APPLICATION_JSON_TYPE, MultiPartMediaTypes.MULTIPART_MIXED_TYPE);
    }
    
	/**
	 * @param multiPart
	 * @param accept
	 * @param type
	 * @return
	 * @throws PhrescoException
	 */
	private ClientResponse create(MultiPart multiPart,
            MediaType accept, MediaType type) throws PhrescoException {
	    updateBuilder();
	    builder = builder.accept(accept).type(type);
	    ClientResponse clientResponse = builder.post(ClientResponse.class, multiPart);
        isErrorThrow(clientResponse);
        return clientResponse;
    }

    /**
	 * Creates List of objects
	 * @param infos
	 * @throws PhrescoException
	 */
	public ClientResponse create(List<E> infos, String accept, String type) throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.create(List<E> infos, String accept, String type)");
	    }
		
		updateBuilder();
		builder = builder.accept(accept).type(type);
		ClientResponse clientResponse = builder.post(ClientResponse.class, infos);
		isErrorThrow(clientResponse);
		return clientResponse;
	}
	
	/**
     * Creates List of objects
     * @param infos
     * @throws PhrescoException
     */
    public ClientResponse create(E info, String accept, String type) throws PhrescoException {
        if (isDebugEnabled) {
            S_LOGGER.debug("Entered into RestClient.create(List<E> infos, String accept, String type)");
        }
        updateBuilder();
        builder = builder.accept(accept).type(type);
        ClientResponse clientResponse = builder.post(ClientResponse.class, info);
        isErrorThrow(clientResponse);
        return clientResponse;
    }
    
    /**
     * @param multiPart
     * @return
     * @throws PhrescoException
     */
    public ClientResponse update(MultiPart multiPart) throws PhrescoException {
    	if (isDebugEnabled) {
    		S_LOGGER.debug("Entered into RestClient.update(MultiPart multiPart)");
    	}
    	return update(multiPart, MediaType.APPLICATION_JSON_TYPE, MultiPartMediaTypes.MULTIPART_MIXED_TYPE);
    }
    
	/**
	 * @param multiPart
	 * @param accept
	 * @param type
	 * @return
	 * @throws PhrescoException
	 */
	private ClientResponse update(MultiPart multiPart, MediaType accept, MediaType type) throws PhrescoException {
	    updateBuilder();
	    builder = builder.accept(accept).type(type);
	    ClientResponse clientResponse = builder.put(ClientResponse.class, multiPart);
        isErrorThrow(clientResponse);
        return clientResponse;
    }
    
	/**
	 * Updates List of objects for the given type 
	 * @param infos
	 * @param type
	 * @return
	 * @throws PhrescoException
	 */
	public List<E> update(List<E> infos, GenericType<List<E>> type) throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.update(List<E> infos, GenericType<List<E>> type)");
	    }

		return update(infos, type, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
	}
	
	/**
	 * Updates List of objects for the given type 
	 * @param infos
	 * @param type
	 * @return
	 * @throws PhrescoException
	 */
	public List<E> update(List<E> infos, GenericType<List<E>> gtype, String accept, String type) throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.update(List<E> infos, GenericType<List<E>> gtype, String accept, String type)");
	    }
		
		updateBuilder();
		builder = builder.accept(accept).type(type);
		ClientResponse clientResponse = builder.put(ClientResponse.class, infos);
		isErrorThrow(clientResponse);
		return clientResponse.getEntity(gtype);
	}
	
	/**
	 * Update the given object by using the type given
	 * @param obj
	 * @param genericType
	 * @return
	 * @throws PhrescoException
	 */
	public E updateById(E obj, GenericType<E> genericType) throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.updateById(E obj, GenericType<E> genericType)");
	    }
		
		return updateById(obj, genericType, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
	}
	
	/**
	 * Update the given object by using the type given
	 * @param obj
	 * @param type
	 * @return
	 * @throws PhrescoException
	 */
	public E updateById(E obj, GenericType<E> genericType, String accept, String type) throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.updateById(E obj, GenericType<E> genericType, String accept, String type)");
	    }
		
		updateBuilder();
		builder = builder.accept(accept).type(type);
		ClientResponse clientResponse = builder.put(ClientResponse.class, obj);
		isErrorThrow(clientResponse);
		return clientResponse.getEntity(genericType);
	}
	
	/**
	 * Deletes the List of objects
	 * @param infos
	 * @throws PhrescoException
	 */
	public void delete(List<E> infos) throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.delete(List<E> infos)");
	    }
		
		updateBuilder();
		ClientResponse clientResponse = builder.delete(ClientResponse.class, infos);
		isErrorThrow(clientResponse);
	}
	
	/**
	 * Throws exception when status not equal to Accepted and Ok status codes
	 * @param clientResponse
	 * @throws PhrescoException
	 */
	private void isErrorThrow(ClientResponse clientResponse) throws PhrescoException {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.isErrorThrow(ClientResponse clientResponse)");
	    }
		
		int status = clientResponse.getStatus();
		if (status == ClientResponse.Status.ACCEPTED.getStatusCode() || 
				status == ClientResponse.Status.OK.getStatusCode() || status == ClientResponse.Status.CREATED.getStatusCode()) {
		    S_LOGGER.info(status);
		} else {
			throw new PhrescoException("Not able to Create");
		}
	}

	/*public void create(String infos) {
		ClientResponse clientResponse = builder.post(ClientResponse.class, infos);
	}
	
	public void update(String infos) {
		ClientResponse clientResponse = builder.put(ClientResponse.class, infos);
	}

	public void updatebyId(Role infos) {
		ClientResponse clientResponse = builder.put(ClientResponse.class, infos);
	}*/
	
	/**
	 * Delete the object by given id parameter
	 */
	public ClientResponse deleteById() {
	    if (isDebugEnabled) {
	        S_LOGGER.debug("Entered into RestClient.deleteById()");
	    }
		
		updateBuilder();
		return builder.delete(ClientResponse.class);
	}
	
	public ClientResponse get(String type) {
		updateBuilder();
		ClientResponse response = builder.type(type).get(ClientResponse.class);
		return response;
	}
}