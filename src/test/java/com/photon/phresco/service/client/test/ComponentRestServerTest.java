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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.photon.phresco.commons.model.DownloadInfo;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.service.client.api.ServiceClientConstant;
import com.photon.phresco.service.client.api.ServiceContext;
import com.photon.phresco.service.client.api.ServiceManager;
import com.photon.phresco.service.client.factory.ServiceClientFactory;
import com.photon.phresco.service.client.impl.RestClient;
import com.photon.phresco.service.client.util.RestUtil;
import com.photon.phresco.util.ServiceConstants;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

public class ComponentRestServerTest implements ServiceConstants {

	public ServiceContext context = null;
	public ServiceManager serviceManager = null;
	
	@Before
	public void Initilaization() throws PhrescoException {
		context = new ServiceContext();
        context.put(ServiceClientConstant.SERVICE_URL, RestUtil.getServerPath());
        context.put(ServiceClientConstant.SERVICE_USERNAME, "demouser");
        context.put(ServiceClientConstant.SERVICE_PASSWORD, "phresco");
        serviceManager = ServiceClientFactory.getServiceManager(context);
	}
	
	@Test
	public void testCreateServer() throws PhrescoException {
		List<String> technologies=new ArrayList<String>();
		
		technologies.add("Html5");
	    List<DownloadInfo> server = new ArrayList<DownloadInfo>();
	    DownloadInfo ss = new DownloadInfo();
	    ss.setId("testServer");
	    ss.setName("TestServer");
	    ss.setDescription("This is a test server");
	    server.add(ss);
        RestClient<DownloadInfo> newApp = serviceManager.getRestClient(REST_API_COMPONENT + REST_API_SERVERS);
        ClientResponse clientResponse = newApp.create(server);
    }
	
	@Test
    public void testFindServers() throws PhrescoException {
    	RestClient<DownloadInfo> serverClient = serviceManager.getRestClient(REST_API_COMPONENT + REST_API_SERVERS);
    	serverClient.queryString(REST_QUERY_CUSTOMERID, "photon");
		GenericType<List<DownloadInfo>> genericType = new GenericType<List<DownloadInfo>>(){};
		List<DownloadInfo> servers = serverClient.get(genericType);
		assertNotNull(servers);
    }

	@Test
	public void testUpdateServers() throws PhrescoException{
		RestClient<DownloadInfo> webserviceClient = serviceManager.getRestClient(REST_API_COMPONENT + REST_API_SERVERS);
		List<String> technologies=new ArrayList<String>();
		technologies.add("Html5");
	    List<DownloadInfo> server = new ArrayList<DownloadInfo>();
	    DownloadInfo ss = new DownloadInfo();
	    ss.setId("testServer");
	    ss.setName("TestServer1");
	    ss.setDescription("This is a test server update");
	    server.add(ss);
	    GenericType<List<DownloadInfo>> genericType = new GenericType<List<DownloadInfo>>() {};
	   
	    List<DownloadInfo> clientResponse = webserviceClient.update(server, genericType);
	    
	}
	
	@Test
    public void testGetServerById() throws PhrescoException {
		String Id = "testServer";
    	RestClient<DownloadInfo> srClient = serviceManager.getRestClient(REST_API_COMPONENT + REST_API_SERVERS);
    	srClient.setPath(Id);
		GenericType<DownloadInfo> genericType = new GenericType<DownloadInfo>(){};
		DownloadInfo server = srClient.getById(genericType);
		assertNotNull(server);
    }
	
	@Test
	public void testUpdateServerById() throws PhrescoException {
        RestClient<DownloadInfo> editServer = serviceManager.getRestClient(REST_API_COMPONENT + REST_API_SERVERS);
        DownloadInfo ss = new DownloadInfo();
        ss.setId("testServer");
	    ss.setName("TestServerUpdate");
	    ss.setDescription("This is a test Server updateId");
	    editServer.setPath("testServer");
        GenericType<DownloadInfo> genericType = new GenericType<DownloadInfo>() {};
        editServer.updateById(ss, genericType);
    }
	
	@Test
	public void testDeleteServer() throws PhrescoException {
        RestClient<DownloadInfo> deleteServer = serviceManager.getRestClient(REST_API_COMPONENT + REST_API_SERVERS);
        deleteServer.setPath("testServer");
        ClientResponse clientResponse = deleteServer.deleteById();
    }
}