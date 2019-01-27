package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;


public class Yakit {

    private SimpleLongProperty id;
    private SimpleStringProperty merkez;
    private SimpleStringProperty plaka;
    private SimpleStringProperty tarih;
    private SimpleStringProperty yakittipi;
    private SimpleDoubleProperty yakitmiktari;
    private SimpleDoubleProperty yakitTutari;

    public Yakit() {
    }

    public Yakit(String merkez, String plaka, String tarih, String yakittipi, Double yakitmiktari, Double yakitTutari) {
        this.id = new SimpleLongProperty(0);
        this.merkez = new SimpleStringProperty(merkez);
        this.plaka = new SimpleStringProperty(plaka);
        this.tarih = new SimpleStringProperty(tarih);
        this.yakittipi = new SimpleStringProperty(yakittipi);
        this.yakitmiktari = new SimpleDoubleProperty(yakitmiktari);
        this.yakitTutari = new SimpleDoubleProperty(yakitTutari);
    }
    public Yakit(Long id, String merkez, String plaka, String tarih, String yakittipi, Double yakitmiktari, Double yakitTutari) {
        this.id = new SimpleLongProperty(id);
        this.merkez = new SimpleStringProperty(merkez);
        this.plaka = new SimpleStringProperty(plaka);
        this.tarih = new SimpleStringProperty(tarih);
        this.yakittipi = new SimpleStringProperty(yakittipi);
        this.yakitmiktari = new SimpleDoubleProperty(yakitmiktari);
        this.yakitTutari = new SimpleDoubleProperty(yakitTutari);
    }

    public Long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }



    public String getMerkez() {
        return merkez.get();
    }

    public SimpleStringProperty merkezProperty() {
        return merkez;
    }

    public void setMerkez(String merkez) {
        this.merkez.set(merkez);
    }

    public String getPlaka() {
        return plaka.get();
    }

    public SimpleStringProperty plakaProperty() {
        return plaka;
    }

    public void setPlaka(String plaka) {
        this.plaka.set(plaka);
    }

    public String getTarih() {
        return tarih.get();
    }

    public SimpleStringProperty tarihProperty() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih.set(tarih);
    }

    public String getYakittipi() {
        return yakittipi.get();
    }

    public SimpleStringProperty yakittipiProperty() {
        return yakittipi;
    }

    public void setYakittipi(String yakittipi) {
        this.yakittipi.set(yakittipi);
    }

    public double getYakitmiktari() {
        return yakitmiktari.get();
    }

    public SimpleDoubleProperty yakitmiktariProperty() {
        return yakitmiktari;
    }

    public void setYakitmiktari(double yakitmiktari) {
        this.yakitmiktari.set(yakitmiktari);
    }

    public double getYakitTutari() {
        return yakitTutari.get();
    }

    public SimpleDoubleProperty yakitTutariProperty() {
        return yakitTutari;
    }

    public void setYakitTutari(double yakitTutari) {
        this.yakitTutari.set(yakitTutari);
    }
}
