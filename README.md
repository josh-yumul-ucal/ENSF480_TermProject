# Github Repo for ENSF 480 Term Project

## Members: 
- Josh Yumul
- Tyler Birkbeck
- Li Tan
- Mustafa Abeidi

## General information
We will be using SQLite to handle the data storage

Three User Roles/Abilities (subject to change):
1. Customer
    - Search flights
    - Make/cancel bookings 
    - view reservations
2. Flight Agent
    - Manage customer data
    - Modify reservations
    - View flight schedules 
3. System Administrator
    - Add/remove flights
    - Manage routes and aircraft
    - Update schedules

- Need to create an overarching schedule that controls when flights happen ()

- Simulate payments? 

# Classes
## Boundary Classes: one for each actor/use-case pair
<u>Customer</u>
- bookFlight -> addCustomerToManifest
- viewAllFlights -> [method that queries the database and returns all future flights, maybe filter by flights that start closest to customer]
- viewCustomerFlights -> [method that queries database and returns all flights that contains the customer's unique ID]
- searchFlights -> [create a generalized method that can query the database and returns the ones with the tags the customer requests] (probably a facade pattern to obfuscate we add a bunch of subsystems for the search bar)

Note: Use an interface to define the flight viewer classes probably

<u>System Admin</u>
- manageAircrafts == add/remove aircrafts from airport hangar table

Probably use a Singleton pattern for each flight


# How to run:
Check that Maven and Java 17 or later is installed on your machine, the proceeding terminal commands should return information on your Maven and JDK installations. 
```powershell
mvn --version
java --version
javac --version
```

Compile and run the project.
```powershell
mvn clean compile
mvn javafx:run
```

Alternatively, compile and run in the same line
```powershell
mvn compile javafx:run
```

I'm not sure if this is how it works on MacOS (sorry Tyler)