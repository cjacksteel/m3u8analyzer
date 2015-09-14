package m3u8analyzer;
import java.io.File;

public class Parser {
	private String myDirectory;
	
	public Parser(String aDirectory){
		myDirectory = aDirectory;
	}

	public void parse(){
		File dir = new File(myDirectory);
		File[] directoryList = dir.listFiles();
		
		for (File file : directoryList) {
	        if (file.isDirectory()) {
	        	//move on if it's a directory
	            parse(file.listFiles());
	        } else {
	            System.out.println("File: " + file.getName());
	        }
	    }
	}
	
	public void parse(File[] files){
		for (File file : files) {
	        if (file.isDirectory()) {
	        	//move on if it's a directory
	            parse(file.listFiles());
	        } else {
	            System.out.println("File: " + file.getName());
	        }
	    }
	}
}
