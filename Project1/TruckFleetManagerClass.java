

import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map;


public class TruckFleetManagerClass {
    //private TreeMap<Integer, ParkingLotClass> parkingLotTree;
    private AVLTree_Imp parkingLotTree;

    public TruckFleetManagerClass() {
        //this.parkingLotTree = new TreeMap<>();
        this.parkingLotTree = new AVLTree_Imp();
    }

    public void createParkingLot(int capacityConstraint, int truckLimit) {
//        if (!parkingLotTree.containsKey(capacityConstraint)) {
//            parkingLotTree.put(capacityConstraint, new ParkingLotClass(capacityConstraint, truckLimit));
//        }
        if (parkingLotTree.searchLot(capacityConstraint) == null)
        {
            parkingLotTree.insert(capacityConstraint, new ParkingLotClass(capacityConstraint, truckLimit));
        }
    }

    public void deleteParkingLot(int capacityConstraint) {
        //parkingLotTree.remove(capacityConstraint);
        parkingLotTree.delete(capacityConstraint);
    }

    public int addTruck(int truckId, int capacity) {
        TruckClass truck = new TruckClass(truckId, capacity);
        ParkingLotClass lot = findSuitableLotForTruck(truck);
        if (lot != null) {
            lot.addTrucktoLot(truck);
            return lot.getLotCapacityConstraint();
        }
        return -1;
    }

    public ParkingLotClass getNextLargerParkingLot(int currentCapacityConstraint) {
        // Use TreeMap's higherEntry method to find the entry with the smallest key that is greater than the current key
//        Map.Entry<Integer, ParkingLotClass> entry = parkingLotTree.higherEntry(currentCapacityConstraint);
        ParkingLotClass larger_lot = parkingLotTree.getNextLargerLot(currentCapacityConstraint);
        return larger_lot;
    }

    private ParkingLotClass findSuitableLotForTruck(TruckClass truck) {
        int capacity = truck.getTruckMaxCapacity();
//        ParkingLotClass suitableLot = parkingLotTree.get(capacity);
        ParkingLotClass suitableLot = parkingLotTree.searchLot(capacity);
        if (suitableLot == null || suitableLot.isLotFull()) {
//            NavigableMap<Integer, ParkingLotClass> smallerLots = parkingLotTree.headMap(capacity, false);
//            for (ParkingLotClass lot : smallerLots.descendingMap().values()) {
//                if (!lot.isLotFull()) {
//                    return lot;
//                }
//            }
//            return null;
            return parkingLotTree.findFirstAvailableSmallerLot(capacity);
        }
        return suitableLot;
    }

    public String markReady(int capacityConstraint) {
        // Get the parking lot with the exact capacity constraint
//        ParkingLotClass lot = parkingLotTree.get(capacityConstraint);
        ParkingLotClass lot = parkingLotTree.searchLot(capacityConstraint);

        // Check if the lot exists and has a truck in the waiting section
        if (lot != null) {
            TruckClass readyTruck = lot.moveToReadyLot(); // Move the earliest truck to the ready section if available
            if (readyTruck != null) {
                return readyTruck.getTruckId() + " " + lot.getLotCapacityConstraint();
            }
        }

        // If no truck is found, check the larger capacity lots
//        NavigableMap<Integer, ParkingLotClass> largerLots = parkingLotTree.tailMap(capacityConstraint, false);
//        for (Map.Entry<Integer, ParkingLotClass> entry : largerLots.entrySet()) {
//            ParkingLotClass largerLot = entry.getValue();
//            TruckClass readyTruck = largerLot.moveToReadyLot(); // Try moving the earliest truck from this lot
//
//            if (readyTruck != null) {
//                // Return the truck ID and the capacity constraint of the parking lot it was found in
//                return readyTruck.getTruckId() + " " + largerLot.getLotCapacityConstraint();
//            }
//        }
        ArrayList<AVLTree_Imp_Node> largerLots = new ArrayList<>();
        parkingLotTree.getNodesGreaterThan(capacityConstraint, largerLots);

        for (AVLTree_Imp_Node node : largerLots) {
            ParkingLotClass largerLot = node.lot_obj;
            TruckClass readyTruck = largerLot.moveToReadyLot(); // Try moving the earliest truck from this lot
            if (readyTruck != null) {
                return readyTruck.getTruckId() + " " + largerLot.getLotCapacityConstraint();
            }
        }
        // If no truck is found in any lot, return "-1"
        return "-1";
    }

