package pitest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.io.FileWriter;


public class MapwithListDistinct {
	

//CSV file header
private static final String FILE_HEADER = "DISTINCT_PLAY_COUNT,CLIENT_COUNT";

public static void main(String[] args) throws IOException {
		
//Data load from csv into map
	String path = new File("SourceFiles/exhibitA-input.csv").getAbsolutePath();
		HashMap<String, List<String>> hash_map = new HashMap<String, List<String>>();
		
		String line;
		
		BufferedReader br = new BufferedReader(new FileReader(path));
		while ((line = br.readLine()) != null) {
		    String[] cols = line.split("	");
		    if (cols[3].length()>9)
		    {
		    	if (cols[3].substring(0, 10).equals("10/08/2016"))
			    {
		    		playedTrackList(hash_map,cols[2],cols[1]);

			    }
		    }
		}

		trackCount(hash_map);
		br.close();
	}

//Put the played tracks as a list for every CLIENT_ID into the HashMap
public static void playedTrackList(HashMap<String, List<String>> map, String key, String value) {
    if (map.get(key) == null) {
        List<String> list = new ArrayList<>();
        list.add(value);
        map.put(key, list);
    } else {
        map.get(key).add(value);
    }
}

// Calculate Record Count with Corresponding Client Count
public static void trackCount(HashMap<String, List<String>> trackmap) {
	
	// Holds initial data
	HashMap<String, Integer> count_map = new HashMap<String, Integer>();
	
	for (String key:trackmap.keySet()) 
	{
		Integer tCounter = countStreamGroup(trackmap.get(key));
		count_map.put(key, tCounter);
		
	}

	// Holds Record Count corresponding to the Client count
	HashMap<Integer, String> result_map = new HashMap<Integer, String>();
	for (String tkey:count_map.keySet())
	{
		Integer count = Collections.frequency(count_map.values(), count_map.get(tkey));
		if (!result_map.containsKey(count_map.get(tkey)))
		{
			result_map.put(count_map.get(tkey),count.toString());
		}
	}
	
	try {
		csvWriter(result_map);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

// Calculate the distinct tracks in the list belongs to CLIENT_ID
public static Integer countStreamGroup(List<String> inputList) {
	List<String> duplicateList = new ArrayList<>();
	Integer distinctRecord = 0;
	for (String item:inputList)
	{
		if(!duplicateList.contains(item))
		{
			duplicateList.add(item);
			distinctRecord++;
		} 
	}
	return distinctRecord;
}


public static void csvWriter(HashMap<Integer, String> writemap) throws IOException {
	FileWriter writer;
	String path = new File("SourceFiles/DistincTrack.csv").getAbsolutePath();
	writer = new FileWriter(path, true);

	// Write CSV
	// Add Header
	writer.append(FILE_HEADER.toString());
	writer.write("\r\n");
	// Add Records
	for (Integer tkey:writemap.keySet()) { 
		writer.write(tkey.toString());
		writer.write(",");
		writer.write(writemap.get(tkey));
		writer.write("\r\n");
	}
	writer.close();
}
}
