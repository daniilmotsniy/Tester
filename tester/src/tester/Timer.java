package tester;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 *  Tester
 *
 *  Author DMotsniy & Mirniy18
 */

public class Timer {
    //Time measure
    public int seconds = 0;
    public int minutes = 0;
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
                        if(seconds==60){
                            minutes++;
                            seconds = 0;
                        }
                        if (minutes > 0) {
                            label_time.setText("Часу минуло: " + minutes + " хв " + seconds + " с");
                        } else {
                            label_time.setText("Часу минуло: " + seconds + " с");
                        }
                    });
                }
            }
        });
        seconds_thread.start(); //Start main thread
    }

}
