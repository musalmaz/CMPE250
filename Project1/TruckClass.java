public class TruckClass {
    private int TruckID;
    private int maxCapacity;
    private int currentLoad;

    public TruckClass(int id, int maxCapacity) {
        this.TruckID = id;
        this.maxCapacity = maxCapacity;
        this.currentLoad = 0; // initially the truck has no load
    }

    public int getTruckId() {
        return TruckID;
    }

    public int getRemainingCapacity() {
        return maxCapacity - currentLoad;
    }

    public boolean isTrackFull() {
        return currentLoad >= maxCapacity;
    }

    public void addLoad(int load) {
        if (currentLoad + load > maxCapacity) {
            throw new IllegalArgumentException("Load exceeds truck capacity.");
        }
        currentLoad += load;
    }

    public void unloadTruck() {
        currentLoad = 0;
    }

    public int getTruckMaxCapacity() {
        return maxCapacity;
    }
}

