package m3u8analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class AnalyzerService {

	public static void main(String[] args) throws IOException {
		String address;
		
		//read in conf file with the address
		BufferedReader br = new BufferedReader(new FileReader("conf/address.conf"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		    	if(line.startsWith("ftp://") || line.startsWith("http://")){
			    	sb.append(line);
			        sb.append(System.lineSeparator());
			    }
		        line = br.readLine();
		    }
		    	address = sb.toString();
		} finally {
		    br.close();
		}
		
		System.out.println(address);
		URL website = new URL(address);
		
		FileFetcher ff = new FileFetcher();
		ff.downloadMasterFile(website);
		
		File masterFile = new File(ff.getMasterFileLocation(website));
		ff.downloadChildFiles(website, masterFile);
	}
}
