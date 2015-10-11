package m3u8analyzer;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

public class FileParser {

	public FileParser(){
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
	            a.checkForMultipleTags("#EXTM3U");
	            a.checkForMultipleTags("#EXT-X-TARGETDURATION");
	            a.checkForMultipleTags("#EXT-X-INDEPENDENT-SEGMENTS");
	            a.checkForMultipleTags("#EXT-X-START");
	            a.checkForMultipleTags("#EXT-X-VERSION");
	            a.checkDurationsAgainstTarget();
	            a.checkForInvalidTags();
	            a.checkForMissingTags("#EXT-X-ENDLIST");
	        }      
	    }
	}
	
	public File getLogfile(){
		//When all files have been parsed
		File logFile;
		Logger logger = Logger.getRootLogger();
		FileAppender appender = (FileAppender)logger.getAppender("R");
		logFile = new File(appender.getFile());
		return logFile;
	}
}
