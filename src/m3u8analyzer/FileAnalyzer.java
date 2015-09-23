package m3u8analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.log4j.Logger;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileAnalyzer {

	private static Logger logger;
	ArrayList<String> content = new ArrayList<String>();

	public FileAnalyzer(File aFile) throws IOException{
		File file = aFile;
		logger=Logger.getLogger(file.toString());
		BufferedReader br = new BufferedReader(new FileReader(file));
		String lineText;
		//add all lines of file into an arraylist
		while ((lineText = br.readLine()) != null){
			content.add(lineText);
		}
		br.close();
	}

	
	public void checkFirstLineForTag() throws IOException{
		String firstLine = content.get(0);
		
		if(firstLine.equals("#EXTM3U")){
			//Nothing, this is expected
		}
		//Any other first line of the file is a problem according to HLS spec
		else{
			logger.error("Incorrect first line. Expected #EXTM3U but found " + firstLine);
		}
	}
	
	public void checkForMultipleTags(String tagName) throws IOException{
		//arraylist to hold the location of each tag found
		ArrayList<Integer> tags = new ArrayList<Integer>();
		String lineText;
		int lineCount;
		
		for (int i=0; i < content.size(); i++){
			lineText = content.get(i);
			lineCount = (i + 1);
			if(lineText.startsWith(tagName)){
					tags.add(lineCount);
				}		
		}
		if(tags.size() > 1)
		{
			logger.error("Contains " + tags.size() + " " + tagName + " tags at lines " + tags.toString());
		}
	}
	public void checkDurationsAgainstTarget() throws IOException{
		String lineText;
		int targetDuration = -1;
				
		for (int i=0; i < content.size(); i++)
		{
			lineText = content.get(i);
			//get the target duration of the file
			if(lineText.startsWith("#EXT-X-TARGETDURATION")){
				targetDuration = Integer.parseInt((lineText.substring(lineText.indexOf(":") + 1)));
			}
			else if(lineText.startsWith("#EXTINF:")){
				if(targetDuration == -1){
					logger.error("No target duration specified!");
					break;
				}
				else if(targetDuration < Float.parseFloat((lineText.substring(lineText.indexOf(":") + 1, lineText.indexOf(","))))){
					logger.error("Line " + (i+1) + " specifies duration of " +  Float.parseFloat((lineText.substring(lineText.indexOf(":") + 1, lineText.indexOf(",")))) + " but target duration is " + targetDuration);
				}		
			}
		}
	}
}