    public String loadToParkingLot(int capacityConstraint, int loadAmount) {
//        ParkingLotClass lot = parkingLotTree.get(capacityConstraint);
        ParkingLotClass lot = parkingLotTree.searchLot(capacityConstraint);

        StringBuilder output = new StringBuilder();

        // Distribute load in the current lot if found
        if (lot != null) {
            loadAmount = distributeLoadToReadySection(loadAmount, lot, output);
        }

        // Continue distributing any remaining load to larger lots
        while (loadAmount > 0) {
            ParkingLotClass largerLot = getNextLargerParkingLot(capacityConstraint);
            if (largerLot != null) {
                capacityConstraint = largerLot.getLotCapacityConstraint();
                loadAmount = distributeLoadToReadySection(loadAmount, largerLot, output);
            } else {
                // Remaining load goes to waste if no larger lot is found
                break;
            }
        }

        // Remove trailing " - " if present or append "-1" if no trucks were loaded
        if (!output.isEmpty()) {
            if (output.toString().endsWith(" - ")) {
                output.setLength(output.length() - 3);
            }
        } else {
            output.append("-1");
        }

        return output.toString();
    }




    private void moveTruckToNewLot(TruckClass truck, int capacityConstraint, StringBuilder output) {
//        ParkingLotClass newLot = parkingLotTree.get(capacityConstraint);
        ParkingLotClass newLot = parkingLotTree.searchLot(capacityConstraint);

        if (newLot != null && !newLot.isLotFull()) {
            newLot.addTrucktoLot(truck);
            output.append(capacityConstraint);
        } else {
            // Find the largest available lot with a smaller capacity
            ParkingLotClass bestLot = null;
            int bestCapacity = -1;

//            for (Map.Entry<Integer, ParkingLotClass> entry : parkingLotTree.headMap(capacityConstraint, false).descendingMap().entrySet()) {
//                ParkingLotClass smallerLot = entry.getValue();
//                if (!smallerLot.isLotFull()) {
//                    bestLot = smallerLot;
//                    bestCapacity = entry.getKey();
//                    break;
//                }
//            }
            ParkingLotClass available_smaller_lot = parkingLotTree.findFirstAvailableSmallerLot(capacityConstraint);
            if (available_smaller_lot != null) {
                bestLot = available_smaller_lot;
                bestCapacity = available_smaller_lot.getLotCapacityConstraint();
            }

            if (bestLot != null) {
                bestLot.addTrucktoLot(truck);
                output.append(bestCapacity);
            } else {
                // No suitable lot found, append "-1"
                output.append("-1");
            }
        }
    }


    public int distributeLoadToReadySection(int loadAmount, ParkingLotClass lot, StringBuilder output) {
        int totalLoadTaken = 0;
        lot.getFullyLoadedTrucks().clear(); // Clear the list before each call

        for (TruckClass truck : new ArrayList<>(lot.getReadySection())) {
            if (loadAmount <= 0) break;

            int loadToGive = Math.min(loadAmount, lot.getLotCapacityConstraint());
           // int loadToGive = Math.min(loadAmount, truck.getRemainingCapacity());
            truck.addLoad(loadToGive);
            totalLoadTaken += loadToGive;
            loadAmount -= loadToGive;

            lot.getReadySection().remove(truck);

            // Append truck information only if load was distributed
            if (loadToGive > 0) {
                if (!output.isEmpty()) {
                    output.append(" - ");
                }
                output.append(truck.getTruckId()).append(" ");//.append(lot.getLotCapacityConstraint());
            }

            // Check if the truck is fully loaded and handle accordingly
            if (truck.isTrackFull()) {
                lot.getFullyLoadedTrucks().add(truck);
                truck.unloadTruck(); // Unload the truck after it becomes full
                moveTruckToNewLot(truck, truck.getTruckMaxCapacity(), output);
            } else {
                // If the truck is partially loaded, move it to a lot matching its remaining capacity
                int remainingCapacity = truck.getRemainingCapacity();
                moveTruckToNewLot(truck, remainingCapacity, output);
            }
        }

        return loadAmount;
       // return totalLoadTaken;
    }


    public int countTrucks(int minCapacity) {
//        int count = 0;
//        for (Map.Entry<Integer, ParkingLotClass> entry : parkingLotTree.tailMap(minCapacity, false).entrySet()) {
//            count += entry.getValue().getTruckCount();
//        }
//        return count;
        return parkingLotTree.sumTruckCountsAbove(minCapacity);

    }

}
