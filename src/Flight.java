enum FlightType {Arrival, Departure};

class Flight {
    String flightNumber;
    FlightType flightType;
    int minuteInQueue;
    int minuteOutQueue;


    // constructor 
    public Flight(String flightNumber, FlightType flightType) {
        this.flightNumber = flightNumber;
        this.flightType = flightType;
    }

    public String toString() {
        return "Flight " + flightType + ": " + flightNumber;
    }

    //  "minute" that flight entered the queue
    public void setMinuteInQueue(int minute) {
        this.minuteInQueue = minute;
    }

    // "minute" that flight exits the queue
    // difference is time in queue
    public void setMinuteOutQueue(int minute) {
        this.minuteOutQueue = minute;
    }

    public int timeInQueue() {
        return minuteOutQueue - minuteInQueue;
    }
}