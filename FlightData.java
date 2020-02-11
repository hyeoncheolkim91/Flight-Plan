package flight2;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows for the creation of a graph and for finding paths between points on the graph.
 */
class FlightData
{
	private final List<City> CITIES = new ArrayList<>();

	/**
	 * Takes an array of strings "A|B|cost|time" and creates a list of all cities, each with a list of destinations
	 * and their costs/times
	 *
	 * @param paths A list of Strings of the format: "A|B|cost|time"
	 */
	public void createGraph(List<String> paths)
	{
		boolean debug = false;

		//Iterate through the array of paths, adding each city and its destinations
		for(String line : paths)
		{
			String[] pathData = line.split("\\|");

			//Get the cities from the list of cities. If they are not already in the list, this will create new ones.
			City start = getCity(pathData[0]);
			City desti = getCity(pathData[1]);

			if(debug)
				System.out.println("Adding destination: " + desti.NAME + " " + pathData[2] + " " + pathData[3]);

			start.addDestination(desti, pathData[2], pathData[3]);
		}
	}

	/**
	 * Returns the cost of going from the source to the destination City
	 *
	 * @param source      The starting city
	 * @param destination The ending city
	 * @return The cost from the source to an adjacent destination
	 */
	private Double getCost(String source, String destination)
	{
		boolean debug = false;

		double output = 0;

		for(City start : CITIES)
			if(start.NAME.equals(source))
			{
				int index = 0;
				for(City dest : start.DESTINATIONS)
				{
					if(dest.NAME.equals(destination))
						output = start.COSTS.get(index);
					index++;
				}
			}

		return output;
	}

	/**
	 * Returns the time to go from the source to the destination City
	 *
	 * @param source      The starting city
	 * @param destination The ending city
	 * @return The time from the source to an adjacent destination
	 */
	private int getTime(String source, String destination)
	{
		boolean debug = false;

		int output = 0;

		for(City start : CITIES)
			if(start.NAME.equals(source))
			{
				int index = 0;
				for(City dest : start.DESTINATIONS)
				{
					if(dest.NAME.equals(destination))
						output = start.TIMES.get(index);
					index++;
				}
			}

		return output;
	}

	/**
	 * Returns a list of all possible paths from startIn to destinationIn
	 *
	 * @param startIn       The name of the starting city
	 * @param destinationIn The name of the destination city
	 * @param time          True sorts the paths by time, False sorts the paths by cost
	 * @return A list of strings that each represent a path from the starting to the ending city
	 */
	public List<String> printFlightPaths(String startIn, String destinationIn, boolean time)
	{
		boolean debug = false;
		List<String> output = new ArrayList<>();

		if(debug)
			System.out.println("Getting flight plan");

		List<List<City>> paths = new ArrayList<>();
		List<City> path = new ArrayList<>();
		path.add(getCity(startIn));
		City dest = getCity(destinationIn);

		String plan;

		double COST;
		int TIME;

		//Get a list of paths from the source to the destination
		getPaths(path, dest, paths);

		paths = sortPaths(paths, time);

		//Build a string displaying the flight path and add it to the list of paths
		int pathNum = 0;
		for(List<City> flightPlan : paths)
		{
			if(flightPlan != null)
			{
				COST = getPathCost(flightPlan);
				TIME = getPathTime(flightPlan);

				pathNum++;
				plan = "     Path " + pathNum + ": " + flightPlan.get(0).NAME;
				int index = 1;
				StringBuilder planBuilder = new StringBuilder(plan);
				while(index < flightPlan.size())
				{
					planBuilder.append(" -> ").append(flightPlan.get(index).NAME);
					index++;
				}
				plan = planBuilder.toString();
				String cost = String.format("%1$,.2f", COST);

				plan += ". Time: " + TIME + " Cost: " + cost + "\n";
				output.add(plan);
			}
		}

		if(pathNum == 0)
			output.add("     No flight plans available\n");

		return output;
	}

