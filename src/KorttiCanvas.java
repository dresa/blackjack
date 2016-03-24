/*
 * @(#)KorttiCanvas.java 29.4.2003
 *
 * Copyright 2003 Esa Junttila
 */

import java.awt.Canvas;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Image;

 /**
 * Luokan ilmentym‰t edustavat yhdelle pelaajalle tarkoitettua korttien piirtoaluetta.
 * Kun ilmentym‰lle on kiinnitetty jokin tietty Kasi, ilmentym‰ hakee joka piirtokerralla
 * korttien kuvat Kadessa olevien korttien perusteella ja piirt‰‰ ne kaikki piirtoalustalle.
 *
 * @author  Esa Junttila
 * @version 29.4.2003
 */
public class KorttiCanvas extends Canvas{

    /** Taulukko, johon haetaan jokainen Korttipakan Kortin kuvatiedosto */
    private static Image[] kaikkiKuvat;

    /** Apuv‰line kuvatiedostojen hakemiseen levylt‰ */
    private static Toolkit toolkit = Toolkit.getDefaultToolkit();

    /** Kadessa juuri nyt olevien korttien vastaavat kuvatiedostot */
    private Image[] kasikorttikuvat = new Image[Kasi.MAXKORTTEJAKADESSA];

    /** Montako kuvatiedostoa tarvitaan piirt‰m‰‰n Kaden kaikki Kortit*/
    private int montakoKuvaa;

    /**
     * Luo piirtoalustan, jolle ei ole viel‰ m‰‰ritelty Kasi-oliota. Kun
     * konstruktoria kutsutaan ensimm‰isen kerran, taulukoidaan
     * kaikki kuvatiedostot levylt‰ muistiin ennen kuin niit‰ viel‰ tarvitaan.
     * Kasi-olion voi vaihtaa halutessaan vaihdaKasi(Kasi)-metodilla.
     */
    public KorttiCanvas() {
         this.montakoKuvaa = 0;
//         this.setBackground(Color.YELLOW);

         if (kaikkiKuvat == null) //Kortteja ei ole viela ladattu valmiiksi kuvataulukkoon kaikkiKuvat.
              lataaKuvatiedostot();
    }


    /**
     * Piirtoalustan piirtotoiminto piirt‰‰ Korttien kuvat KorttiCanvakselle.
     * Metodia ei kutsuta suoraan, vaan KorttiCanvas-olio p‰ivitet‰‰n
     * kutsumalla sen repaint()-metodia. Kortit piirret‰‰n ensin vierekk‰in.
     * Ellei piirtoalustan leveys riit‰, Kortit asetetaan limitt‰in.
     *
     * @param piirtoalusta  KorttiCanvas-olion piirtokelpoinen alue
     */
    public void paint(Graphics piirtoalusta) {
         piirtoalusta.clearRect( 0, 0, this.getWidth(), this.getHeight() ); // x, y, width, height --> Korttiruudun "nollaus"
         
         //Mahtuvatko kaikki kortit vierekk‰in vai joudutaanko ne piirt‰m‰‰n limitt‰in
         if (this.montakoKuvaa < this.getWidth() / 70) //vierekk‰in
              for (int moneskoKuva=0; moneskoKuva < this.montakoKuvaa; moneskoKuva++) {           
                   piirtoalusta.drawImage(this.kasikorttikuvat[moneskoKuva], 70*moneskoKuva, 0, this); // image, x, y, mihin
              }
         else    //limitt‰in
              for (int moneskoKuva=0; moneskoKuva < this.montakoKuvaa; moneskoKuva++) {           
                   piirtoalusta.drawImage(this.kasikorttikuvat[moneskoKuva],
                               (moneskoKuva)*( (this.getWidth()-70) / (this.montakoKuvaa -1) ), 0, this); // image, x, y, mihin
              }

    }

    /**
     * Vaihtaa piirtoalustan Kasi-olion toiseksi. Kutsutaan aina kun
     * pelaajan Kasi on muuttunut tai vaihtunut. Vastedes KorttiCanvas
     * piirt‰‰ vain t‰m‰n uuden Kaden Kortteja.
     *
     * @param korttikasi   Uusi Kasi, jonka Kortteja halutaan piirrett‰v‰n
     */
    public void vaihdaKasi(Kasi korttikasi) {
         this.haeKuvat(korttikasi); //haetaan uuden Kaden Kortit taulukkoon
    }

