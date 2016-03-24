/*
 * @(#)Testaakorttipakka.java 29.4.2003
 *
 * Copyright 2003 Esa Junttila
 */




 /**
 * Luokka Testaakorttipakka on muuten l�hes identtinen luokan Korttipakka
 * kanssa, mutta Testaakorttipakka on j�lkimm�isen aliluokka ja mahdollistaa
 * korttipakan "r�pel�imisen" uusien konstruktorien avulla. My�s juuri t�m�n
 * luokan testip��ohjelma puuttuu, sill� peliin kuuluva koodi on luokassa
 * Korttipakka ja siell� oma testimetodinsa.
 *
 * @author  Esa Junttila
 * @version 29.4.2003
 */

public class Testaakorttipakka extends Korttipakka{

    /** Montako pakallista Testaakorttipakka-olioon tulee oletuksena. */
    private final static int PAKKOJEN_OLETUSMAARA = 1;
    
    /** Kuinka perusteellinen pakan sekoitus on? 0 = ei sekoitusta, oletus = 2 */
    private final static int SEKOITUSKERROIN = 2;

    /**
     * Korttipakassa olevien Kortti-olioiden s�ilytysrakenne. Vaikka Kortteja
     * nostettaisiinkin pakasta, alkuper�iset Kortti-oliot pysyv�t silti turvassa
     * t��ll�.
     */
    private Kortti[] korttitaulu;

    /** Viittaa Korttipakan p��llimm�isen Kortti-olion taulukkoindeksiin.*/
    private int pakanPaallimmainen;


//--------------------- R�PEL�INTIKONSTRUKTORIT:
    public Testaakorttipakka(String parametri) {
         this(6);
         if ( parametri.equals("assa") ) {
              //Muunnetaan kaikki pakan kortit pata�ssiksi
              for (int i=0; i < korttitaulu.length; i++)
                   korttitaulu[i] = Kortti.luoKortti("pata", 1);
         }
    }

    public Testaakorttipakka(Kortti[] paallimmaisetKortit) {
         this(6);
              
         //Vaihdetaan pakan p��llimm�iset kortit parametreissa annetuiksi
         //Pakan p��llimm�inen kortti on korttitaulun viimeinen!
         for (int i=0; i < paallimmaisetKortit.length; i++)
              korttitaulu[ (korttitaulu.length -1) - i] = paallimmaisetKortit[i];
    }




//--------------------

    /**
     * Luo Korttipakan, jossa on parametrina saatu m��r� pakallisia. Pakallinen
     * Kortteja muodostuu kaikista mahdollisten maiden ja arvojen yhdistelmist�.
     * Jos parametrina annetaan 0, Korttipakassa on ikuinen m��r� Kortti-olioita.
     * Jos parametrina annetaan < 0, Korttipakassa on oletusm��r� 
     * <tt>Testaakorttipakka.PAKKOJEN_OLETUSMAARA</tt> pakallisia.
     * 
     * @param montakoPakkaa   Montako pakallista kortteja Testaakorttipakka-olioon alunperin
     * asetetaan? Jos 0, k�yt�ss� on ikuinen pakka. Jos < 0, Korttipakassa on oletusm��r�
     * <tt>Testaakorttipakka.PAKKOJEN_OLETUSMAARA pakallisia</tt>.
     */
    public Testaakorttipakka(int montakoPakkaa) {
         this.pakanPaallimmainen = -1;
         this.korttitaulu = new Kortti[0]; 
                  
         if (montakoPakkaa == 0) {      //Tehd��n "ikuinen" pakka.
              this.korttitaulu = null; //Jos korttitaulu == null, k�yt�ss� on ikuinen pakka.
         }
         else{
              if (montakoPakkaa < 0 )   //Korjataan virheellinen parametri oletusarvoksi.
                   montakoPakkaa = PAKKOJEN_OLETUSMAARA;  //Pakkojen maaran oletusarvo
              
              muodostaKorttipakka(montakoPakkaa); //Korttien vienti tauluun ja sekoitus
         }
    }

