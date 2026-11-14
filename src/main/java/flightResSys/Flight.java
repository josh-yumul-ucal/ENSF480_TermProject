package flightResSys;

/**
 * Flight details associated with an aircraft in the database.
 * Database fields and their mapping will be added when the schema is finalized.
 */
public class Flight {
    private int aircraftId;

    // TODO: Add the remaining private flight members here.

    /*
     * Creates a flight using the aircraft's unique database ID.
     * Currently stores the ID only; database loading is not implemented yet
     * aircraftId is the unique ID of the aircraft in the database
     */
    public Flight(int aircraftId) {
        this.aircraftId = aircraftId;
        // function below parses flight database by unique ID and returns relevant data
        loadFlightDetails();
    }

    private void loadFlightDetails() {
        /* TODO: Query the finalized database using aircraftId and assign
        * the returned values to this flight's private members
        * probably something like takeOffOrigin as country of origin and
        * airportCode as the three letter designation of that country (e.g. YYC)
        */
    }
}
