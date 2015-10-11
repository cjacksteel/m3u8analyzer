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
	ArrayList<String> validTags = new ArrayList<String>();
	String fileType;

	public FileAnalyzer(File aFile) throws IOException{
		File file = aFile;
		logger=Logger.getLogger(file.toString().substring(file.toString().lastIndexOf("\\") + 1));
		BufferedReader br = new BufferedReader(new FileReader(file));
		String lineText;
		//add all lines of file into an arraylist
		while ((lineText = br.readLine()) != null){
			content.add(lineText);
		}
		br.close();
		
		//Create Arraylist of all valid tags from conf file
		BufferedReader br2 = new BufferedReader(new FileReader("conf/validtags.conf"));
		String line = br2.readLine();
		while (line != null) {
			if (line.startsWith("#")){
				validTags.add(line);
			}
			line = br2.readLine();
		}

		br2.close();
		fileType = this.getFileType();
	}
	
	public String getFileType(){
		String type = "Unknown";
		
		for(String line: content){
			if (type == "Master"){
				if(line.contains("#EXTINF")){
					logger.fatal("N/A`Unable to determine file type. File contains both master and playlist tags.");
					type = "Unknown";
					break;
				}
			}
			if (type == "Playlist"){
				if(line.contains("#EXT-X-STREAM-INF")){
					logger.fatal("N/A`Unable to determine file type. File contains both master and playlist tags.");
					type = "Unknown";
					break;
				}
			}
			if(line.contains("#EXT-X-STREAM-INF")){
				type = "Master";
			}
			else if(line.contains("#EXTINF")){
				type = "Playlist";
			}
		}
		return type;
	}

	public void checkFirstLineForTag() throws IOException{
		String firstLine = content.get(0);
		
		if(firstLine.equals("#EXTM3U")){
			//Nothing, this is expected
		}
		//Any other first line of the file is a problem according to HLS spec
		else{
			logger.fatal("1`Incorrect first line. Expected #EXTM3U but found " + firstLine);
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
			logger.error(tags.toString() + "`Contains " + tags.size() + " " + tagName + 
					" tags. Each file may only contain 1 " + tagName + " tag");
		}
	}
	public void checkDurationsAgainstTarget() throws IOException{
		String lineText;
		int targetDuration = -1;
		int version = -1;

		if(fileType == "Playlist"){
			for (int i=0; i < content.size(); i++){
					lineText = content.get(i);
					if(lineText.startsWith("#EXT-X-VERSION")){
						if(version == -1){
							version = Integer.parseInt(lineText.substring(lineText.indexOf(":")+1));
						}
						else{
							version = 0;
						}
					}
				}
		
		for (int i=0; i < content.size(); i++)
		{
			lineText = content.get(i);
			//get the target duration of the file
			if(lineText.startsWith("#EXT-X-TARGETDURATION")){
				try{
					targetDuration = Integer.parseInt((lineText.substring(lineText.indexOf(":") + 1)));
				}
				catch(NumberFormatException e){
					logger.error((i+1) + "`#EXT-X-TARGETDURATION must be an Integer.");
					try{
						targetDuration = Math.round(Float.parseFloat((lineText.substring(lineText.indexOf(":") + 1))));
					}
					catch(NumberFormatException n)
					{
						break;
					}
				}
			}
			else if(lineText.startsWith("#EXTINF:")){
				if(targetDuration == -1){
					logger.error("No target duration specified!`N/A");
					break;
				}
				else if(targetDuration < Float.parseFloat((lineText.substring(lineText.indexOf(":") + 1, lineText.indexOf(","))))){
					logger.error((i+1) + "`Line specifies duration of " +  
							Float.parseFloat((lineText.substring(lineText.indexOf(":") + 1, lineText.indexOf(",")))) + 
							" but target duration is " + targetDuration + ". File duration may not exceed target.");
				}
				
				if(version < 3 && version > 0){
					try{
						Integer.parseInt((lineText.substring(lineText.indexOf(":") + 1, lineText.indexOf(","))));
					}
					catch(NumberFormatException e){
						logger.error((i+1) + "`File specifies version " + version + ". All durations must be integer in this version.");
					}					
				}
				if (version >= 3){
					try{
						int duration = -1;
						duration = Integer.parseInt((lineText.substring(lineText.indexOf(":") + 1, lineText.indexOf(","))));
						if(duration != -1)
						{	
							logger.warn((i+1) + "`File specifies version " + version + ". All durations should be floating point in this version, but found integer.");
						}
					}
					catch(NumberFormatException e){
					}
				}
			}
		}
		}
	}
	
	public void checkForInvalidTags() throws IOException{
		String lineText;
		String tag;
		String [] tagText;
		for (int i=0; i < content.size(); i++){
			//check if the row contains a tag
			lineText = content.get(i);
			if(lineText.startsWith("#EXT")){
				if(lineText.contains(",")){
					tag = lineText.substring(0, lineText.indexOf(","));
				}
				if(lineText.contains(":")){
					tag = lineText.substring(0, lineText.indexOf(":"));
				}
				else{
					tag = lineText;
				}
				
				if(!validTags.contains(tag)){
					logger.error((i+1) + "`Invalid tag. Tag "  + tag + " is not valid per HLS spec.");
				}
			}
		}	
	}
	
	public void checkForMissingTags(String tagName){
		//arraylist to hold the location of each tag found
		int tagCount = 0;
		String lineText;
		String tag;
		if(fileType == "Playlist"){
			for (int i=0; i < content.size(); i++){
					lineText = content.get(i);
					if(lineText.contains(",")){
						tag = lineText.substring(0, lineText.indexOf(","));
					}
					if(lineText.contains(":")){
						tag = lineText.substring(0, lineText.indexOf(":"));
					}
					else{
						tag = lineText;
					}
			
					if(tag.equals(tagName)){
						tagCount++;
					}		
				}
			
			if(tagCount == 0)
				{
					logger.error("N/A`File contains " + tagCount + " " + tagName + 
						" tags. Each file MUST contain this tag.");
				}
		}
	}
}