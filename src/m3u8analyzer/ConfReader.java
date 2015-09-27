package m3u8analyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConfReader {
	
	public ConfReader(){
	}
	
	public String readConf(String path) throws IOException
	{
		String address;
		//read in conf file with the address
		BufferedReader br = new BufferedReader(new FileReader(path));
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
		return address;
	}
}