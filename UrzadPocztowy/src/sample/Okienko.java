package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;
import java.util.concurrent.Semaphore;


public class Okienko extends Rectangle implements Runnable {


    private Poczekalnia poczekalnia;
    public int typObslugi;
    public int numerOkienka;
    static private Random randomTypObslugi = new Random();
    Semaphore wolne = new Semaphore(0);
    Rectangle ramka;

    Okienko(int numerOkienka, Poczekalnia poczekalnia){

        this.poczekalnia = poczekalnia;
        ramka = new Rectangle();
        this.numerOkienka = numerOkienka;
        typObslugi = (randomTypObslugi.nextInt(100))%2 + 1;
        wolne.release();
        setWidth(80);
        setHeight(50);

        ramka.setHeight(getHeight() + getHeight()/4);
        ramka.setWidth(getWidth() + getHeight()/4);
        ramka.setFill(Color.BLACK);

        switch(this.typObslugi){
            case 1:
                setFill(Color.RED);
                break;
            case 2:
                setFill(Color.GREEN);
                break;
        }

        if(numerOkienka == 0){
            setLayoutX(1200);
            setLayoutY(300);
        }
        else
        {
            setLayoutX(Poczta.okienko[numerOkienka - 1].getLayoutX() - 1.5 * getWidth());
            setLayoutY(Poczta.okienko[numerOkienka - 1].getLayoutY());
        }

        ramka.setLayoutX(getLayoutX() - getHeight()/8);
        ramka.setLayoutY(getLayoutY() - getHeight()/8);
    }

    @Override
    public void run() {

        Klient obslugiwanyKlient;

        while(true){
            try{

                Thread.sleep(100);
                obslugiwanyKlient = poczekalnia.poczatekObslugi(this);
                Thread.sleep(new Random().nextInt(4000) + 2000);
                poczekalnia.koniecObslugi(obslugiwanyKlient, this);

            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
