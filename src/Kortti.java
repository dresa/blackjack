/*
 * @(#)Kortti.java	29.4.2003
 *
 * Copyright 2003 Esa Junttila
 */


/**
 * Luokan <tt>Kortti</tt> ilmentym‰t kuvaavat tavallisen
 * korttipakan kortteja. Kortteihin kelpuutettavat maat ja
 * arvot ovat helposti muutettavissa. Jokaiseen Kortti-olioon
 * liittyy myos kuvatiedosto.
 *
 * @version 29.4.2003
 * @author 	Esa Junttila
 */

public class Kortti{
    
    /** Vain t‰m‰n taulukon sis‰lt‰m‰t String-oliot kelpuutetaan Kortti-olion MAA-vakioksi */
    public static final String[] SALLITUTMAAT = {"hertta", "pata", "ruutu", "risti"};
    
    /** Vain t‰m‰n taulukon sis‰lt‰m‰t arvot kelpuutetaan Kortti-olion ARVO-vakioksi */
    public static final int[] SALLITUTARVOT = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

    /** Kertoo mit‰ p‰‰tett‰ Kortteja vastaavat kuvatiedostot k‰ytt‰v‰t (esim. "gif") */
    private static final String TIEDOSTOPAATE = "gif";


    /** 
     * Sis‰lt‰‰ tiedon Kortti-olion maasta. Kun Kortti on kerran luotu,
     * maata ei voi en‰‰ vaihtaa, mutta sit‰ voi tarkastella vapaasti.
     */
    public final String MAA;

    /** 
     * Sis‰lt‰‰ tiedon Kortti-olion arvosta. Kun Kortti on kerran luotu,
     * arvoa ei voi en‰‰ vaihtaa, mutta sit‰ voi tarkastella vapaasti.
     */
    public final int ARVO;

    /** 
     * Sis‰lt‰‰ tiedon Kortti-olion kuvatiedoston nimest‰. Kun Kortti on kerran luotu,
     * kuvatiedoston nime‰ ei voi en‰‰ vaihtaa, mutta sit‰ voi tarkastella vapaasti.
     */
    public final String KUVATIEDOSTO;


    /**
     * Luo uuden Kortti-olion parametrien pohjalta.
     * Vain luoKortti(<tt>String</tt>, <tt>int</tt>)-metodi voi k‰ytt‰‰ t‰t‰
     * yksityist‰ kontruktoria.
     *
     * @param kortinMaa      Luotavan Kortti-olion MAA
     * @param kortinArvo     Luotavan Kortti-olion ARVO
     */
    private Kortti(String kortinMaa, int kortinArvo) {
         this.MAA = kortinMaa;   //muuttumaton
         this.ARVO = kortinArvo; //muuttumaton
         this.KUVATIEDOSTO = this.MAA + this.ARVO + "." + TIEDOSTOPAATE; //Kuvatiedoston muodostus
    }

    /**
     * Parametrittoman konstruktorin k‰yttˆ on estetty,
     * sill‰ kortissa ilman maata ja arvoa ei ole j‰rke‰.
     */
    private Kortti() { MAA = "virhe"; ARVO = -1; KUVATIEDOSTO = "virhe";} //Kaantaja tahtoo alkuarvot...


