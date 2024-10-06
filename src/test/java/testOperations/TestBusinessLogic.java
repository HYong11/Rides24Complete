package testOperations;

import java.util.Date;

import configuration.ConfigXML;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class TestBusinessLogic {
	TestDataAccess dbManagerTest;
 	
    
	   public TestBusinessLogic()  {
			
			System.out.println("Creating TestBusinessLogic instance");
			@SuppressWarnings("unused")
			ConfigXML c=ConfigXML.getInstance();
			dbManagerTest=new TestDataAccess(); 
			dbManagerTest.close();
		}
		
		 
		public boolean removeDriver(String driverEmail) {
			dbManagerTest.open();
			boolean b=dbManagerTest.removeDriver(driverEmail);
			dbManagerTest.close();
			return b;

		}
		
		public Driver createDriver(String email, String name) {
			dbManagerTest.open();
			Driver driver=dbManagerTest.createDriver(email, name);
			dbManagerTest.close();
			return driver;

		}
		
		public boolean existDriver(String email) {
			dbManagerTest.open();
			boolean existDriver=dbManagerTest.existDriver(email);
			dbManagerTest.close();
			return existDriver;

		}
		
		public Driver addDriverWithRide(String name, String from, String to,  Date date, int nPlaces, float price) {
			dbManagerTest.open();
			Driver driver=dbManagerTest.addDriverWithRide(name, from, to, date, nPlaces, price);
			dbManagerTest.close();
			return driver;

		}
		public boolean existRide(String email, String from, String to, Date date) {
			dbManagerTest.open();
			boolean b=dbManagerTest.existRide(email, from, to, date);
			dbManagerTest.close();
			return b;
		}
		public Ride removeRide(String email,String from, String to, Date date ) {
			dbManagerTest.open();
			Ride r=dbManagerTest.removeRide( email, from,  to,  date );
			dbManagerTest.close();
			return r;
		}
		
	    public void persistBooking(Booking booking) {
	        dbManagerTest.open();
	        dbManagerTest.persistBooking(booking);
	        dbManagerTest.close();
	    }
	    
	    
	    public void cancelRide(Ride ride) {
	        dbManagerTest.open();
	        dbManagerTest.cancelRide(ride); // Llama al método de TestDataAccess
	        dbManagerTest.close();
	    }
	    
	    public Traveler getTraveler(String username) {
	        dbManagerTest.open();
	        Traveler traveler= dbManagerTest.getTraveler(username); // Llama al método de TestDataAccess
	        dbManagerTest.close();
			return traveler;
	    }
	    
	    public void addMovement(User user, String eragiketa, double amount) {
	        dbManagerTest.open();
	        dbManagerTest.addMovement(user, eragiketa, amount);
	        dbManagerTest.close();
	    }

	    public Driver addTraveler(String username, String password) {
	        dbManagerTest.open();
	        Driver driver = dbManagerTest.addDriver(username, password); // Llama al método en TestDataAccess
	        dbManagerTest.close();
	        return driver; // Retorna el Driver creado o null si hubo un error
	    }
	    
	    public boolean isRideActive(Ride ride) {
	        dbManagerTest.open();
	        boolean isActive = dbManagerTest.isRideActive(ride);
	        dbManagerTest.close();
	        return isActive; // Devuelve el resultado de TestDataAccess
	    }
	    
		public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverName) throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
	        dbManagerTest.open();
	        Ride ride = dbManagerTest.createRide(from,to,date,nPlaces,price,driverName);
	        dbManagerTest.close();
	        return ride; // Devuelve el resultado de TestDataAccess
	    }
}
