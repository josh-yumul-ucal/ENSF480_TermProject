package flightResSys;

import static flightResSys.UserRole.*;

public class FlightViewFactory {
  public static FlightView createFlightView(UserRole role){
    if (role == CUSTOMER){
      return new CustomerFlightView();
    } else if (role == FLIGHT_AGENT){
      return new AgentFlightView();
    } else if (role == ADMIN){
      return new AdminFlightView();
    }
    throw new IllegalArgumentException("Invalid User Role");
  }
}
