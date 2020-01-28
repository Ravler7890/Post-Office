package sample;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import java.util.Random;

public class Klient extends Circle implements Runnable {

    //interes; 0 - przesylka listowa; 1 - paczka; 2 - przekaz pieniezny;

    static double speed = 2;
    int interes;
    private Poczekalnia poczekalnia;
    private int pozycjawKolejce;
    static private Random interesRandom = new Random();

    Klient(Poczekalnia poczekalnia) {

        setTranslateX(-20);
        setTranslateY(800);
        setRadius(20);
        this.poczekalnia = poczekalnia;
        interes = interesRandom.nextInt(3);

        switch (interes) {
            case 0:
                setFill(Color.BLACK);
                break;
            case 1:
                setFill(Color.RED);
                break;
            case 2:
                setFill(Color.GREEN);
                break;
        }

    }

    @Override
    public void run(){

        try {
            Thread.sleep(50);
            poczekalnia.stanWKolejce(this);

        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void setPozycjawKolejce(int pozycjawKolejce) {
        this.pozycjawKolejce = pozycjawKolejce;
    }

    public int getPozycjawKolejce() {
        return pozycjawKolejce;
    }

    public static long move(double toX, double toY, Klient klient){

        double dlugoscWektora = Math.sqrt(Math.pow((toX - klient.getTranslateX()), 2) + Math.pow((toY - klient.getTranslateY()), 2));
        Line path = new Line(klient.getTranslateX(), klient.getTranslateY(), toX, toY);
        PathTransition animacja = new PathTransition();
        animacja.setDuration(Duration.millis(speed*dlugoscWektora));
        animacja.setNode(klient);
        animacja.setPath(path);
        Platform.runLater(animacja::play);

        return (long)(2*dlugoscWektora);
    }
}
