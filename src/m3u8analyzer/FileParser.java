package m3u8analyzer;
import java.io.File;
import java.io.IOException;

public class FileParser {

	public FileParser(){
		//null body
	}

	public File[] getFileArray(String aDirectory){
		File dir = new File(aDirectory);
		File[] directoryList = dir.listFiles();
		return directoryList;
	}
		
	public void parse(File[] files) throws IOException{
		for (File file : files) {
	        if (file.isDirectory()) {
	        	//don't do anything if it's a directory
	            parse(file.listFiles());
	        } else {
	        	//instantiate the analyzer for this file
	        	FileAnalyzer a = new FileAnalyzer(file);
	            a.checkFirstLineForTag();
	            a.checkForMultipleVersionTags();
	            //close it to free up resources
	            a.closeBufferedReader();
	        }
	    }
	}
}
