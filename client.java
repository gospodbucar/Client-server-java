import java.io.*;
import java.net.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.*;




public class client {

    //METODA ZA VNOS ŠTEVILA IZ TERMINALA
    static int vnesist(){

        Scanner myInput = new Scanner( System.in );
        int a;
 
        System.out.print( "Vnesi celo število " );
        a = myInput.nextInt();
        myInput.close();
        return a;

    }
   
    //METODA, KI SE IZVRŠUJE PREKO UDP KOMUNIKACIJE
    static void UDP(int port) throws java.io.IOException{

                // Povežemo se na strežnik
                String server = new String("localhost");
        
        
                // odpremo datoteko za shranjevanje
                PrintWriter outputStream = null;
                outputStream = new PrintWriter(new FileWriter("vsebina.txt"));
                
                try {
                    //UDP vtičnica povezava
                    DatagramSocket clientSocket = new DatagramSocket();

                    
        
                    //ustvarimo URL objekt
                    URL fis = new URL("https://www.fis.unm.si/");
                
                    //ustvarimo bralca z medpomnilikom, ki bere iz URLja s pomočjo bralca vhodnega toka
                    BufferedReader in = new BufferedReader(
                    new InputStreamReader(fis.openStream()));
        
        
                    //beremo iz toka vse dokler je kaj za prebrati
                    String inputLine; //vrstice
                    int stVrstic = 0; //stevilo vrstic
                    while ((inputLine = in.readLine()) != null) {
                        outputStream.println(inputLine); //zapis v datoteko
                        stVrstic++; //vsako vrstico se poveča za 1
                    }

                    System.out.println();
                    //izpis v terminal
                    System.out.println("Povezal sem se na URL naslov, prebral sem vsebino in jo shranil v datoteko. Stevilo vrstic je: " + stVrstic);
                    System.out.println();
        
                    //IP destinacija
                    InetAddress IPAddress = InetAddress.getByName(server);

                    // Posljemo
                    System.out.println("Pošiljam podatke UDP strežniku ter čakam na odgovor.");
                    System.out.println();
        
                    byte[] sendBuffer = new byte[1024];
                    String sporocilo = String.valueOf(stVrstic); //Int v String
                    sendBuffer = sporocilo.getBytes(); // Podatki se morajo spremeniti v byte za prenos
                    DatagramPacket sendPacket = new DatagramPacket(
                                    sendBuffer, sendBuffer.length, IPAddress, port);
                    clientSocket.send(sendPacket);
                    clientSocket.setSoTimeout(10000); // Nastavimo timeout na 10 sekund
                                
                    // Sprejmemo
                    byte[] receiveBuffer = new byte[1024];
                    DatagramPacket receivedPacket = new DatagramPacket(
                                    receiveBuffer, receiveBuffer.length);
                  
                    try {
                        clientSocket.receive(receivedPacket); //za sprejem od strežnika
                        String sprejeto = new String(receivedPacket.getData()); //shranimo v spremenljivko sprejeto
                        System.out.println("Nazaj sem dobil: " + sprejeto); // izpis kaj smo dobili nazaj od serverja
                        System.out.println();

                        if(sprejeto.trim().equals("10")){ //če nam server vrne 10 dobimo pritrdilni odgovor in izpišemo spodnji stavek
                        //izpis
                        System.out.println("Prejel sem potrdilno sporočilo in s tem UDP odjemalec zaključuje z delovanjem.");}
                        System.out.println();
        
                    }
                
                    catch (SocketTimeoutException ste) {
                        System.out.println("Potekel je zahtevek za sprejem");
                    }
                    //na koncu obvezno zapremo povezave in datoteke
                    outputStream.close();
                    in.close();
                    clientSocket.disconnect();
                    clientSocket.close();
                    
                }
                   
                catch (UnknownHostException ex) {
                    System.out.println("Naslova ne morem razrešiti na IP");
                }
                catch (IOException ex) {
                    System.out.println(ex);
                }                 
    }
    //METODA, KI SE IZVRŠUJE PREKO TCP KOMUNIKACIJE
    public static void TCP(int port) throws IOException{

        Socket clientSocket = null; //USTVARIMO CLIENT SOCKET ZA POVEZAVO
        String server = new String("localhost"); // IME STREŽNIKA
       
            try {
                clientSocket = new Socket(server, port); //POVEZAVA Z STREŽNIKOM NA PORTU KI GA DODELIMO
            }

            catch (UnknownHostException e) {
                System.out.println("Ne morem razresiti domene");
                System.exit(1);
            }
            catch (IOException e) {

                if(e != null){  //ČE PRIDE DO NAPAKE SE ŠE ENKRAT IZVRŠI TCP REKRUZIVNO
                    TCP(2228);
                }
                System.out.println("Ne morem se povezati na streznik");
                System.exit(1);
                
            }
            
            // Za komunikacijo s streznikom
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            Scanner s = null;
            int stevc = 0;  // ŠTEVEC ŠTEVILA BESED V XANADU.TXT

            try {
                s = new Scanner(new BufferedReader(new FileReader("xanadu.txt")));

                while (s.hasNext()) {
                    s.next();
                    stevc++;
                }

                System.out.println("Iz datoteke smo prebrali " + stevc + " besed");
                System.out.println();
                
                //POŠLJEMO STREŽNIKU
                out.println(stevc);

            }

            finally {
                if (s != null) {
                    s.close();
                    s = null;
                }
            }

            String inputLine = "";  // SPREMENLJIVKA V KATERO PREBEREMO KAR NAM POŠLJE STREŽNIK
                while ((inputLine = in.readLine()) != null) {
            String potrditev ="11";  // SPREMENLJIVKA ZA POTRDITEV OD SERVERJA
                if(inputLine.trim().equals(potrditev)){System.out.println("Prejel sem potrditveno sporočilo"); // ČE NAM SERVER POŠLJE 11 KOT POTRDITEV PROGRAM IZPIŠE SPOROČILO
                System.out.println();
            }
                break;
            }

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); //ODPREMO NOV TOK ZA POŠILJANJE

            out.flush();
            int a = vnesist();  //FUNKCIJA ZA VNOS IZ TERMINALA
            System.out.println();

            System.out.println("Vneseno število je " + a); // IZPIS VNEŠENEGA SPOROČILA
            System.out.println();

            String nekii = String.valueOf(a);  //SPREMENIMO INTEGER V STRING
       
            out.println(nekii); //POŠLJEMO STREŽNIKU

            String konc = "";  // STRING V KATEREGA SHRANIMO SEŠTEVEK, KI GA POŠLJE SERVER
            while ((konc = in.readLine()) != null) {
                System.out.println("Sestevek, ki ga je podal streznik, je " + konc);
                System.out.println();

                // NA KONCU ZAPREMO PROGRAM
                System.exit(0);
            }

        // ZAPREMO VSE POVEZAVE IN DATOTEKE
        in.close();
        out.close();
		clientSocket.close();
        stdIn.close();


        }
  
    public static void main(String[] args) throws Exception {
    
    // NAJPREJ KLIČEM FUNKCIJO UDP NA PORTU 2227
    UDP(2227);

    // NA KONCU PA FUNKCIJO TCP NA PORTU 2228
    TCP(2228);
    
    
        
        

        
        
  
        
        
    }

}