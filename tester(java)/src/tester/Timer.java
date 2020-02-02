package tester;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Timer {
    @FXML
    private Label label_time;

    public int seconds = 0; // секунды
    static boolean f = false; // вкл/выкл секундомера

    Timer(boolean f){
        this.f = f;
    }

    void runTime(){
        Thread seconds_thread = new Thread(new Runnable(){ // создаем поток
            public void run(){
                while (f){ // пока секундомер ВКЛ, то будем делать следующее
                    try {
                        Thread.sleep(1000); // пауза в 1 секунду
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(()-> // это форма для потока в JavaFX
                    {
                        seconds++; // увеличиваем секунду на 1 (пауза то была)
                        System.out.println("Time - " + seconds);
                        //label_time.setText(String.valueOf(seconds));
                    });
                }
            }
        });
        seconds_thread.start(); // сообственно старт самого потока выше
    }
}
