package m3u8analyzer;
import java.io.File;

public class FileParser {

	public FileParser(){
		//null body
	}

	public File[] getFileArray(String aDirectory){
		File dir = new File(aDirectory);
		File[] directoryList = dir.listFiles();
		return directoryList;
	}
		
	public void parse(File[] files){
		for (File file : files) {
	        if (file.isDirectory()) {
	        	//don't do anything if it's a directory
	            parse(file.listFiles());
	        } else {
	        	//instantiate the analyzer for this file
	        	FileAnalyzer a = new FileAnalyzer(file);
	            System.out.println(a.toString());
	        }
	    }
	}
}