    /**
     * Hakee kuvat luokan omasta kuvataulukosta KorttiCanvas-olion omaan
     * kuvataulukkoon. KorttiCanvas-olion kuvataulukko sis‰lt‰‰ aina siis kaikki
     * piirrett‰v‰t kortit
     *
     * @param korttikasi  Mink‰ Kaden kortit liittyv‰t nyt t‰h‰n piirtoalustaan
     */
    private void haeKuvat(Kasi korttikasi) {
         if (korttikasi == null)
              korttikasi = new Kasi( (Korttipakka)null); //Valiaikainen tyhja Kasi

         //Kuvien lataus kasikorttien taulukkoon:
         for (int i=0; i < korttikasi.montakoKorttia(); i++) {
              Kortti yksiKortti = korttikasi.katsoKortti(i+1);

// Kortit ovat taulussa sen mukaan, miten ne esiteltiin luokan Kortti vakioissa SALLITUTMAAT ja SALLITUTARVOT.
// Yhdella SALLITUTMAAT.length-mittaisella hyppayksella siirrytaan seuraavaan MAA-arvoon.
              
              //Monesko haettavan kortin maa on taulussa Kortti.SALLITUTMAAT ?
              int kerroin = 0;
              for (int j=0; j < Kortti.SALLITUTMAAT.length; j++)
                   if (yksiKortti.MAA.equals( Kortti.SALLITUTMAAT[j] )) {
                        kerroin = j;
                        break;
                   }

              //Monesko haettavan kortin arvo on taulussa Kortti.SALLITUTARVOT ?
              int siirto = 0;
              for (int j=0; j < Kortti.SALLITUTARVOT.length; j++)
                   if (yksiKortti.ARVO == Kortti.SALLITUTARVOT[j] ) {
                        siirto = j;
                        break;
                   }                           
              //Haetaan taulusta korttia vastaava kuva.
              this.kasikorttikuvat[i] = KorttiCanvas.kaikkiKuvat[kerroin * Kortti.SALLITUTARVOT.length + siirto];
         }
         this.montakoKuvaa = korttikasi.montakoKorttia();
    }
 

    /**
     * Lataa luokkaan kaikki Korttipakassa olevat korttikuvat. Kuvat ladataan
     * vain kerran - silloin kun KorttiCanvaksen konstruktoria kutsutaan
     * ensimm‰ist‰ kertaa. Kuvatiedostot s‰ilyv‰t taulukoituna KorttiCanvas-olioita
     * varten. Kuvat haetaan Kortti-luokan m‰‰rittelemien Kortti-olioita vastaavista
     * kuvatiedostoista. Kuvat ovat taulukossa Kortti-luokan taulukoiden SALLITUTMAAT
     * ja SALLITUTARVOT mukaisessa loogisessa j‰rjestyksess‰.
     */
    private static void lataaKuvatiedostot() {
         int maita = Kortti.SALLITUTMAAT.length;   // mahdollisten maiden m‰‰r‰
         int arvoja = Kortti.SALLITUTARVOT.length; // mahdollisten arvojen m‰‰r‰

         // viedaan jarjestyksessa hertta1, hertta2, ... hertta13, pata1, pata2, ... ruutu 7, ... risti 13.
         KorttiCanvas.kaikkiKuvat = new Image[maita * arvoja]; // Tilaa kaikille korteille.
         for (int maa=0; maa < maita; maa++) {          // K‰yd‰‰n kaikki maat l‰pi
              for (int arvo=0; arvo < arvoja; arvo++) { // K‰yd‰‰n kaikki arvot l‰pi
                   kaikkiKuvat[arvoja * maa + arvo] = 
                            toolkit.getImage( Kortti.kerroKuvatiedosto(Kortti.SALLITUTMAAT[maa],
                                              Kortti.SALLITUTARVOT[arvo] )                      ); //maa, arvo
              }
         }
    }
}
