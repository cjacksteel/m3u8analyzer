package m3u8analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.log4j.Logger;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileAnalyzer {

	File file;
	BufferedReader br;
	private static Logger logger;

	
	public FileAnalyzer(File aFile) throws IOException{
		file = aFile;
		br = new BufferedReader(new FileReader(file));
		logger=Logger.getLogger(file.toString());
	}

	
	public void checkFirstLineForTag() throws IOException{
		
		String firstLine = br.readLine();
		
		if(firstLine.equals("#EXTM3U")){
			//Nothing, this is expected
		}
		else{
			logger.error("Incorrect first line. Expected #EXTM3U but found " + firstLine );
		}
	}
	
	public void checkForMultipleVersionTags() throws IOException{
		int lineCounter = 1;
		ArrayList<Integer> versionTags = new ArrayList<Integer>();
		String fileText;
		
		while ((fileText = br.readLine()) != null){
			lineCounter++;
			if(fileText.startsWith("#EXT-X-VERSION")){
					versionTags.add(lineCounter);
				}
		}

		if(versionTags.size() > 1)
		{
			logger.error("Contains " + versionTags.size() + " #EXT-X-VERSION tags at lines " + versionTags.toString());
		}
	}
		
	public void closeBufferedReader() throws IOException{
		br.close();
	}
}