    /**
     * Luo Korttipakan, jossa on oletusmaara eli
     * <tt>Testaakorttipakka.PAKKOJEN_OLETUSMAARA</tt> pakallista Kortteja.
     */
    public Testaakorttipakka() {
         this(PAKKOJEN_OLETUSMAARA);
    }

    /**
     * Montako Kortti-oliota pakassa on viel� nostettavissa.
     * Ikuinen pakka palauttaa -1.
     *
     * @return   Palautetaan luku, joka ilmaisee montako kertaa
     * Korttipakasta viel� voidaan nostaa sekoittamatta. Jos pakka
     * on ikuinen, palautetaan -1. Tyhj� pakka palauttaa 0.
     */
    public int korttejaPakassa() { 
         if (this.korttitaulu == null)
              return -1;    
         
         return this.pakanPaallimmainen +1;
    }

    /**
     * Kuinka monta Kortti-oliota Korttipakassa oli luontihetkell�. Ikuinen
     * pakka palauttaa -1.
     *
     * @return   Palautetaan se lukum��r�, joka Kortti-olioita Testaakorttipakkaan
     * alun perin vietiinkin. Ikuisen pakan tapauksessa palautuu -1.
     */
    public int maxKorttejaPakassa() { 
         if (this.korttitaulu == null)
              return -1;
         
         return this.korttitaulu.length;
    }


    /**
     * Sekoittaa kaikki Kortti-oliot takaisin Testaakorttipakkaan. Jokainen - my�s nostetut
     * kortit - ovat mukana kun Testaakorttipakka on sekoitettu. Sekoituksen j�lkeen Testaakorttipakka
     * on j�lleen yht� t�ysi kuin luontihetkell�. Jos Korttipakassa oli ikuinen m��r�
     * pakallisia, sekoittaminen ei vaikuta Testaakorttipakkaan mitenk��n.
     */
    public void sekoitaPakka() {
         if (this.korttitaulu == null) //Jos pakka on ikuinen, ei tarvitse tehd� mit��n.
              return;

        //Sekoitus toimii niin, etta k�yd��n koko taulu l�pi SEKOITUSKERROIN kertaa, ja jokaiselle
        //taulun alkiolle arvotaan toinen alkio, jonka kanssa vaihtaa paikkaa. K�ytetyt kortit
        //otetaan mukaan sekoitettuun, t�ysikokoiseen pakkaan.
         Kortti muisti;
         int arvottu;
         for ( int kierros=0; kierros < SEKOITUSKERROIN; kierros++) {
              for (int i=0; i < korttitaulu.length; i++) {
                   arvottu = (int)(Math.random() * korttitaulu.length); //Arvotaan "vaihtokumppani"
                   muisti = korttitaulu[i];
                   korttitaulu[i] = korttitaulu[arvottu];
                   korttitaulu[arvottu] = muisti;
              }
         }
         this.pakanPaallimmainen = korttitaulu.length -1; //Pakan paallimmainen on taulukon viimeinen alkio    
    }

    /**
     * valitsee Korttipakasta p��llimm�isen Kortti-olion ja palauttaa
     * sen kopion. Jos Testaakorttipakka on ikuinen, Kortti-olio arvotaan Kortti-luokassa
     * m��riteltyjen sallittujen maiden ja arvojen perusteella. Jos Testaakorttipakka
     * on tyhj�, palautetaan null.
     *
     * @return      Normaalisti palautetaan Korttipakan p��llimm�isen Kortti-olion
     * kopio. Jos pakka on ikuinen, palautettava Kortti-olio arvotaan Kortti-luokassa
     * m��riteltyjen sallittujen maiden ja arvojen perusteella. Jos Testaakorttipakka
     * on tyhj�, palautetaan <tt>null</tt>.
     */
    public Kortti nostaKortti() {
         if (this.korttitaulu == null) { //Jos pakan koko on ��ret�n, Kortti arvotaan.
              String[] maat = Kortti.SALLITUTMAAT;
              int[] arvot = Kortti.SALLITUTARVOT;
              return Kortti.luoKortti( maat[ (int)(Math.random()*maat.length) ],
                                       arvot[ (int)(Math.random()*arvot.length) ] );
         }

         if (this.pakanPaallimmainen == -1)
              return null;    // Kortit ovat lopussa!

         Kortti annettavaKortti = this.korttitaulu[pakanPaallimmainen];
         this.pakanPaallimmainen--;
 
         //Ei anneta varsinaista Korttia vaan sen kopio
         return Kortti.luoKortti(annettavaKortti.MAA, annettavaKortti.ARVO );
    }


