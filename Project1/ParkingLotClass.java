import java.util.ArrayList;

public class ParkingLotClass {
    private int parkinLotCapacityConstraint;
    private int parkingLotTruckLimit;
    private ArrayList<TruckClass> waitingSection;
    private ArrayList<TruckClass> readySection;
    private ArrayList<TruckClass> fullyLoadedTrucks = new ArrayList<>();

    public ParkingLotClass(int capacityConstraint, int parkingLotTruckLimit) {
        this.parkinLotCapacityConstraint = capacityConstraint;
        this.parkingLotTruckLimit = parkingLotTruckLimit;
        this.waitingSection = new ArrayList<>();
        this.readySection = new ArrayList<>();
        this.fullyLoadedTrucks = new ArrayList<>();
    }

    public int getLotCapacityConstraint() {
        return parkinLotCapacityConstraint;
    }

    public boolean isLotFull() {
        return (waitingSection.size() + readySection.size()) >= parkingLotTruckLimit;
    }

    public boolean addTrucktoLot(TruckClass truck) {
        if (!isLotFull()) {
            waitingSection.add(truck);
            return true;
        }
        return false;
    }

    public TruckClass moveToReadyLot() {
        if (!waitingSection.isEmpty()) {
            TruckClass truck = waitingSection.remove(0);
            readySection.add(truck);
            return truck;
        }
        return null;
    }

    public ArrayList<TruckClass> getReadySection() {
        return readySection;
    }

    public ArrayList<TruckClass> getFullyLoadedTrucks() {
        return new ArrayList<>(fullyLoadedTrucks);
    }

    public int getTruckCount() {
        return waitingSection.size() + readySection.size();
    }



}

