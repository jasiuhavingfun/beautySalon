package org.example;

import entity.Clients;
import entity.Reservations;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

//        Clients clients = new Clients();
//        clients.setId(0);
//        clients.setName("Jan");
//        clients.setSurname("Ziarkowski");

        java.sql.Date date=new java.sql.Date(2022, 10, 1);

//        Reservations reservations = new Reservations();
//        reservations.setId(0);
//        reservations.setIdService(1);
//        reservations.setIdClient(1);
//        reservations.setDate(date);

        int customerId = 1;
        Query query = session.createQuery("from entity.Reservations where idClient = "+customerId+"");
        List<Reservations> reservations = query.list();
        System.out.println(reservations);
//        session.save(reservations);
//        session.getTransaction().commit();
        session.close();
    }
}