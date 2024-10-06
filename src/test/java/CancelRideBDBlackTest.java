import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import configuration.UtilDate;
import dataAccess.DataAccess;
import domain.Booking;
import domain.Car;
import domain.Driver;
import domain.Movement;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

public class CancelRideBDBlackTest {
	// sut: system under test
	private static DataAccess sut = new DataAccess();
	
    //additional operations needed to execute the test 
	private static TestDataAccess testDA = new TestDataAccess();
	
//
//	private Driver driver;
//	private Traveler traveler;
//	private Booking book;
	
	@Test
    //sut.cancelRide: Ride is null, cancelRide should jump to the exception.
	public void test1() {

//	    String driverUsername = "Driver1";
//
//	    traveler = new Traveler("Unax", "789");
//		traveler.setIzoztatutakoDirua(68);
//		traveler.setMoney(100);
//		traveler.setBalorazioa(14);
//		traveler.setBalkop(4);
//		ride = a
//		book = new Booking(ride, traveler, 2);
//		book.setStatus("Accepted");
//		////////////////////////////////////////////
//	    Calendar cal = Calendar.getInstance();
//	    cal.set(2024, Calendar.MAY, 20);
//	    Date rideDate = cal.getTime();
//	    ///////////////////////////////
//		Driver driver1 = new Driver("Urtzi", "123");
//		driver1.setMoney(15);
//		driver1.setBalorazioa(14);
//		driver1.setBalkop(3);
//		
//		Traveler traveler1 = new Traveler("Unax", "789");
//		traveler1.setIzoztatutakoDirua(68);
//		traveler1.setMoney(100);
//		traveler1.setBalorazioa(14);
//		traveler1.setBalkop(4);
//	    
//		driver1.addRide("Donostia", "Madrid", rideDate, 5, 20); //ride1
//		Ride ride1 = driver1.getCreatedRides().get(0);
//		
//		Booking book = new Booking(ride1, traveler, 2);
//		book.setStatus("Accepted");
//		testDA.persistBooking(book);
		try {
//			testDA.open();
//			testDA.createRide("Donostia", "Madrid", rideDate, 5, 20, driverUsername);
//			testDA.close();
			
			Ride ride = null;
			
			sut.open();
			sut.cancelRide(ride);
			sut.close();
			
		} catch (Exception e) {
            //if the program goes to this point, fail
            fail();
        }
		
//	        testDA.open();
//			driver =  testDA.createDriver(driverUsername, null);  
//			testDA.persistBooking(null);
//			
//			 Driver driver1 = new Driver("Urtzi", "123");
//			 driver1.setMoney(15);
//				driver1.setBalorazioa(14);
//				driver1.setBalkop(3);
//				ride = driver.addRide(rideFrom, rideTo, rideDate, 2, 10);
//
//			ride = null;
//			Booking book = new Booking(ride, traveler, 2);
//			book.setStatus("Accepted");
//			
//			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//			Date rideDate=null;;
//			try {
//				rideDate = sdf.parse("05/10/2026");
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}	
//			
//			testDA.open();
//			testDA.persistBooking(book);
//			testDA.open();
//			sut.open();
//			ride = sut.createRide(rideFrom, rideTo, rideDate, 2, 10, "driverUsername");
//			sut.close();
//
//			// Cancelar el viaje
//			sut.open();
//			sut.cancelRide(ride);
//			sut.close();
//			
//			// verify the results
//			assertNotNull(ride);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			fail();
//		}
//		
//	 			Driver driver1 = new Driver("Urtzi", "123");
//			driver1.setMoney(15);
//			driver1.setBalorazioa(14);
//			driver1.setBalkop(3);
//		ride = driver.addRide(rideFrom, rideTo, rideDate, 2, 10);
//		driver.setMoney(15);
//		driver.setBalorazioa(14);
//		driver.setBalkop(3);
//		ride = driver.getCreatedRides().get(0);
//		Traveler traveler = new Traveler("Unax", "789");
//		traveler.setIzoztatutakoDirua(68);
//		traveler.setMoney(100);
//		traveler.setBalorazioa(14);
//		traveler.setBalkop(4);
//		Booking book1 = new Booking(ride, traveler, 2);
//		book1.setStatus("Accepted");
//
//		db.persist(book1);
//
//		traveler1.addBookedRide(book1);
//		db.merge(traveler1);
//		Car c1 = new Car("1234ABC", "Renault", 5);
//		driver1.addCar(c1);
//		db.persist(c1);
//*/
//
//
//		// Verificar que el viaje fue cancelado
//
//		boolean exist = testDA.existRide("Urtzi", rideFrom, rideTo, rideDate);
//		assertTrue(!exist);
	}

	@Test
	//sut.cancelRide: Ride is null, cancelRide should jump to the exception.
	public void test2() {
		List<Booking> booking = new LinkedList<>();
		ride = null;
		// Intentar cancelar el viaje no existente
		sut.open();
		sut.cancelRide(ride); // Ha saltado a exception
		sut.close();

	}

	@Test
	public void test3_CancelAlreadyCanceledRide() {
		// Crear un viaje
		driver = new Driver("Urtzi", "123");
		ride = driver.addRide(rideFrom, rideTo, rideDate, 2, 10);

		// Cancelar el viaje
		sut.open();
		sut.cancelRide(ride);
		sut.close();

		// Verificar que el viaje fue cancelado
		boolean exist = testDA.existRide("Urtzi", rideFrom, rideTo, rideDate);
		assertTrue(!exist);

		// Intentar cancelar de nuevo el mismo viaje
		sut.open();
		sut.cancelRide(ride);
		sut.close();

	}

	@Test
	public void test4_CancelRideWithNull() {
		// Intentar cancelar un viaje con un objeto nulo
		sut.open();
		sut.cancelRide(null);
		sut.close();

		// Verificar que no hay cambios en el estado del sistema
		List<Ride> rides = sut.getRidesByDriver(driverUsername);
		assertEquals(0, rides.size());
	}

	@Test
	public void test5_CancelRideWithInvalidData() {
		ride = null;
		ride.setFrom(null); // Origen nulo
		ride.setTo(null); // Destino nulo
		ride.setDate(rideDate);

		// Intentar cancelar el viaje
		sut.open();
		sut.cancelRide(ride);
		sut.close();

		// Verificar que no hay cambios en el estado del sistema
		List<Ride> rides = sut.getRidesByDriver(driverUsername);
		assertEquals(0, rides.size()); // Si el viaje no fue creado, la lista debe estar vacía
	}

	@Test
	public void test6_CancelRideWithNullFromAndTo() {
		// Crear un viaje
		testDA.addDriverWithRide(driverUsername, rideFrom, rideTo, rideDate, 2, 10);
		List<Ride> rides = sut.getRidesByDriver(driverUsername);
		assertEquals(1, rides.size());

		// Crear un viaje con origen y destino nulos
		Ride invalidRide = rides.get(0);
		invalidRide.setFrom(null);
		invalidRide.setTo(null);

		// Intentar cancelar el viaje
		sut.open();
		sut.cancelRide(invalidRide);
		sut.close();

		// Verificar que el viaje sigue existente
		List<Ride> updatedRides = sut.getRidesByDriver(driverUsername);
		assertEquals(1, updatedRides.size());
	}
}
