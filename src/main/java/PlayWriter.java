/*
 * PlayWriter.java
 *
 * PLayWriter class.
 * Creates the lovers, and writes the two lover's story (to an output text file).
 */


import java.net.Socket;
import java.net.InetAddress;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;

import javafx.util.Pair;


public class PlayWriter {

    private Romeo  myRomeo  = null;
    private InetAddress RomeoAddress = null;
    private int RomeoPort = 0;
    private Socket RomeoMailbox = null;

    private Juliet myJuliet = null;
    private InetAddress JulietAddress = null;
    private int JulietPort = 0;
    private Socket JulietMailbox = null;

    double[][] theNovel = null;
    int novelLength = 0;

    public PlayWriter()
    {
        novelLength = 500; //Number of verses
        theNovel = new double[novelLength][2];
        theNovel[0][0] = 0;
        theNovel[0][1] = 1;
    }

    //Create the lovers
    public void createCharacters() throws IOException{
        //Create the lovers

        System.out.println("PlayWriter: Romeo enters the stage.");

			//TO BE COMPLETED
        this.myRomeo = new Romeo(0.0);
        myRomeo.start();

        System.out.println("PlayWriter: Juliet enters the stage.");

			//TO BE COMPLETED
        this.myJuliet = new Juliet(1.0);
        myJuliet.start();
    }

    //Meet the lovers and start letter communication
    public void charactersMakeAcquaintances() throws IOException{
            
			//TO BE COMPLETED
        Pair<InetAddress,Integer> pairR = myRomeo.getAcquaintance();
        this.RomeoAddress = pairR.getKey();
        this.RomeoPort = pairR.getValue();


        System.out.println("PlayWriter: I've made acquaintance with Romeo");
            
			//TO BE COMPLETED
        Pair<InetAddress,Integer> pairJ = myJuliet.getAcquaintance();
        this.JulietAddress = pairJ.getKey();
        this.JulietPort = pairJ.getValue();

        System.out.println("PlayWriter: I've made acquaintance with Juliet");
    }


    //Request next verse: Send letters to lovers communicating the partner's love in previous verse
    public void requestVerseFromRomeo(int verse) {
        System.out.println("PlayWriter: Requesting verse " + verse + " from Romeo. -> (" + theNovel[verse - 1][1] + ")");

        //TO BE COMPLETED

        try {
            RomeoMailbox = new Socket(this.RomeoAddress, this.RomeoPort);
            OutputStream RrequestStream = this.RomeoMailbox.getOutputStream();
            OutputStreamWriter RrequestStreamWriter = new OutputStreamWriter(RrequestStream);
            RrequestStreamWriter.write(theNovel[verse-1][1] + "J"); //Add terminator character 'J'
            RrequestStreamWriter.flush(); //Service request sent
        } catch (IOException e) {
            System.out.println("Client: I/O error. " + e);

        }
    }


    //Request next verse: Send letters to lovers communicating the partner's love in previous verse
    public void requestVerseFromJuliet(int verse) {
        System.out.println("PlayWriter: Requesting verse " + verse + " from Juliet. -> (" + theNovel[verse-1][0] + ")");
        //TO BE COMPLETED
        try {
            //System.out.println("Client: Requesting calculator service for command '" + this.userCommand + "'");
            JulietMailbox = new Socket(this.JulietAddress, this.JulietPort);
            OutputStream JrequestStream = this.JulietMailbox.getOutputStream();
            OutputStreamWriter JrequestStreamWriter = new OutputStreamWriter(JrequestStream);
            JrequestStreamWriter.write(this.theNovel[verse-1][0] + "R"); //Add terminator character '#'
            JrequestStreamWriter.flush(); //Service request sent
        } catch (IOException e) {
            System.out.println("Client: I/O error. " + e);

        }
    }


