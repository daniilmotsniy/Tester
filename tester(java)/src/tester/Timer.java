package tester;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Timer {

    public int seconds = 0; // секунды
    boolean f = false; // вкл/выкл секундомера

    public void setLabel_time(Label label_time) {
        this.label_time = label_time;
    }

    private Label label_time;

    public void runTime(){
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
                        label_time.setText("Часу минуло: " + seconds);
                    });
                }
            }
        });
        seconds_thread.start(); // сообственно старт самого потока выше
    }

}
