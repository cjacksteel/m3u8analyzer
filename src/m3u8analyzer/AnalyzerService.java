package m3u8analyzer;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AnalyzerService {

	public static void main(String[] args) throws IOException {
		
		URL website = new URL("http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8");
		
		FileFetcher ff = new FileFetcher();
		ff.downloadMasterFile(website);
		
		File masterFile = new File(ff.getMasterFileLocation(website));
		ff.downloadChildFiles(website, masterFile);
	}
}
