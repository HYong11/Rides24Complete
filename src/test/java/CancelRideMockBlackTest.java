import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Driver; // Importado para manejar Driver
import domain.Ride;

public class CancelRideMockBlackTest {

    static DataAccess sut;
    
    @Mock
    EntityManager db;
    @Mock
    EntityTransaction et;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        Mockito.doReturn(et).when(db).getTransaction();
        sut = new DataAccess(db);
    }

    @After
    public void tearDown() {
        // Cleanup after tests
    }

    @Test
    // Test when the ride exists, it should be canceled successfully
    public void testCancelRideSuccess() {
        // Crear un objeto Driver simulado
        Driver driver = Mockito.mock(Driver.class);
        
        // Crear un Ride con el parámetro Driver añadido
        Ride ride = new Ride("Donostia", "Zarautz", new Date(), 2, 10.0, driver);
        
        // Configurar los mocks
        when(db.find(Ride.class, ride.getRideNumber())).thenReturn(ride);
        
        // Invocar el método bajo prueba
        sut.open();
        try {
            sut.cancelRide(ride);
            // Verificar que el Ride fue removido de la base de datos
            verify(db, times(1)).remove(ride);
        } catch (Exception e) {
            fail("No debería lanzarse una excepción");
        } finally {
            sut.close();
        }
    }

    @Test
    // Test when the ride does not exist, it should throw RideNotFoundException
    public void testCancelRideNotFound() {
        // Crear un objeto Driver simulado
        Driver driver = Mockito.mock(Driver.class);
        
        // Crear un Ride con el parámetro Driver añadido
        Ride ride = new Ride("Donostia", "Zarautz", new Date(), 2, 10.0, driver);

        // Configurar los mocks para devolver null cuando el ride no se encuentra
        when(db.find(Ride.class, ride.getRideNumber())).thenReturn(null);
        
        // Invocar el método bajo prueba
        sut.open();
        try {
            sut.cancelRide(ride);
            fail("Debería lanzarse RideNotFoundException");
        } catch (RideNotFoundException e) {
            // Comportamiento esperado
        } finally {
            sut.close();
        }
    }

    @Test
    // Test when an exception is thrown during the cancelRide operation
    public void testCancelRideDatabaseError() {
        // Crear un objeto Driver simulado
        Driver driver = Mockito.mock(Driver.class);
        
        // Crear un Ride con el parámetro Driver añadido
        Ride ride = new Ride("Donostia", "Zarautz", new Date(), 2, 10.0, driver);

        // Configurar los mocks para lanzar una excepción durante la transacción
        when(db.find(Ride.class, ride.getRideNumber())).thenReturn(ride);
        Mockito.doThrow(new PersistenceException("Database error")).when(db).remove(ride);

        // Invocar el método bajo prueba
        sut.open();
        try {
            sut.cancelRide(ride);
            fail("Debería lanzarse PersistenceException");
        } catch (PersistenceException e) {
            // Comportamiento esperado
        } finally {
            sut.close();
        }
    }

    @Test
    // Test when the ride is already canceled (assuming there's a status field)
    public void testCancelRideAlreadyCanceled() {
        // Crear un objeto Driver simulado
        Driver driver = Mockito.mock(Driver.class);
        
        // Crear un Ride con el parámetro Driver añadido
        Ride ride = new Ride("Donostia", "Zarautz", new Date(), 2, 10.0, driver);
        ride.setActive(false);  // Asumiendo que hay un campo `active` para marcar el ride como cancelado

        // Configurar los mocks
        when(db.find(Ride.class, ride.getRideNumber())).thenReturn(ride);
        
        // Invocar el método bajo prueba
        sut.open();
        try {
            sut.cancelRide(ride);
            fail("Debería lanzarse RideAlreadyCanceledException");
        } catch (RideAlreadyCanceledException e) {
            // Comportamiento esperado
        } finally {
            sut.close();
        }
    }
}
