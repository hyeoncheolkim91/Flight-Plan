package flight2;

import java.util.ArrayList;
import java.util.List;

class City
{
	public final String NAME;
	public final List<City> DESTINATIONS = new ArrayList<>();
	public final List<Double> COSTS = new ArrayList<>();
	public final List<Integer> TIMES = new ArrayList<>();

	public City(String name)
	{
		NAME = name;
	}

	public void addDestination(City destination, String cost, String time)
	{
		DESTINATIONS.add(destination);
		COSTS.add(Double.parseDouble(cost));
		TIMES.add(Integer.parseInt(time));
	}

	//Returns the cost of a flight to the destination city from the current city
	public Double getCost(String destination)
	{
		Double output = -1.0;

		int i = 0;
		for(City city: DESTINATIONS)
		{
			if(city.NAME.equals(destination))
				output = COSTS.get(i);
			else
				i++;
		}

		return output;
	}

	//Returns the time of a flight to the destination city from the current city
	public int getTime(String destination)
	{
		int output = -1;

		int i = 0;
		for(City city: DESTINATIONS)
		{
			if(city.NAME.equals(destination))
				output = TIMES.get(i);
			else
				i++;
		}

		return output;
	}
}