    //Receive letter from Romeo with renovated love for current verse
    public void receiveLetterFromRomeo(int verse) throws IOException{
        //System.out.println("PlayWriter: Receiving letter from Romeo for verse " + verse + ".");

			//TO BE COMPLETED

        try {
            InputStream RoutcomeStream = RomeoMailbox.getInputStream();
            InputStreamReader RoutcomeStreamReader = new InputStreamReader(RoutcomeStream);
            StringBuffer stringBuffer = new StringBuffer();
            char x;
            while (true) //Read until terminator character is found
            {
                x = (char) RoutcomeStreamReader.read();
                if (x == 'R')
                    break;
                stringBuffer.append(x);
            }
            this.theNovel[verse][0] = Double.parseDouble(stringBuffer.toString());
            this.RomeoMailbox.close();
        }catch(IOException e){
            System.out.println("Client: I/O error. " + e);
        }

        System.out.println("PlayWriter: Romeo's verse " + verse + " -> " + theNovel[verse][0]);

    }

    //Receive letter from Juliet with renovated love fro current verse
    public void receiveLetterFromJuliet(int verse) throws IOException{
            
			//TO BE COMPLETED
        try {
            InputStream JoutcomeStream = JulietMailbox.getInputStream();
            InputStreamReader JoutcomeStreamReader = new InputStreamReader(JoutcomeStream);
            StringBuffer stringBuffer = new StringBuffer();
            char x;
            while (true) //Read until terminator character is found
            {
                x = (char) JoutcomeStreamReader.read();
                if (x == 'J')
                    break;
                stringBuffer.append(x);
            }
            this.theNovel[verse][1] = Double.parseDouble(stringBuffer.toString());
            this.JulietMailbox.close();
        }catch(IOException e){
            System.out.println("Client: I/O error. " + e);
        }

        System.out.println("PlayWriter: Juliet's verse " + verse + " -> " + theNovel[verse][1]);

    }



    //Let the story unfold
    public void storyClimax() throws IOException{
        for (int verse = 1; verse < novelLength; verse++) {
            //Write verse
            System.out.println("PlayWriter: Writing verse " + verse + ".");
            this.requestVerseFromRomeo(verse);
			//TO BE COMPLETED
            this.receiveLetterFromRomeo(verse);
            this.requestVerseFromJuliet(verse);
            this.receiveLetterFromJuliet(verse);

            System.out.println("PlayWriter: Verse " + verse + " finished.");
        }
    }

    //Character's death
    public void charactersDeath() throws IOException{
            
			//TO BE COMPLETED
        myRomeo.interrupt();
        myJuliet.interrupt();
			
    }


    //A novel consists of introduction, conflict, climax and denouement
    public void writeNovel() throws IOException{
        System.out.println("PlayWriter: The Most Excellent and Lamentable Tragedy of Romeo and Juliet.");
        System.out.println("PlayWriter: A play in IV acts.");
        //Introduction,
        System.out.println("PlayWriter: Act I. Introduction.");
        this.createCharacters();
        //Conflict
        System.out.println("PlayWriter: Act II. Conflict.");
        this.charactersMakeAcquaintances();
        //Climax
        System.out.println("PlayWriter: Act III. Climax.");
        this.storyClimax();
        //Denouement
        System.out.println("PlayWriter: Act IV. Denouement.");
        this.charactersDeath();

    }


    //Dump novel to file
    public void dumpNovel() {
        FileWriter Fw = null;
        try {
            Fw = new FileWriter("RomeoAndJuliet.csv");
        } catch (IOException e) {
            System.out.println("PlayWriter: Unable to open novel file. " + e);
        }

        System.out.println("PlayWriter: Dumping novel. ");
        StringBuilder sb = new StringBuilder();
        for (int act = 0; act < novelLength; act++) {
            String tmp = theNovel[act][0] + ", " + theNovel[act][1] + "\n";
            sb.append(tmp);
            //System.out.print("PlayWriter [" + act + "]: " + tmp);
        }

        try {
            BufferedWriter br = new BufferedWriter(Fw);
            br.write(sb.toString());
            br.close();
        } catch (Exception e) {
            System.out.println("PlayWriter: Unable to dump novel. " + e);
        }
    }

    public static void main (String[] args) throws IOException{
        PlayWriter Shakespeare = new PlayWriter();
        Shakespeare.writeNovel();
        Shakespeare.dumpNovel();
        System.exit(0);
    }


}
