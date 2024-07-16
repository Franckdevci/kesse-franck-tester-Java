package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import org.apache.logging.log4j.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {
    @Mock
    private DataBaseConfig mockDataBaseConfig;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private Logger mockLogger;

    @Mock
    private Ticket ticket;

    @InjectMocks
    private TicketDAO ticketDAO;

    @BeforeEach
    public void setUpPerTest() {
        try {
            when(mockDataBaseConfig.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Test method to verify the successful saving of a ticket in the database.
     * 
     */
    @Test
    public void saveTicketSuccessTest() {
        try {
            when(mockPreparedStatement.execute()).thenReturn(true);
        } catch (Exception e) {
            System.out.println(e);
        }
        Ticket ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ticket.setParkingSpot(parkingSpot);
        ticket.setInTime(new Date());
        boolean isSaved = ticketDAO.saveTicket(ticket);
        assertFalse(isSaved);
    }

    
    /**
     * Test method to verify the failure of saving a ticket in the database due to
     * an exception.
     * 
     */
    @Test
    public void saveTicketFailureTest() {
        try {
            when(mockPreparedStatement.execute()).thenThrow(new SQLException("Test mock exception to interrupt the save"));
        } catch (Exception e) {
            System.out.println(e);
        }
        Ticket ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        ticket.setParkingSpot(parkingSpot);
        ticket.setInTime(new Date());
        boolean isSaved = ticketDAO.saveTicket(ticket);
        assertFalse(isSaved);
    }
    
    
    @Test
    public void getTicketFailureTest() {
        try {
            when(mockResultSet.next()).thenReturn(false);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        } catch (Exception e) {
            System.out.println(e);
        }
        Ticket ticket = ticketDAO.getTicket("ABCDEF");
        assertNull(ticket);
    }
    
    
    
    @Test
    public void getNbTicketSuccessTest() {
        try {
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(5);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        } catch (Exception e) {
            System.out.println(e);
        }
        int ticketCount = ticketDAO.getNbTicket("ABCD");
        assertEquals(5, ticketCount);
    }
    
    
    @Test
    public void getNbTicketFailureTest() {
        try {
            when(mockResultSet.next()).thenReturn(false);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        } catch (Exception e) {
            System.out.println(e);
        }
        int ticketCount = ticketDAO.getNbTicket("ABCD");
        assertEquals(0, ticketCount);
    }
    
    
    @Test
    public void updateTicketSuccessTest() {
        try {
            when(mockPreparedStatement.execute()).thenReturn(true);
        } catch (Exception e) {
            System.out.println(e);
        }
        Ticket ticket = mock(Ticket.class);
        when(ticket.getId()).thenReturn(123);
        when(ticket.getPrice()).thenReturn(15.02); 
        when(ticket.getOutTime()).thenReturn(new Date()); 
        boolean isUpdated = ticketDAO.updateTicket(ticket);
        assertTrue(isUpdated);
    }
    
    
    @Test
    public void updateTicketFailureTest() {
        try {
            doThrow(new SQLException("Test exception")).when(mockPreparedStatement).execute();
        } catch (Exception e) {
            System.out.println(e);
        }
        Ticket ticket = mock(Ticket.class);
        when(ticket.getId()).thenReturn(33);
        when(ticket.getPrice()).thenReturn(55.45);
        when(ticket.getOutTime()).thenReturn(new Date()); 
        boolean isUpdated = ticketDAO.updateTicket(ticket);
        assertFalse(isUpdated);
    }


    
}