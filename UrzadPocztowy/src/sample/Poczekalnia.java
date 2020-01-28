package sample;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Poczekalnia {

    private static int maxPoczekalni;
    private int stanPoczekalni = 0;

    private Lock dostep = new ReentrantLock();
    private Object poczekalniaPusta = new Object();
    private Object poczekalniaPelna = new Object();

    public static volatile Integer obsluzono = new Integer(0);
    public static volatile boolean end = false;


    private static ArrayList<Klient> kolejka = new ArrayList<>();

    Poczekalnia(int liczbaMiejscPoczekalnia) {
        maxPoczekalni = liczbaMiejscPoczekalnia;
    }

    public void stanWKolejce(Klient klient) throws InterruptedException {

        dostep.lock();
        while (stanPoczekalni >= maxPoczekalni) {
            dostep.unlock();
            synchronized (poczekalniaPelna) {
                poczekalniaPelna.wait();
            }
            dostep.lock();
        }
        stanPoczekalni++;
        kolejka.add(klient);
        klient.setPozycjawKolejce(kolejka.indexOf(klient));
        synchronized (poczekalniaPusta) {
            poczekalniaPusta.notify();
        }
        Thread.sleep(Klient.move(1300 - klient.getPozycjawKolejce() * 2.5 * klient.getRadius(), 800, klient) / 5);
        dostep.unlock();
    }

    public Klient poczatekObslugi(Okienko okienko) throws InterruptedException {

        Klient obslugiwanyKlient;
        dostep.lock();
        while (czyOdpowiednieOkienko(okienko) == null) {
            dostep.unlock();
            synchronized (poczekalniaPusta) {
                poczekalniaPusta.wait();
            }
            dostep.lock();
        }
        stanPoczekalni--;
        obslugiwanyKlient = czyOdpowiednieOkienko(okienko);
        kolejka.remove(obslugiwanyKlient);
        Klient.move(okienko.getLayoutX() + okienko.getWidth() / 2, okienko.getLayoutY() + okienko.getHeight() + 10, obslugiwanyKlient);
        Thread.sleep(50);
        naprawKolejke();
        synchronized (poczekalniaPelna) {
            poczekalniaPelna.notify();
        }
        dostep.unlock();
        return obslugiwanyKlient;
    }

    public Klient koniecObslugi(Klient obslugiwanyKlient, Okienko okienko) {

        dostep.lock();
        try {
            Thread.sleep(Klient.move(obslugiwanyKlient.getTranslateX(), obslugiwanyKlient.getTranslateY() + 50, obslugiwanyKlient) + 50);
            Klient.move(-20, obslugiwanyKlient.getTranslateY(), obslugiwanyKlient);
            obsluzono = obsluzono + 1;
            Poczta.text.setText("Klienci obsłużeni: " + obsluzono.toString());
            if(obsluzono == Poczta.N){
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dostep.unlock();
        return obslugiwanyKlient;
    }

    static public Klient czyOdpowiednieOkienko(Okienko okienko) {
        if (kolejka.isEmpty())
            return null;
        for (Klient e : kolejka) {
            if (e.interes == okienko.typObslugi || e.interes == 0)
                return e;
        }
        return null;
    }

    public void naprawKolejke() {
        try {
            for (Klient e : kolejka) {
                e.setPozycjawKolejce(kolejka.indexOf(e));
                Thread.sleep(Klient.move(1300 - 2.5 * e.getRadius() * e.getPozycjawKolejce(), 800, e) / 20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}