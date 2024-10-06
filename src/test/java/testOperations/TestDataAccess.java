package testOperations;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import domain.Booking;
import domain.Driver;
import domain.Movement;
import domain.Ride;
import domain.Traveler;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;


public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestDataAccess()  {
		
		System.out.println("TestDataAccess created");

		//open();
		
	}

	
	public void open(){
		

		String fileName=c.getDbFilename();
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
		System.out.println("TestDataAccess opened");

		
	}
	public void close(){
		db.close();
		System.out.println("TestDataAccess closed");
	}

	public boolean removeDriver(String name) {
		System.out.println(">> TestDataAccess: removeDriver");
		Driver d = db.find(Driver.class, name);
		if (d!=null) {
			db.getTransaction().begin();
			db.remove(d);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
	public Driver createDriver(String name, String pass) {
		System.out.println(">> TestDataAccess: addDriver");
		Driver driver=null;
			db.getTransaction().begin();
			try {
			    driver=new Driver(name,pass);
				db.persist(driver);
				db.getTransaction().commit();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return driver;
    }
	public boolean existDriver(String email) {
		 return  db.find(Driver.class, email)!=null;
		 

	}
		
		public Driver addDriverWithRide(String name, String from, String to,  Date date, int nPlaces, float price) {
			System.out.println(">> TestDataAccess: addDriverWithRide");
				Driver driver=null;
				db.getTransaction().begin();
				try {
					 driver = db.find(Driver.class, name);
					if (driver==null) {
						System.out.println("Entra en null");
						driver=new Driver(name,null);
				    	db.persist(driver);
					}
				    driver.addRide(from, to, date, nPlaces, price);
					db.getTransaction().commit();
					System.out.println("Driver created "+driver);
					
					return driver;
					
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return null;
	    }
		
		
		public boolean existRide(String name, String from, String to, Date date) {
			System.out.println(">> TestDataAccess: existRide");
			Driver d = db.find(Driver.class, name);
			if (d!=null) {
				return d.doesRideExists(from, to, date);
			} else 
			return false;
		}
		public Ride removeRide(String name, String from, String to, Date date ) {
			System.out.println(">> TestDataAccess: removeRide");
			Driver d = db.find(Driver.class, name);
			if (d!=null) {
				db.getTransaction().begin();
				Ride r= d.removeRide(from, to, date);
				db.getTransaction().commit();
				System.out.println("created rides" +d.getCreatedRides());
				return r;

			} else 
			return null;

		}

	    public void persistBooking(Booking booking) {
	        db.getTransaction().begin();
	        try {
	            db.persist(booking);
	            db.getTransaction().commit();
	        } catch (Exception e) {
	            if (db.getTransaction().isActive()) {
	                db.getTransaction().rollback();
	            }
	            e.printStackTrace();
	        }
	    }
	    
	    public void addMovement(User user, String eragiketa, double amount) {
	        try {
	            db.getTransaction().begin();
	            Movement movement = new Movement(user, eragiketa, amount);
	            db.persist(movement);
	            db.getTransaction().commit();
	        } catch (Exception e) {
	            e.printStackTrace();
	            db.getTransaction().rollback();
	        }
	    }
	    
	    public void cancelRide(Ride ride) {
	        try {
	            db.getTransaction().begin();

	            for (Booking booking : ride.getBookings()) {
	                if (booking.getStatus().equals("Accepted") || booking.getStatus().equals("NotDefined")) {
	                    double price = booking.prezioaKalkulatu();
	                    Traveler traveler = booking.getTraveler();
	                    double frozenMoney = traveler.getIzoztatutakoDirua();
	                    traveler.setIzoztatutakoDirua(frozenMoney - price);

	                    double money = traveler.getMoney();
	                    traveler.setMoney(money + price);
	                    db.merge(traveler); // Asegúrate de que estás actualizando al Traveler en la DB
	                    addMovement(traveler, "BookDeny", price); // Si tienes este método implementado
	                    db.getTransaction().commit();
	                    db.getTransaction().begin(); // Comenzar una nueva transacción
	                }
	                booking.setStatus("Rejected");
	                db.merge(booking); // Actualiza el booking en la base de datos
	            }
	            ride.setActive(false); // Cambiar estado del ride
	            db.merge(ride); // Actualiza el ride en la base de datos

	            db.getTransaction().commit();
	        } catch (Exception e) {
	            if (db.getTransaction().isActive()) {
	                db.getTransaction().rollback();
	            }
	            e.printStackTrace();
	        }
	    }
		
	    
	    public Traveler getTraveler(String username) {
	        System.out.println(">> TestDataAccess: getTraveler");
	        TypedQuery<Traveler> query = db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username", Traveler.class);
	        query.setParameter("username", username);
	        List<Traveler> resultList = query.getResultList();
	        if (resultList.isEmpty()) {
	            return null;
	        } else {
	            return resultList.get(0);
	        }
	    }
	    
	    public Driver addDriver(String username, String password) {
	        System.out.println(">> TestDataAccess: addDriver");
	        Driver driver = null;
	        db.getTransaction().begin();
	        try {
	            Driver existingDriver = db.find(Driver.class, username);
	            Traveler existingTraveler = db.find(Traveler.class, username);
	            if (existingDriver != null || existingTraveler != null) {
	                return null; // Retorna null si ya existe un Driver o Traveler con el mismo nombre
	            }

	            driver = new Driver(username, password);
	            db.persist(driver);
	            db.getTransaction().commit();
	        } catch (Exception e) {
	            e.printStackTrace();
	            if (db.getTransaction().isActive()) {
	                db.getTransaction().rollback();
	            }
	        }
	        return driver; // Retorna el Driver creado o null si hubo un error
	    }
	    
	    public boolean isRideActive(Ride ride) {
	        System.out.println(">> TestDataAccess: isRideActive");

	        if (ride != null) {
	            // Comprobar si tiene bookings activos
	            for (Booking booking : ride.getBookings()) {
	                if (booking.getStatus().equals("Accepted") || booking.getStatus().equals("NotDefined")) {
	                    return true; // Hay bookings activos
	                }
	            }
	        }
	        return false; // No hay bookings activos
	    }
	    
		public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverName)
				throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
			System.out.println(
					">> TestDataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverName + " date " + date);
			if (driverName==null) return null;
			try {
				if (new Date().compareTo(date) > 0) {
					System.out.println("ppppp");
					throw new RideMustBeLaterThanTodayException(
							ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
				}

				db.getTransaction().begin();
				Driver driver = db.find(Driver.class, driverName);
				if (driver.doesRideExists(from, to, date)) {
					db.getTransaction().commit();
					throw new RideAlreadyExistException(
							ResourceBundle.getBundle("Etiquetas").getString("TestDataAccess.RideAlreadyExist"));
				}
				Ride ride = driver.addRide(from, to, date, nPlaces, price);
				// next instruction can be obviated
				db.persist(driver);
				db.getTransaction().commit();

				return ride;
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				return null;
			}
			

		}
}