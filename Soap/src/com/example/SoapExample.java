package com.example;


import org.exist.soap.Query;
import org.exist.soap.QueryService;
import org.exist.soap.QueryServiceLocator;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.rpc.ServiceException;


@Stateless
@WebService
public class SoapExample  {


	public void getNazivi() throws RemoteException, UnsupportedEncodingException, ServiceException {
		QueryService service = new QueryServiceLocator();
        Query query = service.getQuery();
        String session = query.connect("guest", "guest");
        
        byte[] data = query.getResourceData(session, 
            "/db/projekat/nazivi.xml",
            true, false, false);
        System.out.println(new String(data, "UTF-8"));
        query.disconnect(session);

	}

}