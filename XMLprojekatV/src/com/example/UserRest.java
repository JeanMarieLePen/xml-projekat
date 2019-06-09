package com.example;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.xmldb.api.base.XMLDBException;

import com.baza.konekcijaExist;
//@ApplicationPath("/projekat")

@Path("/user")
public class UserRest {

	@GET
    @Path("/getNazivi")
    public void getNazivi() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
        System.out.println("Invoking getNazivi");
        konekcijaExist ke= new konekcijaExist();
        ke.getXML("nazivi.xml");
	}
	
	@GET
    @Path("/addNazivi")
    public void addXML() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
        System.out.println("Invoking addNazivi");
        konekcijaExist ke= new konekcijaExist();
        ke.dodajXML("books.xml");
	}
}
