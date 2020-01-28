package sample;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
public class Poczta implements Initializable {

    @FXML
    private Pane mainPane;
    @FXML
    private ChoiceBox<Integer> liczbaMiejscPoczekalnia;
    @FXML
    private ChoiceBox<Integer> liczbaOkienek;
    @FXML
    private ChoiceBox<Integer> liczbaKlientow;

    public static Text text = new Text();

    static int M; //liczba okienek
    static int N; //liczba klientow ktorzy w sumie maja odwiedzic poczte
    static int O;

    static Klient[] klient;
    static Okienko[] okienko;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        for(int i = 10; i <= 200; i = i + 10){
            liczbaKlientow.getItems().add(i);
        }
        liczbaKlientow.setValue(40);

        for(int i = 1; i <= 10; i++){
            liczbaOkienek.getItems().add(i);
        }
        liczbaOkienek.setValue(5);

        for(int i = 1; i <= 25; i++){
            liczbaMiejscPoczekalnia.getItems().add(i);
        }
        liczbaMiejscPoczekalnia.setValue(10);

    }

    //metode wywoluje ten maly przycisk start w interfejsie

    public void start() throws InterruptedException{

        mainPane.getChildren().add(text);



        N = liczbaKlientow.getValue();
        M = liczbaOkienek.getValue();
        O = liczbaMiejscPoczekalnia.getValue();

        Poczekalnia poczekalnia = new Poczekalnia(O);
        text.setText("Klienci obsłużeni: " + Poczekalnia.obsluzono.toString());
        text.setX(670);
        text.setY(250);
        text.setScaleX(2);

        klient = new Klient[N];
        okienko = new Okienko[M];


        for(int i = 0; i < N; i++) {
            mainPane.getChildren().add(klient[i] = new Klient(poczekalnia));
            Thread thread = new Thread(klient[i]);
            thread.setName("interesant " + i);
            thread.start();
        }

        for(int i = 0; i < M; i++){
            okienko[i] = new Okienko(i, poczekalnia);
            mainPane.getChildren().add(okienko[i].ramka);
            mainPane.getChildren().add(okienko[i]);
            Thread thread = new Thread(okienko[i]);
            thread.setName("okienko " + i);
            thread.start();
        }

        System.out.println("Wątki postawione");
    }

    public void reset() throws Exception {

        Poczekalnia.end = true;
        Thread.sleep(1000);
        mainPane.getChildren().removeAll(okienko);
        for (Okienko o: okienko
             ) { mainPane.getChildren().removeAll(o.ramka);

        }
        mainPane.getChildren().removeAll(klient);
        okienko = null;
        klient = null;

    }
}