    /**
     * Korttipakan konstruktori kutsuu t�t� metodia kun Testaakorttipakka on valmis muodostettavaksi.
     * Metodi alustaa korttitaulun ja vie kaikki pakalliset Kortteja Testaakorttipakkaan. Metodi
     * kutsuu lopuksi sekoitusmetodia.
     *
     * @param pakallisia    Montako korttipakallista muodostettavaan Testaakorttipakkaan tulee
     */
    private void muodostaKorttipakka(int pakallisia) {
         if (this.korttitaulu == null) {  //  Ikuista pakkaa ei tarvitse muodostaa
              return;
         }

         if (pakallisia < 1) {             // Virheellinen pakallisten m��r�
              System.out.println("VIRHE: Testaakorttipakka.muodostaKorttipakka("+pakallisia+")");
              return;
         }

         this.korttitaulu = new Kortti[ pakallisia *     //Tehd��n korttitaulusta riitt�v�n suuri.
                                        Kortti.SALLITUTMAAT.length *
                                        Kortti.SALLITUTARVOT.length  ];

         //Muodostetaan pakan jokainen kortti loogisella tavalla: Jokaista sallittua
         //maata kohden kaikki mahdolliset arvot. Toistetaan "pakallisia" kertaa.
         String[] maat = Kortti.SALLITUTMAAT;
         int[] arvot = Kortti.SALLITUTARVOT;
         for (int i=0; i < pakallisia; i++)
              for (int j=0; j < maat.length; j++) 
                   for (int k=0; k < arvot.length; k++) 
                        korttitaulu[i * maat.length * arvot.length + j * arvot.length + k] =
                                                                       Kortti.luoKortti(maat[j], arvot[k]);

         this.sekoitaPakka();
         this.pakanPaallimmainen = korttitaulu.length -1; //Pakan paallimmainen on taulukon viimeinen alkio
    }


    /**
     * Kertoo Korttipakan merkkiesitysmuodon. Mukana Korttipakassa nostettavissa olevat
     * Kortti-oliot samassa j�rjestyksess� kuin pakassakin. Mukana p��llimm�isen
     * kortin osoitin ja korttilaskuri. Jos pakka on ikuinen, palautuu "".
     *
     * @param      ��rellisen Korttipakan merkkiesitysmuoto String-oliona. Mukana
     * kaikki nostettavissa olevat kortit siin� j�rjestyksess� kuin ne ovat pakassa.
     * Jos pakka on ikuinen, palautuu "";
     */
    public String toString() {      
         if (this.korttitaulu == null) //Pakka on ikuinen
              return "ikuinen pakka";

         int kulkuri = pakanPaallimmainen;   
         String mjono=" Pakan paalta: (";          
         while (kulkuri != -1) {      //Tulostetaan yksi Kortti-olio kerrallaan.
              mjono = mjono + korttitaulu[kulkuri];
              if (kulkuri != 0)  // ei ole pohjimmainen kortti pakassa,
                   mjono = mjono + ") (";
              kulkuri--;
         }
         return mjono+") \nPakassa on " + this.korttejaPakassa() +
                "/" + this.maxKorttejaPakassa() + "  korttia. Paallimmainen kortti on " +
                      this.korttitaulu[this.pakanPaallimmainen];
    }
}