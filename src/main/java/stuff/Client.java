package stuff;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 5000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Podaj imie: ");
        String name = userIn.readLine();
        System.out.print("Podaj nazwisko: ");
        String surname = userIn.readLine();

        int clientId = 0 ;
        writer.println("if_client_exists/" + name + "/" + surname);
        String response = reader.readLine();

        String[] responseParts = response.split("/");
        String action = responseParts[0];

        if (action.equals("client_exists")) {

            clientId = Integer.parseInt(responseParts[1]);
            System.out.println("Klient z ID: " + clientId);

        } else if (action.equals("client_dont_exists")) {

            System.out.println("Taki klient nie istnieje, zakładamy nowe konto");

            writer.println("client_create/" + name + "/" + surname);

            response = reader.readLine();
            responseParts = response.split("/");
            action = responseParts[0];
            clientId = Integer.parseInt(responseParts[1]);
            System.out.println("Twoje ID to " + clientId);
        }

        if (action.equals("ok") || action.equals("client_exists"))
        {

            Scanner scanner = new Scanner(System.in);
            while(true)
            {
                System.out.println("-------------------------------");
                System.out.println("1. Lista wykonywanych zabiegów");
                System.out.println("2. Lista zarezerwowanych zabiegów");
                System.out.println("3. Rezerwacja zabiegu");
                System.out.println("4. Rezygnacja z rezerwacji");
                System.out.println("5. Exit");
                System.out.print("to co? ");

                int choice = scanner.nextInt();

                if (choice == 1)
                {
                    writer.println("get_all_services");

                    String line = reader.readLine();
                    while (!line.equals("end_of_list")) {
                        String[] parts = line.split("/");
                        int serviceId = Integer.parseInt(parts[0]);
                        String serviceName = parts[1];
                        int servicePrice = Integer.parseInt(parts[2]);

                        System.out.println("Nr " + serviceId + " nazwa: " + serviceName + " cena: "+servicePrice);
                        line = reader.readLine();

                    }
                }
                else if(choice == 3)
                {
                    scanner.nextLine();
                    System.out.println("Wprowadż nr zabiegu ktory chcesz zarezerwowac: ");
                    int serviceId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Wprowadż date w formie: rok/miesiac/dzien: ");
                    String serviceDate = scanner.nextLine();

                    writer.println("make_a_reservation/" + clientId + "/"+ serviceId+ "/"+ serviceDate);

                }
                else if (choice == 2)
                {
                    writer.println("get_clients_reservations/"+clientId);
                    //resonse from the server
                    response = reader.readLine();
                    responseParts = response.split("/");

                    if (response.startsWith("clients_dont_reservations"))
                    {
                        System.out.println("Nie masz rezerwacji");
                    }
                    else if (response.startsWith("RESERVATION"))
                    {
                        while (!response.equals("end_of_list"))
                        {
                            int reservationId = Integer.parseInt(responseParts[1]);
                            String reservationName = responseParts[2];
                            String reservationData = responseParts[3];
                            int reservationPrice = Integer.parseInt(responseParts[4]);

                            System.out.println("Rezerwacja ("+ reservationId +") na "+reservationName+" dnia: "+reservationData+" w cenie: "+reservationPrice +"zl");

                            response = reader.readLine();
                            responseParts = response.split("/");
                        }


                    }
                }
                else if (choice == 4)
                {
                    System.out.print("Podaj ID rezerwacji do usuniecia ");
                    int idReservation = scanner.nextInt();

                    writer.println("delete_clients_reservation/"+idReservation+"/"+clientId);
                    response = reader.readLine();
                    if (response.equals("delete_clients_reservation_not_found")) {
                        System.out.println("Nie masz rezerwacji o takim ID");
                    } else if (response.equals("delete_clients_reservation_done")) {
                        System.out.println("Rezerwacja została usunieta");
                    }
                }
                else if(choice == 5)
                {
                    socket.close();
                    break;
                }
            }

        }
        else
        {
            System.out.println("Error");
        }

        //socket.close();


    }
}