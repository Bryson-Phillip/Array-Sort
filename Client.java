import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.net.Socket;
public class Client {
    public static void main (String[] Args){
        try {
            // Instantiates socket related objects
            InetAddress local = InetAddress.getLocalHost();
            Socket socket = new Socket(local, 5051, local, 5050);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Scanner in = new Scanner(new InputStreamReader(socket.getInputStream()));

            // Creates variables to store the conditions of the array sort
            Random rand = new Random();
            boolean multi;
            int thread_size;
            int array_size;
            int[] arr;

            Scanner scan = new Scanner(System.in);
            System.out.println("Type true to run in multi-thread mode, otherwise type false: ");
            multi = scan.nextBoolean();

            // Receives and sends number of threads to the Server
            if (multi) {
                System.out.println("Thread size: ");
                thread_size = scan.nextInt();
                out.print(thread_size + " ");
                out.flush();
            }
            else{
                out.print(1 + " ");
                out.flush();
            }

            // Receives and sends the size of the array to the server
            System.out.println("Array size: ");
            array_size = scan.nextInt();
            out.print(array_size+" ");
            out.flush();

            // Generates initial values for the array and sends them to the server
            arr = new int[array_size];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = rand.nextInt(100);
                out.print(arr[i]+" ");
                out.flush();
            }
            System.out.println(Arrays.toString(arr));

            // receives the sorted array
            for (int i = 0; i < arr.length; i++) {
                arr[i] = Integer.parseInt(in.next());
            }
            System.out.println(Arrays.toString(arr));



        }
        catch (Exception io){io.printStackTrace();}
    }

}
