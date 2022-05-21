import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        double meanArrivalFreq = 0.0;
        double meanDepartureFreq = 0.0;

        AirTrafficControlSim sim = new AirTrafficControlSim();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter mean departure frequency (0.0 < df > 1.0): ");

        if  (scanner.hasNextDouble())
        {
            meanDepartureFreq = scanner.nextDouble();
            scanner.nextLine();
        }

        System.out.println("Enter mean arrival frequency   0.0 < af > 1.0): ");
        if  (scanner.hasNextDouble())
        {
            meanArrivalFreq = scanner.nextDouble();
            scanner.nextLine();
        }



        //  Each i in the loop represents a minute, 720 x 2 = 1440 minutes in 24 hours
        for (int i = 0; i < 720; i++)
        {
            sim.processArrival(meanArrivalFreq);
            sim.processDeparture(meanDepartureFreq);
        }

        // Print out statistics calculated from the simulation.
        sim.printSimSummaryStatistics();
    }
}
