import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Server {
    //records if the sorting is done
    public static boolean done = false;
    public static void main (String[] Args){
        try {
            // instantiates variables
            long time1;
            long time2;
            int []arr;
            pThread[] Threadstore;
            ServerSocket server = new ServerSocket(5051);
            Socket sock = server.accept();
            Scanner scan = new Scanner(new InputStreamReader(sock.getInputStream()));
            PrintWriter print = new PrintWriter(sock.getOutputStream());

            // receives array size and thread size
            int thread_size = Integer.parseInt(scan.next());
            arr = new int[Integer.parseInt(scan.next())];

            //recreates array on the server side
            for (int i = 0; i < arr.length; i++){
                arr[i] = Integer.parseInt(scan.next());
            }

            // creates the pThread objects
            Threadstore = new pThread[thread_size];
            for (int i = 0; i < Threadstore.length; i++){
                Threadstore[i] = new pThread(arr, Threadstore);
            }
            //starts recording time and begins the srt
            time1 = System.currentTimeMillis();
            for (pThread i : Threadstore){
                i.start();
            }

            // checks to see if the sorting is done
            while(!done){
                TimeUnit.MILLISECONDS.sleep(1);
            }
            //stops recording time
            time2 = System.currentTimeMillis() - time1;

            // sends sorted array back to the client
            for (int i : arr) {
                print.print(i + " ");
                print.flush();
            }

            System.out.println(time2);

        }
        catch (Exception io){io.printStackTrace();}
    }
}
