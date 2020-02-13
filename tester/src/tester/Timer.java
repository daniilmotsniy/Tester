package tester;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Timer {
    //Seconds
    public int seconds = 0;
    //On/off
    boolean f = false;

    //Setter
    public void setLabel_time(Label label_time) {
        this.label_time = label_time;
    }

    private Label label_time;

    //Main function
    public void runTime(){
        Thread seconds_thread = new Thread(new Runnable(){ // creating thread
            public void run(){
                while (f){ //While "On"
                    try {
                        Thread.sleep(1000); //Pause for 1 sec
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(()-> //Platform for thread in JavaFX
                    {
                        seconds++; // Seconds increment
                        System.out.println("Time - " + seconds);
                        label_time.setText("Часу минуло: " + seconds);
                    });
                }
            }
        });
        seconds_thread.start(); //Start main thread
    }

}
