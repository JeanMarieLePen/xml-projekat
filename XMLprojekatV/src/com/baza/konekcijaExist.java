package com.baza;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.exist.xmldb.EXistResource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import com.baza.AuthenticationUtilities;
import com.baza.AuthenticationUtilities.ConnectionProperties;

public class konekcijaExist {
	private static String folderProjekta = "/db/projekat/";
	private static ConnectionProperties conn;
	
	public konekcijaExist() throws IOException {
		conn=AuthenticationUtilities.loadProperties();
	}
	
	public void dodajXML(String naziv) throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException, IOException {
		dodajXML("", naziv,naziv);
	}
	
	public void dodajXML(String imeUbazi, String imeFajla) throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException, IOException {
		dodajXML("",imeUbazi,imeFajla);
	}
	public void dodajXML(String podfolderBaze,String imeUBazi, String nazivFajla ) throws XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		
		String collectionId = folderProjekta+ podfolderBaze;
		String documentId = imeUBazi; 
		String filePath = "data/" + nazivFajla;
		
		 System.out.println("\t- collection ID: " + collectionId);
	    	System.out.println("\t- document ID: " + documentId);
	    	System.out.println("\t- file path: " + filePath + "\n");
	        
		
		System.out.println("[INFO] Loading driver class: " + conn.driver);
    	Class<?> cl = Class.forName(conn.driver);
        
        
        // encapsulation of the database driver functionality
    	Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        
        // entry point for the API which enables you to get the Collection reference
        DatabaseManager.registerDatabase(database);
        
        // a collection of Resources stored within an XML database
        Collection col = null;
        XMLResource res = null;
        
try { 
        	
        	System.out.println("[INFO] Retrieving the collection: " + collectionId);
            col = getOrCreateCollection(collectionId);
            
            /*
             *  create new XMLResource with a given id
             *  an id is assigned to the new resource if left empty (null)
             */
            System.out.println("[INFO] Inserting the document: " + documentId);
            
            // assign ID explicitly
            res = (XMLResource) col.createResource(documentId, XMLResource.RESOURCE_TYPE);
            
            // or use API's identity strategy
            // res = (XMLResource) col.createResource(col.createId(), XMLResource.RESOURCE_TYPE);
            
           // File f = new File(filePath);
           
           // if(!f.canRead()) {
           //     System.out.println("[ERROR] Cannot read the file: " + filePath);
           //     return;
           // }
            
            
          InputStream f1 = openStream(filePath);
          File f =new File("");
          //f.createNewFile();
         copyToFile(f1,f);
          System.out.println("kopiro u fajl;");
            res.setContent(f);
            System.out.println("[INFO] Storing the document: " + res.getId());
            
            col.storeResource(res);
            System.out.println("[INFO] Done.");
            
        } finally {
            
        	//don't forget to cleanup
            if(res != null) {
                try { 
                	((EXistResource)res).freeResources(); 
                } catch (XMLDBException xe) {
                	xe.printStackTrace();
                }
            }
            
            if(col != null) {
                try { 
                	col.close(); 
                } catch (XMLDBException xe) {
                	xe.printStackTrace();
                }
            }
        }
	}
	public void copyToFile(InputStream inputStream, File file) throws IOException {
	    try(OutputStream outputStream = new FileOutputStream(file)) {
	        IOUtils.copy(inputStream, outputStream);
	    }
	}  
	private static Collection getOrCreateCollection(String collectionUri) throws XMLDBException {
        return getOrCreateCollection(collectionUri, 0);
    }
    
    private static Collection getOrCreateCollection(String collectionUri, int pathSegmentOffset) throws XMLDBException {
        
        Collection col = DatabaseManager.getCollection(conn.uri + collectionUri, conn.user, conn.password);
        
        // create the collection if it does not exist
        if(col == null) {
        
         	if(collectionUri.startsWith("/")) {
                collectionUri = collectionUri.substring(1);
            }
            
        	String pathSegments[] = collectionUri.split("/");
            
        	if(pathSegments.length > 0) {
                StringBuilder path = new StringBuilder();
            
                for(int i = 0; i <= pathSegmentOffset; i++) {
                    path.append("/" + pathSegments[i]);
                }
                
                Collection startCol = DatabaseManager.getCollection(conn.uri + path, conn.user, conn.password);
                
                if (startCol == null) {
                	
                	// child collection does not exist
                    
                	String parentPath = path.substring(0, path.lastIndexOf("/"));
                    Collection parentCol = DatabaseManager.getCollection(conn.uri + parentPath, conn.user, conn.password);
                    
                    CollectionManagementService mgt = (CollectionManagementService) parentCol.getService("CollectionManagementService", "1.0");
                    
                    System.out.println("[INFO] Creating the collection: " + pathSegments[pathSegmentOffset]);
                    col = mgt.createCollection(pathSegments[pathSegmentOffset]);
                    
                    col.close();
                    parentCol.close();
                    
                } else {
                    startCol.close();
                }
            }
            return getOrCreateCollection(collectionUri, ++pathSegmentOffset);
        } else {
            return col;
        }
    }
	
    public void getXML(String Naziv) throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
    	getXML("",Naziv);
    }
    public static InputStream openStream(String fileName) throws IOException {
		return AuthenticationUtilities.class.getClassLoader().getResourceAsStream(fileName);
	}
    
	public void getXML(String podfolder, String Naziv) throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
    	
    	String collectionId = folderProjekta+ podfolder;
		String documentId = Naziv; 
		
		System.out.println("\t- collection ID: " + collectionId);
    	System.out.println("\t- document ID: " + documentId + "\n");
        
        // initialize database driver
    	System.out.println("[INFO] Loading driver class: " + conn.driver);
        Class<?> cl = Class.forName(conn.driver);
        
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        
        DatabaseManager.registerDatabase(database);
        
        Collection col = null;
        XMLResource res = null;
        
        try {    
            // get the collection
        	System.out.println("[INFO] Retrieving the collection: " + collectionId);
            col = DatabaseManager.getCollection(conn.uri + collectionId);
            col.setProperty(OutputKeys.INDENT, "yes");
            
            System.out.println("[INFO] Retrieving the document: " + documentId);
            res = (XMLResource)col.getResource(documentId);
            
            if(res == null) {
                System.out.println("[WARNING] Document '" + documentId + "' can not be found!");
            } else {
            	
            	System.out.println("[INFO] Showing the document as XML resource: ");
            	System.out.println(res.getContent());
                
            }
        } finally {
            //don't forget to clean up!
            
            if(res != null) {
                try { 
                	((EXistResource)res).freeResources(); 
                } catch (XMLDBException xe) {
                	xe.printStackTrace();
                }
            }
            
            if(col != null) {
                try { 
                	col.close(); 
                } catch (XMLDBException xe) {
                	xe.printStackTrace();
                }
            }
        }
    }
}
