package m3u8analyzer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

	public static void main(String[] args) throws MalformedURLException, IOException {
		//Read in the conf file and determine what the URL is
		ConfReader configFile = new ConfReader();
		URL website = new URL(configFile.readConf("conf/address.conf"));

		//Get the files from the specified URL
		FileFetcher ff = new FileFetcher(website);
		/*ff.downloadMasterFile();
		File masterFile = new File(ff.getMasterFileLocation());
		ff.downloadChildFiles(masterFile);
*/
		//System.out.println(ff.getRootDirectory());
		
		FileParser p = new FileParser();
		p.parse(p.getFileArray(ff.getRootDirectory()));
	}
}