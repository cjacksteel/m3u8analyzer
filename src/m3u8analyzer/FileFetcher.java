package m3u8analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.commons.lang3.StringUtils;

public class FileFetcher {
	
	public FileFetcher(){
		
	}
	
	//get the master m3u8 file and store it in the m3u8 files directory of the project
	public void downloadMasterFile(URL website) throws IOException{
		String fileName = null;
		FileOutputStream fos = null;
		
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				
		File outputDirectory = new File("m3u8 files/" + website.toString().replaceAll("[-+.^:,/]","_"));
		if (!outputDirectory.exists()) {
			if (outputDirectory.mkdir()) {
				System.out.println("Directory " + outputDirectory + " is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		
		try {
			fileName = outputDirectory + "/" + website.toString().substring(website.toString().lastIndexOf('/') + 1);
			fos = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}
	
	//take in a master file and download child m3u8 files
	public void downloadChildFiles(URL website, File masterFile) throws IOException{
		URL childAddress;
 	   	String line;
 	   	String urlDifference;
 	   	FileOutputStream fos = null;
		String fileName = null;
		File outputDirectory;
		
		try (BufferedReader br = new BufferedReader(new FileReader(masterFile))) {
		    while ((line = br.readLine()) != null) {
		       if(line.startsWith("#")){
		    	   //do nothing
		       }
		       else{
		    	   //if the file is using full URL just use the line
		    	   if(line.startsWith("http://")){
		    		   childAddress = new URL(line);
		    	   }
		    	   //if file is using relative URL, combine it with the full URL of the master
		    	   else{
		    		   childAddress = new URL(website.toString().substring(0, website.toString().lastIndexOf('/') + 1) + line);
		    	   }
		   
		    	   //Check if it's in a different directory  on the web server than the master file
		    	   urlDifference = StringUtils.difference(website.toString(), childAddress.toString());
		    	   
		    	   //If it is, make the directory locally
		    	   if(urlDifference.contains("/")){
		    		   outputDirectory = new File("m3u8 files/" + website.toString().replaceAll("[-+.^:,/]","_") + "/" + urlDifference.substring(0, urlDifference.lastIndexOf("/") + 1));
		    			if (!outputDirectory.exists()) {
		    				if (outputDirectory.mkdir()) {
		    					System.out.println("Directory " + outputDirectory + " is created!");
		    				} else {
		    					System.out.println("Failed to create directory!");
		    				}
		    			}
		    	   }
		    	   else{
		    		   outputDirectory = new File("m3u8 files/" + website.toString().replaceAll("[-+.^:,/]","_") );
		    	   }
		    	   
		    	   ReadableByteChannel rbc = Channels.newChannel(childAddress.openStream());
		    	   
		    	   try {
		   				fileName = outputDirectory + "/" + childAddress.toString().substring(childAddress.toString().lastIndexOf('/') + 1);
		   				fos = new FileOutputStream(fileName);
		   			} catch (FileNotFoundException e) {
		   			// TODO Auto-generated catch block
		   				e.printStackTrace();
		   			}
		   			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
  
		       }
		    }
		}
	}
	
	public String getMasterFileLocation(URL website){
		String path = "m3u8 files/" + website.toString().replaceAll("[-+.^:,/]","_") + "/" + website.toString().substring(website.toString().lastIndexOf('/') + 1);
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) {
			return path;
		}
		else{
			return "File does not exist!";
		}
	}
}
