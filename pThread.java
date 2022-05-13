import java.util.Arrays;

public class pThread extends Thread{
    private int[] arr;
    private pThread[] peers;
    public int start;
    public int level;
    public static int glob_start = 0;
    public static int glob_level = 1;
    public static boolean first = true;
    public static int count = 0;
    public int id;
    pThread(int[] array, pThread[] array1){
        // initializes variables, and assigns an id to each thread object
        arr = array;
        peers = array1;
        level = 1;
        start = 0;
        count++;
        id = count;
    }
    static synchronized void set(pThread self, int arrlength){
        // checks the position and level of the previously assigned thread and assigns
        // an idle calling thread to the next position
        if (!first) {
            int temp1 = glob_level;
            int temp2 = glob_start + (int)Math.pow(2,glob_level);
            if (temp2 >= arrlength) {
                temp1++;
                temp2 = 0;
            }

            self.level = temp1;
            self.start = temp2;
            glob_level = temp1;
            glob_start = temp2;
        }
        else{
            first = false;

        }
    }

    //the main sorting algorithm
    public void run(){
        while (!Server.done) {

            // Checks to see if there are overlapping threads on a lower level, suspends sorting until threads have been reassigned
            set(this, arr.length);
            int locEnd = start + (int)Math.pow(2,level) - 1;
            boolean ready = false;
            while (!ready) {
                ready = true;
                for (pThread i : peers) {
                    int extStart = i.start;
                    int extEnd = i.start + (int)Math.pow(2,i.level) - 1;
                    if (extStart >= start && extStart <= locEnd) {
                        if (i.level < level) {
                            ready = false;
                        }
                    }
                    if (extEnd >= start && extEnd <= locEnd) {
                        if (i.level < level) {
                            ready = false;
                        }
                    }

                }
            }
            // checks if the sorting is done
            if ( (int)Math.pow(2,level) >= arr.length * 2) {
                Server.done = true;
                break;
            }


            // implementation of  merge sort
            int []temp = new int[ (int)Math.pow(2,level) ];
            int index1 = start;
            int index2 = start + ( (int)Math.pow(2,level) / 2);
            int temp1;
            int temp2;
            for (int i = 0; i < temp.length; i++) {
                if ((index2-start < temp.length) && (index2 < arr.length)) {
                    temp2 = arr[index2];
                }
                else{temp2 = 100;}

                if ((index1-start < (int)Math.pow(2,level) / 2) && (index1 < arr.length)) {
                    temp1 = arr[index1];
                }
                else{temp1 = 100;}

                if (temp2 < temp1) {
                    temp[i] = temp2;
                    index2++;
                } else {
                    temp[i] = temp1;
                    index1++;
                }

            }
            for (int i = 0; i < temp.length; i++){
                try{
                    arr[start + i] = temp[i];
                }
                catch (Exception io){}
            }
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("Thread " + id + " sorts array: " + Arrays.toString(temp));
        }
    }
}
