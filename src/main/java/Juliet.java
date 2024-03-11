/*
 * Juliet.java
 *
 * Juliet class.  Implements the Juliet subsystem of the Romeo and Juliet ODE system.
 */



import javafx.util.Pair;

import java.lang.Thread;
import java.net.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.*;

import java.util.StringTokenizer;

public class Juliet extends Thread {

    private ServerSocket ownServerSocket = null; //Juliet's (server) socket
    private Socket serviceMailbox = null; //Juliet's (service) socket

    private int JPort = 0;

    private String JulietIPAddress = null;
    private double currentLove = 0;
    private double b = 0;

    private String recieveStr = null;

    private String reply = null;

    private double tmp = 0.0;

    //Class construtor
    public Juliet(double initialLove){
        //Initialize the socket
        JPort = 7779;
        JulietIPAddress = "127.0.0.1";
        currentLove = initialLove;
        b = 0.01;
        try {
			//TO BE COMPLETED

            ownServerSocket = new ServerSocket(this.JPort, 5, InetAddress.getByName(JulietIPAddress));

            //System.out.println("Server at " + theIPAddress + " is listening on port : " + this.thePort);

            System.out.println("Juliet: Good pilgrim, you do wrong your hand too much, ...");
        } catch(Exception e) {
            System.out.println("Juliet: Failed to create own socket " + e);
        }
    }

    //Get acquaintance with lover;
    // Receives lover's socket information and share's own socket
    public Pair<InetAddress,Integer> getAcquaintance() throws IOException{
        System.out.println("Juliet: My bounty is as boundless as the sea,\n" +
                "       My love as deep; the more I give to thee,\n" +
                "       The more I have, for both are infinite.");
            
			//TO BE COMPLETED
            Pair<InetAddress,Integer> pair = new Pair<>(InetAddress.getByName(JulietIPAddress), this.JPort);
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
                while (true)
                {
                    x = (char) socketReader.read();
                    if (x == 'R')
                        break;
                    stringBuffer.append(x);
                }
                this.recieveStr = stringBuffer.toString();

                tmp = Double.parseDouble(this.recieveStr);


        }catch(IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }

        System.out.println("Juliet: Romeo, Romeo! Wherefore art thou Romeo? (<-" + tmp + ")");

        return tmp;
    }


    //Love (The ODE system)
    //Given the lover's love at time t, estimate the next love value for Romeo
    public double renovateLove(double partnerLove){
        System.out.println("Juliet: Come, gentle night, come, loving black-browed night,\n" +
                "       Give me my Romeo, and when I shall die,\n" +
                "       Take him and cut him out in little stars.");
        currentLove = currentLove+(-b*partnerLove);
        return currentLove;
    }


    //Communicate love back to playwriter
    public void declareLove() throws IOException{
            
			//TO BE COMPLETED
        this.reply = "Juliet: Good night, good night!\n Parting is such sweet sorrow,\n That I shall say good night till it be morrow.";


        try {
            OutputStream outcomeStream = this.serviceMailbox.getOutputStream();
            OutputStreamWriter outcomeStreamWriter = new OutputStreamWriter(outcomeStream);
            outcomeStreamWriter.write(currentLove + "J"); //Wrap and add termination character
            outcomeStreamWriter.flush(); // Return outcome
        }catch (IOException e){
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        System.out.println("Juliet: Good night, good night!\n" +
                "Parting is such sweet sorrow,\n That I shall say good night till it be morrow." + " (->" + currentLove + "J)");
    }


    //Execution
    public void run () {
        try {
            while (!this.isInterrupted()) {
                //Retrieve lover's current love
                double RomeoLove = this.receiveLoveLetter();

                //Estimate new love value
                this.renovateLove(RomeoLove);

                //Communicate back to lover, Romeo's love
                this.declareLove();
            }
        }catch (Exception e){
            System.out.println("Juliet: " + e);
        }
        if (this.isInterrupted()) {
            System.out.println("Juliet: I will kiss thy lips.\n" +
                    "Haply some poison yet doth hang on them\n" +
                    "To make me die with a restorative.");
        }

    }

}
