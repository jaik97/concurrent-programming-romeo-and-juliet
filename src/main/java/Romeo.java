/*
 * Romeo.java
 *
 * Romeo class.  Implements the Romeo subsystem of the Romeo and Juliet ODE system.
 */


import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetAddress;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import javafx.util.Pair;

public class Romeo extends Thread {

    private ServerSocket ownServerSocket = null; //Romeo's (server) socket
    private Socket serviceMailbox = null; //Romeo's (service) socket

    private int RPort = 0;

    private String RomeoIPAddress = null;
    private double currentLove = 0;
    private double a = 0; //The ODE constant

    private String recieveStr = null;

    private String reply = null;

    private double tmp = 0.0;

    //Class construtor
    public Romeo(double initialLove) throws IOException{
        //Initialize the socket
        RPort = 7778;
        RomeoIPAddress = "127.0.0.1";
        currentLove = initialLove;
        a = 0.02;
        try {
            
			//TO BE COMPLETED
			this.ownServerSocket = new ServerSocket(this.RPort, 5, InetAddress.getByName(RomeoIPAddress));
            //this.start();
            //System.out.println("Server at " + theIPAddress + " is listening on port : " + this.thePort);
            System.out.println("Romeo: What lady is that, which doth enrich the hand\n" +
                    "       Of yonder knight?");
        } catch(Exception e) {
            System.out.println("Romeo: Failed to create own socket " + e);
        }
   }

    //Get acquaintance with lover;
    public Pair<InetAddress,Integer> getAcquaintance() throws IOException{
        System.out.println("Romeo: Did my heart love till now? forswear it, sight! For I ne'er saw true beauty till this night.");
            
			//TO BE COMPLETED
        Pair<InetAddress,Integer> pair = new Pair<>(InetAddress.getByName(RomeoIPAddress), this.RPort);
        return pair;
			
    }


    //Retrieves the lover's love
    public double receiveLoveLetter() throws IOException
    {
            
			//TO BE COMPLETED


        this.recieveStr = "";

        try {

                this.serviceMailbox = this.ownServerSocket.accept();
                InputStream socketStream = this.serviceMailbox.getInputStream();
                InputStreamReader socketReader = new InputStreamReader(socketStream);
                StringBuffer stringBuffer = new StringBuffer();
                char x;
                while (true) //Read until terminator character is found
                {
                    x = (char) socketReader.read();
                    if (x == 'J')
                        break;
                    stringBuffer.append(x);
                }
                this.recieveStr = stringBuffer.toString();

                tmp = Double.parseDouble(this.recieveStr);

        }catch(IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }

        System.out.println("Romeo: O sweet Juliet... (<-" + tmp + ")");

        return tmp;
    }


    //Love (The ODE system)
    //Given the lover's love at time t, estimate the next love value for Romeo
    public double renovateLove(double partnerLove){
        System.out.println("Romeo: But soft, what light through yonder window breaks?\n" +
                "       It is the east, and Juliet is the sun.");
        currentLove = currentLove+(a*partnerLove);
        return currentLove;
    }


    //Communicate love back to playwriter
    public void declareLove(){
            
			//TO BE COMPLETED

        this.reply = "Romeo: I would I were thy bird.";

        try {
            OutputStream outcomeStream = this.serviceMailbox.getOutputStream();
            OutputStreamWriter outcomeStreamWriter = new OutputStreamWriter(outcomeStream);
            outcomeStreamWriter.write(currentLove+"R"); //Wrap and add termination character
            outcomeStreamWriter.flush(); // Return outcome
        }catch (IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        System.out.println("Romeo: I would I were thy bird. (->" + currentLove + "R)");
    }

    //Execution
    public void run () {
        try {
            while (!this.isInterrupted()) {
                //Retrieve lover's current love
                double JulietLove = this.receiveLoveLetter();

                //Estimate new love value
                this.renovateLove(JulietLove);

                //Communicate love back to playwriter
                this.declareLove();
            }
        }catch (Exception e){
            System.out.println("Romeo: " + e);
        }
        if (this.isInterrupted()) {
            System.out.println("Romeo: Here's to my love. O true apothecary,\n" +
                    "Thy drugs are quick. Thus with a kiss I die." );
        }
    }

}
