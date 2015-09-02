package m3u8analyzer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class AnalyzerService {

	public static void main(String[] args) throws IOException {
		
		URL website = null;
		try {
			website = new URL("http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ReadableByteChannel rbc = null;
		try {
			rbc = Channels.newChannel(website.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOutputStream fos = null;
		String fileName = null;
		
		try {
			fileName = website.toString().substring(website.toString().lastIndexOf('/') + 1);
			System.out.println(fileName);
			fos = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		System.out.println("Finished.");
	}

}
