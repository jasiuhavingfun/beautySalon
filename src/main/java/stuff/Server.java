package stuff;

import java.net.*;
import java.io.*;
import java.util.*;

import entity.*;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class Server {
    public static void main(String[] args) throws IOException {
        // Create a server socket and bind it to a port
        ServerSocket serverSocket = new ServerSocket(5000);

        System.out.println("Server listening on port 5000");
        //Map<Integer, ReservationEntity> reservations = new HashMap<>();

        // Listen for incoming connections
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");

            // Create a new thread for each incoming connection
            new Thread(() -> handleRequest(socket)).start();
        }
    }

    private static void handleRequest(Socket socket) {
        try {

            while(true) {

                SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
                Session session = sessionFactory.openSession();

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                String request = reader.readLine();

                String[] requestParts = request.split("/");
                String action = requestParts[0];
                if (action.equals("if_client_exists"))
                {
                    session.beginTransaction();
                    Query query = session.createQuery("from entity.Clients");
                    List<Clients> customers = query.list();
                    String name = requestParts[1];
                    String surname = requestParts[2];

                    int id = 0;
                    for (Clients customer : customers) {
                        if (customer.getName().equals(name) && customer.getSurname().equals(surname)) {
                            id = customer.getId();
                        }
                    }
                    if (id != 0) writer.println("client_exists/" + id);
                    else writer.println("client_dont_exists");
                }
                else if (action.equals("client_create")) {

                    session.beginTransaction();
                    String name = requestParts[1];
                    String surname = requestParts[2];
                    Clients clients = new Clients();

                    clients.setId(0);
                    clients.setName(name);
                    clients.setSurname(surname);

                    int clientId = (int) session.save(clients);
                    session.getTransaction().commit();
                    session.close();

                    writer.println("ok/"+clientId);
                }
                else if (action.equals("get_all_services"))
                {
                    session.beginTransaction();
                    Query query = session.createQuery("from entity.Services");
                    List<Services> services = query.list();

                    System.out.println(services);

                    for (Services service: services)
                    {
                        writer.println(service.getId() + "/"+ service.getServiceName()+"/"+service.getPrice());
                    }
                    writer.println("end_of_list");
                    session.getTransaction().commit();
                }
                else if (action.equals("get_clients_reservations"))
                {
                    int customerId = Integer.parseInt(requestParts[1]);
                    session.beginTransaction();
                    Query query = session.createQuery("from entity.Reservations where idClient = "+customerId);
                    List<Reservations> reservations = query.list();

                    Query query1 = session.createQuery("from entity.Services");
                    List<Services> services = query1.list();

                    if (reservations.isEmpty())
                    {
                        writer.println("clients_dont_reservations");
                    }
                    else
                    {
                        for (Reservations reservation : reservations)
                        {
                            writer.println("RESERVATION/"+reservation.getId()+"/"+services.get(reservation.getIdService()-1).getServiceName()+"/"+reservation.getDate().toString()+"/"+reservation.getPrice());
                        }
                        writer.println("end_of_list");
                    }


                } else if (action.equals("delete_clients_reservation"))
                {
                    session.beginTransaction();
                    int customerId = Integer.parseInt(requestParts[2]);
                    Query query = session.createQuery("from entity.Reservations where idClient = "+customerId);
                    List<Reservations> reservations = query.list();
                    int reservationId = Integer.parseInt(requestParts[1]);

                    if (reservations.isEmpty()) writer.println("delete_clients_reservation_not_found");

                    else
                    {
                        for (Reservations reservation : reservations)
                        {
                            if (reservation.getId() == reservationId)
                            {
                                session.delete(reservation);
                                session.getTransaction().commit();
                                writer.println("delete_clients_reservation_done");
                                break;
                            }
                        }
                        writer.println("end_of_list");
                    }

                }
                else if (action.equals("make_a_reservation"))
                {
                    int clientId = Integer.parseInt(requestParts[1]);
                    int serviceId = Integer.parseInt(requestParts[2]);
                    int serviceYear = Integer.parseInt(requestParts[3]);
                    int serviceMonth = Integer.parseInt(requestParts[4]);
                    int serviceDay = Integer.parseInt(requestParts[5]);

                    Query query = session.createQuery("from entity.Services");
                    List<Services> servicePrice = query.list();

                    if(serviceMonth==12) serviceMonth=13;
                    java.sql.Date date=new java.sql.Date(serviceYear-1900, serviceMonth-1, serviceDay);
                    session.beginTransaction();

                    System.out.println(clientId);
                    Reservations reservations = new Reservations();
                    reservations.setId(0);
                    reservations.setIdClient(clientId);
                    reservations.setIdService(serviceId);
                    reservations.setDate(date);
                    reservations.setPrice(servicePrice.get(serviceId-1).getPrice());
                    session.save(reservations);

                }
                else {
                    System.out.println("Invalid action: " + action);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}