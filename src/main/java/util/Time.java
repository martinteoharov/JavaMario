package util;

public class Time {
    // Static variables are initialized at the program startup.
    public static float timeStarted = System.nanoTime();

    public static float getTime(){
        return (float)((System.nanoTime() - timeStarted) * 1E-9); // return time in seconds
    }


    public Time(){

    }
}
