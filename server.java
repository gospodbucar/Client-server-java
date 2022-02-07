import java.io.*;
import java.net.*;
import java.util.*;

public class server {

    public static void main(String[] args) throws Exception {

        //////////////UDP///////

        DatagramSocket udpserverSocket = null; // nov socket 
        double neky=0;      // SPREMENLJIVKA V KATERO SHRANIMO VREDNOST ŠTEVILA VRSTIC, KI JO BOMO POTREBOVALI PRI TCP

        // Najprej ustvarimo Socket UDP
       try {

        int lport =2227; // NASTAVIMO PORT
        udpserverSocket = new DatagramSocket(lport);
        boolean neki = true; // SPREMENLJIVKA ZA ZAUSTAVITEV UDP, KO BOMO KONČALI

        // Streznik poslusa in odgovarja dokler ne spremenimo vrednosti neki
        while ( neki == true) {
        
            // Sprejmemo
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receiveData = new DatagramPacket(
                            receiveBuffer, receiveBuffer.length);
                            
            System.out.println();

            System.out.println("Cakam na paket UDP paket: ");
            System.out.println();

            udpserverSocket.receive(receiveData);
        
            String sentence = new String(receiveData.getData()); // shranimo vrednost, ki nam pošlje strežnik
          
            if(sentence != null){String potrditev = String.valueOf(10); // string potrditev shranimo 10 kot string da server pošlje če je prejel vsebino
            // če dobimo vrednost, ki ni null se shrani kot potrditev 10
        
            // Pošljemo nazaj
            byte[] sendBuffer = new byte[1024];
            int port = receiveData.getPort();
            InetAddress IPAddress = receiveData.getAddress();
            System.out.println("Prejel sem UDP paket z vsebino: " + sentence); // izpiše da je prejel in vsebino
            System.out.println();

            System.out.println("Pošiljam potrditveno sporočilo UDP odjemalcu: "); // izpiše da pošilja potrditveno sporočilo 10
            System.out.println();

            sendBuffer = potrditev.getBytes();//pošlje potrditveno sporočilo v obliki bytov
         
            DatagramPacket sendPacket = new DatagramPacket(
                            sendBuffer, sendBuffer.length, IPAddress, port);
            udpserverSocket.send(sendPacket);

            neky = Double.parseDouble(sentence); // shranimo za pozneje v tcp

            neki=false; // lahko zaključimo izvajanje

            }

        udpserverSocket.close(); //zapremo povezavo
        } 
     
        }
    
        catch (SocketException ex) {
            System.out.println("UDP Port je zaseden.");
            System.exit(1);

        }


        //////////TCP///////////

        
                 
        ServerSocket serverSocket = null; // USTVARIMO SOCKET

        int lport = 2228; // PORT NA KATEREM NAJ SERVER POSLUŠA
      
        // Ustvarimo socket
        try {
            serverSocket = new ServerSocket(lport);
        }

        catch (IOException e) {
        
            System.out.println("Nisem mogel zasesti vrat" + lport);
            System.exit(1);
        }
        
  
           // Sprejmemo odjemalca
           Socket clientSocket = null;
           try {
               clientSocket = serverSocket.accept();


           }
           catch (IOException e) {
               System.out.println("Nisem mogel dobiti povezave");
               System.exit(1);
               if(e != null){ clientSocket = serverSocket.accept();} // ČE JAVI NAPAKO SERVER POIZKUSI ŠE ENKRAT
           }
           
           System.out.println("Povezan, cakam na promet."); // ČE JE POVEZAN IZPIŠE
           System.out.println();

           // ZA VNOS IN POŠILJANJE PODATKOV. I/O TOK

           BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
           PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
           
           String inputLine;
           String stevilo;
           
           // Prebiramo kaj smo dobili od odjemalca
           while ((inputLine = in.readLine()) != null) {
            String potrditev ="11"; // ČE DOBI OD ODJEMALCA ŽELJENO POŠLJE 11 ZA POTRDITEV
            System.out.println("Prejel sem podatke. Stevilo besed v datoteki je " + inputLine);
            System.out.println();

            out.println(potrditev); // ČE DOBI OD ODJEMALCA ŽELJENO POŠLJE 11 ZA POTRDITEV

            out.flush();
            break;
               
           }

           while ((stevilo = in.readLine()) != null) { // SPREJMEMO ŠTEVILO, KI JO JE VNESEL ODJEMALEC
            
            System.out.println("Prejel sem stevilo " + stevilo + ", vnešeno preko tipkovnice");
            System.out.println();
           
            break;
     
           }

           int j = (int)neky; // SPREMENLJIVKA IZ UDP OZ GLOBALNA

           int v=Integer.parseInt(stevilo);  // STRING V INT
           int x=Integer.parseInt(inputLine);// STRING V INT
           
           int vsota = v + x + j ; // SEŠTEJEMO VSOTO VRSTIC BESED IN VNEŠENEGA ŠT
         
           System.out.println("Vsota je " + vsota); // IZPIS VSOTE
           System.out.println();

           String vsot = String.valueOf(vsota); // V STRING
           out.println(vsot); // POŠLJE ODJEMALCU

           // Zapremo "tokove"
           in.close();
           out.close();
           clientSocket.close();
           serverSocket.close();

           // IZHOD IZ PROGRAMA
            System.exit(1);
           
        }
}

    