    /**
     * Metodin avulla voi luoda varmasti oikeellisia Kortti-ilmentymi‰.
     * Sallitut maat ja arvot on lueteltu julkisissa luokkamuuttujissa. Jos parametrit
     * ovat virheelliset, palautuu <code>null</code>, muuten viite Kortti-olioon.
     *
     * @param   kortinMaa          Kortin maaksi pyrkiv‰ <tt>String</tt>-olio.
     * @param   kortinArvo         Kortin arvoksi pyrkiv‰ <tt>int</tt>-arvo.
     * @return  <tt>Null</tt>, jos ainakin jompi kumpi parametreista oli virheellinen.
     * (parametrien oikeellisuus tarkistetaan luokan Kortti luokkamuuttujista)
     * Jos parametrit olivat kunnossa, palautuu Kortti-olio.
     */
    public static Kortti luoKortti(String kortinMaa, int kortinArvo) {
         int valittuMaa = 0;
         int valittuArvo = 0;

         if (kortinMaa == null)
              return null;

         boolean virheKortissa = true;
         for (int i=0; i<SALLITUTMAAT.length; i++)
              if (kortinMaa.equalsIgnoreCase(SALLITUTMAAT[i])) { //Kortin parametri t‰sm‰‰ jonkin sallitun
                   valittuMaa = i;                     //Stringin kanssa.
                   virheKortissa = false;
                   break;
              }
         if (virheKortissa) 
              return null; // Virheellinen kortin maa

         virheKortissa = true;
         for (int i=0; i<SALLITUTARVOT.length; i++)
              if (kortinArvo == SALLITUTARVOT[i]) { //Kortin parametri t‰sm‰‰ jonkin sallitun
                   valittuArvo = i;                 //int-arvon kanssa.
                   virheKortissa = false;
                   break;
              }
         if (virheKortissa) 
              return null; // Virheellinen kortin arvo.

         // Haetaan lopulliset parametrit luokkamuuttujista.
         return new Kortti(SALLITUTMAAT[valittuMaa], SALLITUTARVOT[valittuArvo]);
    }


    /**
     * Metodi kertoo, mik‰ on parametreja vastaavan Kortti-olion kuvatiedoston nimi.
     * Palautettava String-olio sis‰lt‰‰ koko tiedostonimen p‰‰tteineen.
     *
     * @param   maa          Kiinnostavan Kortin maa.
     * @param   arvo         Kiinnostavan Kortin arvo.
     * @return  <tt>Null</tt>, jos ainakin jompi kumpi parametreista oli virheellinen.
     * (parametrien oikeellisuus tarkistetaan luokan <tt>Kortti</tt> luokkamuuttujista)
     * Jos parametrit olivat kunnossa, palautuu Kortti-olion kuvatiedoston t‰ydellinen
     * nimi p‰‰tteineen String-oliona.
     */
    public static String kerroKuvatiedosto(String maa, int arvo) {
         Kortti kortti = luoKortti(maa, arvo);
         if (kortti == null) //Maa tai arvo oli virheellinen, eika Korttia voitu luoda.
              return null;
         return kortti.KUVATIEDOSTO;
    }


    /**
     * Metodi palauttaa Kortti-olion merkkiesityksen muodossa "ruutu 4".
     *
     * @return  Kortti-olion merkkiesitys String-muotoisena. Esim. "ruutu 4".
     */
    public String toString() { return this.MAA + " " + this.ARVO; }


    /**
     * P‰‰ohjelmametodi testaa luokan toimivuuden lausekattavasti.
     */
    public static void main(String[] args) {
         Kortti testi = luoKortti("jokeri", 1); //virheellinen Kortti
         System.out.println("jokeri 1 -kortti on Kortti-oliona " + testi + "  (Jos null, niin metodi huomasi virheellisen maa-arvon");

         testi = luoKortti("ruutu", 0);         //virheellinen Kortti
         System.out.println("ruutu 0 -kortti on Kortti-oliona " + testi + "  (Jos null, niin metodi huomasi virheellisen arvon");

         testi = luoKortti(null, 0);         //virheellinen Kortti
         System.out.println("<null> 0 -kortti on Kortti-oliona " + testi + "  (Jos null, niin metodi huomasi virheellisen maa-arvon");
         
         testi = luoKortti("ruUTu", 2);         //oikea kortti
         System.out.println("Yritettiin luoda 'ruUTu 2', saatiin " + testi + ", jonka kuvatiedosto on: " + testi.KUVATIEDOSTO);

         System.out.println("Kortin hertta 12 kuvatiedosto on " + Kortti.kerroKuvatiedosto("hertta", 12) );
         System.out.println("Virheellisen kortin <null> 12 kuvatiedosto on " + Kortti.kerroKuvatiedosto(null, 12) );
         System.out.println("Kortin rUuTU 8 kuvatiedosto on " + Kortti.kerroKuvatiedosto("rUuTU", 8) );
         System.out.println("Virheellisen kortin jokeri 1 kuvatiedosto on " + Kortti.kerroKuvatiedosto("jokeri", 1) );
         System.out.println("Virheellisen kortin pata -3 kuvatiedosto on " + Kortti.kerroKuvatiedosto("pata", -3) );
    }
}