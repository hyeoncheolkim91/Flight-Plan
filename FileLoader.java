package flight2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileLoader
{
	public List<String> readFile(String fileName)
	{
		boolean debug = false;
		List<String> output = new ArrayList<>();

		// Create an input stream from the file
		try
		{
			FileInputStream fileInputStream = new FileInputStream(fileName);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

			//Get the number of lines of data in the file
			int numLines = Integer.parseInt(bufferedReader.readLine());

			//Add each line of data to the list
			for(int i = 0; i < numLines; i++)
				output.add(bufferedReader.readLine());

		}
		catch(FileNotFoundException e)
		{
			System.out.println("File Not found.");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		if(debug)
			for(String s : output)
				System.out.println(s);

		return output;
	}
}