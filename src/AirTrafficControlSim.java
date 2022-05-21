import java.util.ArrayDeque;
import java.util.ArrayList;

public class AirTrafficControlSim
{
    final int MIN_FLIGHT_SPACING = 10; //Minimum waiting time for a flight to take off or departure
    int flight_number; //Incremented flight number
    int timerCounter; //Keep track of when a flight can arrive or departure
    int timeInterval; //Keep track of time (Clock)
    int idleInterval; //Keep track of idle time

    ArrayDeque<Flight> arrivalQueue = new ArrayDeque<>(); //Arriving Queue
    ArrayDeque<Flight> departureQueue = new ArrayDeque<>(); //Departing Queue

    ArrayList<Flight> reroutedStatistics = new ArrayList<>(); //Static Info
    ArrayList<Flight> delayedStatistics =  new ArrayList<>(); //Static Info

    ArrayList<Flight> arrivalStatistics = new ArrayList<>(); //Static Info
    ArrayList<Flight> departureStatistics =  new ArrayList<>(); //Static Info

    public void printSimSummaryStatistics()
    {
        System.out.println("******************************************");
        System.out.println("       Simulator Summary Statistics      ");
        System.out.println("******************************************");
        System.out.println("Time period  stimulated: " + (timeInterval/60 + ":" + String.format("%02d",timeInterval % 60)));
        System.out.println("Number of arrivals: " + arrivalStatistics.size());
        System.out.println("Number of departures: " + departureStatistics.size());
        System.out.println("Total number of flights handled: " + (arrivalStatistics.size() + departureStatistics.size()));
        System.out.println("Average number of arrival per hour: " + arrivalStatistics.size() / 24);
        System.out.println("Average number of departure per hour: " + departureStatistics.size() / 24);
        System.out.println("Departures remaining in queue: " + departureQueue.size());
        System.out.println("Arrivals remaining in queue: " + arrivalQueue.size());
        System.out.println("Number of rerouted Arrivals: " + reroutedStatistics.size());
        System.out.println("Number of delayed Departures: " + delayedStatistics.size());
        System.out.printf("Percent time idle runway: %.2f percent\n", ((double) idleInterval/timeInterval *100));
        System.out.println("Average departure time in queue: " + averageDepartureTime() + " minutes");
        System.out.println("Average arrival time in queue: " + averageArrivalTime() + " minutes");
        System.out.println("******************************************");
    }

    private int averageDepartureTime() {
        int sum = 0;

        for(Flight flight : departureStatistics)
        {
            sum += flight.timeInQueue();
        }

        return  sum/departureStatistics.size();
    }

    private int averageArrivalTime() {
        int sum = 0;

        for(Flight flight : arrivalStatistics)
        {
            sum += flight.timeInQueue();
        }

        return  sum/arrivalStatistics.size();
    }

    public static int getPoissonRandom(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);
        return k - 1;
    }


    void processArrival(double meanArrivalFreq) {
        int count = 0;
        timerCounter++;
        timeInterval++;
        if ((count = getPoissonRandom(meanArrivalFreq)) > 0)
        {
            addToArrivalQueue(count);
        }
        else
        {
            if(arrivalQueue.isEmpty())
            {
                idleInterval++;
            }
        }

        if (timerCounter >= MIN_FLIGHT_SPACING)
        {
            if (arrivalQueue.size() > 0)
            {
                timerCounter = 0;
                Flight flight = arrivalQueue.pop();
                flight.setMinuteOutQueue(timeInterval);
                arrivalStatistics.add(flight);
                System.out.println(flight + " arrived at " + (timeInterval/60 + ":" + String.format("%02d",timeInterval % 60)));

            }
        }
    }

    private void addToArrivalQueue(int count) {
        while(count > 0)
        {
            Flight new_flight = new Flight("AA" + ++flight_number , FlightType.Arrival);

            if(arrivalQueue.size() < 5)
            {
                new_flight.setMinuteInQueue(timeInterval);
                arrivalQueue.add(new_flight);
            }
            else
            {
                System.out.println(new_flight + " rerouted at " + (timeInterval/60 + ":" + String.format("%02d",timeInterval % 60)));
                reroutedStatistics.add(new_flight);
            }
            count--;
        }
    }



    void processDeparture(double meanDepartureFreq) {
        int count = 0;
        timerCounter++;
        timeInterval++;
        if ((count = getPoissonRandom(meanDepartureFreq)) > 0)
        {
            addToDepartureQueue(count);
        }
        else
        {
            if(departureQueue.isEmpty())
            {
                idleInterval++;
            }
        }
        if (timerCounter >= MIN_FLIGHT_SPACING)
        {
            if (departureQueue.size() > 0)
            {
                timerCounter = 0;
                Flight flight = departureQueue.pop();
                flight.setMinuteOutQueue(timeInterval);
                departureStatistics.add(flight);
                System.out.println(flight + " departed at " + (timeInterval/60 + ":" + String.format("%02d",timeInterval % 60)));
            }
        }
    }

    private void addToDepartureQueue(int count) {
        while(count > 0)
        {
            Flight new_flight = new Flight("UA" + ++flight_number , FlightType.Departure);

            if(arrivalQueue.isEmpty())
            {
                if(departureQueue.size() < 5)
                {
                    new_flight.setMinuteOutQueue(timeInterval);
                    departureQueue.add(new_flight);
                }
                else
                {
                    System.out.println(new_flight + " delayed at " + (timeInterval/60 + ":" + String.format("%02d",timeInterval % 60)));
                    delayedStatistics.add(new_flight);
                }
            }

            count--;
        }
    }
}