	/**
	 * Returns a list of all paths between two cities.
	 *
	 * @param path        A list of cities already in the path, should start as just the source city in the list.
	 * @param destination The destination city
	 * @param paths       A list of all paths, starts empty and each path is added when completed
	 */
	private void getPaths(List<City> path, City destination, List<List<City>> paths)
	{
		boolean debug = false;

		City current = path.get(path.size() - 1);

		if(debug)
			System.out.println("getPaths: current city: " + current.NAME);

		//Look at each destination city in the current city
		for(City next : current.DESTINATIONS)
		{
			if(debug)
				System.out.println("Looking at next: " + next.NAME);

			//If the next city is already in the list, don't add it to the list
			if(!path.contains(next))
			{
				if(debug)
					System.out.println("Adding " + next.NAME + " to path");

				List<City> newPath = new ArrayList<>(path);

				if(debug)
					System.out.println("Adding cost: " + getCost(current.NAME, next.NAME));

				newPath.add(next);

				//If the next node is the destination
				if(next.NAME.equals(destination.NAME))
				{
					if(debug)
						System.out.println("path has reached the destination");

					paths.add(newPath);
				}
				else
				{
					if(debug)
						System.out.println("next is not destination");
					getPaths(newPath, destination, paths);
				}
			}
			else if(debug)
				System.out.println("next city already in list: " + next.NAME);
		}
	}

	/**
	 * Returns the object representing a city in the graph
	 *
	 * @param name The name of the city
	 * @return The City object in the graph with the same name as the input
	 */
	private City getCity(String name)
	{
		boolean debug = false;

		for(City city : CITIES)
			if(city.NAME.equals(name))
				return city;

		City temp = new City(name);
		CITIES.add(temp);

		return temp;
	}

	/**
	 * Returns the total cost of a path
	 *
	 * @param path A list of City objects in the path
	 * @return The total cost of the path
	 */
	private double getPathCost(List<City> path)
	{
		boolean debug = false;
		double output = 0;

		int index = 0;
		if(path != null)
			while(index < path.size() - 1)
			{
				output += getCost(path.get(index).NAME, path.get(index + 1).NAME);

				if(debug)
					System.out.println("getPathCost: " + path.get(index).NAME + " -> " + path.get(index + 1).NAME +
					                   "\noutput += " + getCost(path.get(index).NAME, path.get(index + 1).NAME) +
					                   " = " + output);

				index++;
			}

		return output;
	}

	/**
	 * Returns the total time of a path
	 *
	 * @param path A list of City objects in the path
	 * @return The total time to traverse the given path
	 */
	private int getPathTime(List<City> path)
	{
		boolean debug = false;
		int output = 0;

		int index = 0;
		if(path != null)
			while(index < path.size() - 1)
			{
				output += getTime(path.get(index).NAME, path.get(index + 1).NAME);
				index++;
			}

		return output;
	}

	/**
	 * @param paths List paths
	 * @param time  True sorts paths by time, False sorts paths by cost
	 * @return
	 */
	public List<List<City>> sortPaths(List<List<City>> paths, boolean time)
	{
		List<City> firstPath = null, secondPath = null, thirdPath = null;
		double firstWeight = -1, secondWeight = -1, thirdWeight = -1;
		double weight;

		for(List<City> current : paths)
		{
			if(time)
				weight = getPathTime(current);
			else
				weight = getPathCost(current);

			//If the first spot is empty or greater than the current path
			if(weight < firstWeight || firstWeight == -1)
			{
				//Move all paths down to make room in the first spot
				thirdPath = secondPath;
				thirdWeight = secondWeight;
				secondPath = firstPath;
				secondWeight = firstWeight;
				firstPath = current;
				firstWeight = weight;
			}
			//Otherwise, if the second spot is empty or a worse path than the current one
			else if(weight < secondWeight || secondWeight == -1)
			{
				thirdPath = secondPath;
				thirdWeight = secondWeight;
				secondPath = current;
				secondWeight = weight;
			}
			//Otherwise, if the third spot is empty or a worse path than the current one
			else if(weight < thirdWeight || thirdWeight == -1)
			{
				thirdPath = current;
				thirdWeight = weight;
			}
		}

		List<List<City>> output = new ArrayList<>();
		output.add(firstPath);
		output.add(secondPath);
		output.add(thirdPath);

		return output;
	}
}