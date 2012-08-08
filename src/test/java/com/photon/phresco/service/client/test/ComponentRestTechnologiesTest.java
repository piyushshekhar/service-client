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
package com.photon.phresco.service.client.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.Technology;
import com.photon.phresco.service.client.api.ServiceClientConstant;
import com.photon.phresco.service.client.api.ServiceContext;
import com.photon.phresco.service.client.api.ServiceManager;
import com.photon.phresco.service.client.factory.ServiceClientFactory;
import com.photon.phresco.service.client.impl.RestClient;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

public class ComponentRestTechnologiesTest {
	
	public ServiceContext context = null;
	public ServiceManager serviceManager = null;
	private static String id = null;
	private static String id2 = null;
	private static String id3 = null;
	
	@Before
	public void Initilaization() {
		context = new ServiceContext();
        context.put(ServiceClientConstant.SERVICE_URL, "http://localhost:3030/service/rest/api");
        context.put(ServiceClientConstant.SERVICE_USERNAME, "demouser");
        context.put(ServiceClientConstant.SERVICE_PASSWORD, "phresco");
	}

	@Ignore
    public void testCreateServer() throws PhrescoException {
    	List<Technology> techs = new ArrayList<Technology>();
    	Technology tech = new Technology();
    	tech.setName("Drupal");
    	List<String> versions = new ArrayList<String>();
    	versions.add("6.18");
    	versions.add("6.19");
    	versions.add("7.0");
		tech.setVersions(versions);
		techs.add(tech);
		
		Technology tech2 = new Technology();
    	tech2.setName("PHP");
    	List<String> versions2 = new ArrayList<String>();
    	versions2.add("4.5");
    	versions2.add("5.0");
		tech2.setVersions(versions2);
		techs.add(tech2);
		
		Technology tech3 = new Technology();
    	tech2.setName("iPhone");
    	List<String> versions3 = new ArrayList<String>();
    	versions3.add("4.5");
    	versions3.add("5.0");
		tech3.setVersions(versions3);
		techs.add(tech3);
    	
		serviceManager = ServiceClientFactory.getServiceManager(context);
		RestClient<Technology> techClient = serviceManager.getRestClient("/component/technologies");
		ClientResponse response = techClient.create(techs);
		System.out.println("response " + response.getStatus());
    }

	@Ignore
    public void testGetTechnologies() {
        try {
            serviceManager = ServiceClientFactory.getServiceManager(context);            
            RestClient<Technology> techClient = serviceManager.getRestClient("/component/technologies");
            GenericType<List<Technology>> genericType = new GenericType<List<Technology>>(){};
            List<Technology> list = techClient.get(genericType);
            id = list.get(0).getId();
            id2 = list.get(1).getId();
            id3 = list.get(2).getId();
            for (Technology tech : list) {
                System.out.println("Tech Name == " + tech.getName() + " id " + tech.getId());
                System.out.println("tec " + tech);
            }
            
        } catch (PhrescoException e) {
            e.printStackTrace();
        }
    }
    
	@Ignore
    public void testPutTechnologies() throws PhrescoException {
    	List<Technology> techs = new ArrayList<Technology>();
    	Technology tech = new Technology();
    	System.out.println("id = " + id);
    	tech.setId(id);
    	tech.setName("Java");
    	List<String> versions = new ArrayList<String>();
    	versions.add("1.5");
    	versions.add("1.6");
		tech.setVersions(versions);
		techs.add(tech);
    	serviceManager = ServiceClientFactory.getServiceManager(context);
		RestClient<Technology> techClient = serviceManager.getRestClient("/component/technologies");
		GenericType<List<Technology>> type = new GenericType<List<Technology>>(){};
		List<Technology> entity = techClient.update(techs, type);
		for (Technology technology : entity) {
			System.out.println("tec " + technology);
		}
    }
    
	@Ignore
    public void testGetTechnologyById() throws PhrescoException {
        try {
	    	serviceManager=ServiceClientFactory.getServiceManager(context);
	    	RestClient<Technology> techClient = serviceManager.getRestClient("/component/technologies");
	    	techClient.setPath(id2);
	    	GenericType<Technology> genericType = new GenericType<Technology>()  {};
	    	Technology tech = techClient.getById(genericType);
	    	System.out.println("name == " + tech);
    	    
        } catch(PhrescoException e){
        	e.printStackTrace();
        }
    }
    
	@Ignore
    public void testPutTechnologyById() throws PhrescoException {
    	Technology tech = new Technology();
    	tech.setId(id2);
    	tech.setName("android-native");
    	List<String> versions = new ArrayList<String>();
    	versions.add("1.0");
    	versions.add("3.0");
		tech.setVersions(versions);
    	serviceManager = ServiceClientFactory.getServiceManager(context);
		RestClient<Technology> techClient = serviceManager.getRestClient("/component/technologies");
		techClient.setPath(id2);
		GenericType<Technology> genericType = new GenericType<Technology>()  {};
		Technology technology = techClient.updateById(tech, genericType);
		System.out.println(technology);
    }

	@Ignore
    public void testDeleteTechnologyById() throws PhrescoException {
    	serviceManager = ServiceClientFactory.getServiceManager(context);            
    	RestClient<Technology> techClient = serviceManager.getRestClient("/component/technologies");
    	techClient.setPath(id3);
    	ClientResponse response = techClient.deleteById();
    	System.out.println(response.getStatus());
    }
	
	
}
