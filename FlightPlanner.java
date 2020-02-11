package flight2;

import java.util.List;

class FlightPlanner
{
	public static void main(String[] args)
	{
		FlightData flightData = new FlightData();
		FileLoader fileLoader = new FileLoader();

		// Load the data from both files. The files should be int the project folder, at the same level as the source
		// folder.
		flightData.createGraph(fileLoader.readFile("Flight Data.txt"));
		List<String> flights = fileLoader.readFile("Requested Flight Plans.txt");

		int flightNum = 1;

		for(String flight : flights)
		{
			String[] flightInfo = flight.split("\\|");
			System.out.println(
					"Flight " + flightNum + ": " + flightInfo[0] + " to " + flightInfo[1] + " (" + flightInfo[2] +
					")");
			boolean time = true;
			if(flightInfo[2].equals("C"))
				time = false;
			List<String> flightPaths = flightData.printFlightPaths(flightInfo[0], flightInfo[1], time);
			for(String flightPath : flightPaths)
				System.out.print(flightPath);
			System.out.println();
		}
	}
}