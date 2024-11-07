import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = args[1];

        TruckFleetManagerClass manager = new TruckFleetManagerClass();

        // Measure the start time
        long startTime = System.nanoTime();

        // Parse input and execute commands
        try (Scanner scanner = new Scanner(new File(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            while (scanner.hasNextLine()) {
                String[] command = scanner.nextLine().split(" ");
                switch (command[0]) {
                    case "create_parking_lot":
                        manager.createParkingLot(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
                        break;
                    case "delete_parking_lot":
                        manager.deleteParkingLot(Integer.parseInt(command[1]));
                        break;
                    case "add_truck":
                        int truckLot = manager.addTruck(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
                        writer.write(truckLot + "\n");
                        break;
                    case "ready":
                        String readyOutput = manager.markReady(Integer.parseInt(command[1]));
                        writer.write(readyOutput + "\n");
                        break;
                    case "load":
                        String loadOutput = manager.loadToParkingLot(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
                        writer.write(loadOutput + "\n");
                        break;
                    case "count":
                        int count = manager.countTrucks(Integer.parseInt(command[1]));
                        writer.write(count + "\n");
                        break;
                }
            }
            // Measure the end time
            long endTime = System.nanoTime();
            // Calculate the time taken
            long duration = endTime - startTime; // duration in nanoseconds
            double durationInSeconds = duration / 1_000_000_000.0; // convert to milliseconds

            // Print the time taken
            System.out.println("Time taken: " + durationInSeconds + " s");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
