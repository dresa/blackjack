/*
 * @(#)KorttiGUI.java 29.4.2003
 *
 * Copyright 2003 Esa Junttila
 */

import java.awt.*;

 /**
 * Luokan ilmentym‰ edustaa Blackjack-pelin k‰yttˆliittym‰n vasenta puolta. Luokka
 * antaa vain tulostietoja eik‰ vaikuta itse pelin kulkuun mill‰‰n lailla. KorttiGUI
 * on vain Blackjack-luokan apulainen. Luokka toteuttaa Korttien, pistelukujen ja
 * inforuudun tulostamisen halutulla hetkell‰.
 *
 * @author  Esa Junttila
 * @version 29.4.2003
 */
public class KorttiGUI extends Panel {

    /** Korttiruutujen korkeus m‰‰r‰ytyy t‰m‰n korkeuksisen TextArea-olion avulla */
    private static final int KORTTIRUUDUN_KORKEUS = 6;

    /** Korttiruutujen leveys m‰‰r‰ytyy t‰m‰n leveyksisen TextArea-olion avulla */
    private static final int KORTTIRUUDUN_LEVEYS = 36;

//------- VARIT:
    /** Mink‰ v‰rinen on K‰yttˆliittym‰n vasen puoli */
    private static final Color IKKUNAN_TAUSTAVARI = new Color(0, 128, 0); //tummanvihrea
    
    /** Mill‰ v‰rill‰ h‰vinnyt osapuoli v‰rj‰t‰‰n */
    private static final Color TAPPIOVARI = new Color(255, 100, 100);     //melko vahva punainen
    
    /** Mill‰ v‰rill‰ voittanut osapuoli v‰rj‰t‰‰n */
    private static final Color VOITTOVARI = new Color(0, 255, 0);         //kirkkaanvihrea
    
    /** Mill‰ v‰rill‰ Blackjackin saanut osapuoli v‰rj‰t‰‰n */
    private static final Color BLACKJACKVARI = new Color(255, 255, 0);    //keltainen

    /** Millainen on editoitavan ponosruudun v‰ri */
    private static final Color PANOSRUUDUN_VARI = Color.WHITE;
    
    /** Mik‰ on tekstien oletusv‰ri */
    private static final Color TEKSTIVARI = Color.BLACK;

//------- FONTIT:
    /** Mill‰ fontilla pisteluvut merkit‰‰n */
    private static final Font PISTERUUDUN_FONTTI = new Font("SansSerif", Font.BOLD, 26);
    
    /** Mill‰ fontilla pistelukujen yll‰ olevat otsakkeet merkit‰‰n */
    private static final Font OTSAKEFONTTI = new Font("SansSerif", Font.PLAIN, 14);


//------- MUUTTUJAT:
    /** Mink‰ pelikierroksen kulkua KorttiGUIn ilmentym‰ seuraa */
    private Pelikierros peli;

//------- GRAAFISEN KAYTTOLIITTYMAN KOMPONENTIT:
    /** K‰yttˆliittym‰n korttisarake */
    private Panel vasen;
    
    /** K‰yttˆliittym‰n pistelukusarake */
    private Panel oikea;
  
  //-------
    /** Jakajan ja molempien pelaajien Korttien piirtoalueet*/
    private KorttiCanvas jakaja    = new KorttiCanvas();
    private KorttiCanvas pelaaja_1 = new KorttiCanvas();
    private KorttiCanvas pelaaja_2 = new KorttiCanvas();

    /** Inforuutu, johon tulostuu ohjelman ilmoituksia */
    private TextArea inforuutu     = new TextArea("", KORTTIRUUDUN_KORKEUS, KORTTIRUUDUN_LEVEYS, TextArea.SCROLLBARS_NONE);
  //-------    
    /** Jakajan ja molempien pelaajien Pistelukujen alueet*/
    private Panel jakajanPistealue;
    private Panel pelaajanPistealue_1;
    private Panel pelaajanPistealue_2;
    private Panel valepistealue;
  
    /** Pistelukualueiden otsakkeet*/
    private Label jakajanOtsake = new Label("Jakaja:");
    private Label pelaajanOtsake_1 = new Label("Pelaaja:");
    private Label pelaajanOtsake_2 = new Label("Pelaaja:");
  
    /** Pistelukualueiden pistelukuruudut, eli miss‰ luvut tulostuvat */
    private Label jakajanPisteet    = new Label("");
    private Label pelaajanPisteet_1 = new Label("");
    private Label pelaajanPisteet_2 = new Label("");
    private TextField valepisteruutu = new TextField(""); //mukana luomassa tyhj‰‰ tilaa ruudulle


    /** Ei kutsuta koskaan */
    private KorttiGUI() {}

    /**
     * Luo k‰yttˆliittym‰n vasemman puolen, joka seuraa parametrina saadun
     * Pelikierroksen kulkua. P‰ivitt‰‰ itsens‰ automaattisesti vain sen kerran
     * kun olio luodaan. Parametrina voi antaa myˆs null-arvon
     *  - silloin p‰ivitysoperaatiot eiv‰t tee mit‰‰n.
     *
     * @param tamaPeli   Pelikierros, jonka kulkua halutaan seurata. 
     */
    public KorttiGUI(Pelikierros tamaPeli) {
         this.peli = tamaPeli;

         this.setLayout(new BorderLayout(5,0) );  //hgap,vgap
         this.setBackground(IKKUNAN_TAUSTAVARI);
       //-------
         this.vasen = new Panel( new GridLayout(4,1,0,5) ); // Korttisarake (rows,cols,hgap,vgap)
         this.oikea = new Panel( new GridLayout(4,1,0,40) ); // Pistelukusarake

       //-- Aluksi vain jakajan ja 1. pelaajan Korttiruudut ovat n‰kyvill‰ 
         this.jakaja.setVisible(true);
         this.pelaaja_1.setVisible(true);
         this.pelaaja_2.setVisible(false);
         this.inforuutu.setEditable(false);
         this.inforuutu.setVisible(false);

         this.vasen.add(this.jakaja);
         this.vasen.add(this.pelaaja_1);
         this.vasen.add(this.pelaaja_2);
         this.vasen.add(this.inforuutu);
//-------
       //-- Jokainen n‰ist‰ sis‰lt‰‰ tilan otsakkeelle (Label) ja pisteluvulle (Label)
         this.jakajanPistealue    = new Panel(new GridLayout(2,1,0,0));
         this.pelaajanPistealue_1 = new Panel(new GridLayout(2,1,0,0));
         this.pelaajanPistealue_2 = new Panel(new GridLayout(2,1,0,0));
         this.valepistealue       = new Panel(new GridLayout(2,1,0,0));

         this.jakajanPistealue.add(this.jakajanOtsake);
         this.jakajanPistealue.add(this.jakajanPisteet);
         this.pelaajanPistealue_1.add(this.pelaajanOtsake_1);
         this.pelaajanPistealue_1.add(this.pelaajanPisteet_1);
         this.pelaajanPistealue_2.add(this.pelaajanOtsake_2);
         this.pelaajanPistealue_2.add(this.pelaajanPisteet_2);
         this.valepistealue.add(this.valepisteruutu);

         //-- Otsakkeiden fontin kiinnitys
         this.jakajanOtsake.setFont(OTSAKEFONTTI);
         this.pelaajanOtsake_1.setFont(OTSAKEFONTTI);
         this.pelaajanOtsake_2.setFont(OTSAKEFONTTI);

         this.oikea.add(this.jakajanPistealue);
         this.oikea.add(this.pelaajanPistealue_1);
         this.oikea.add(this.pelaajanPistealue_2);
         this.oikea.add(this.valepistealue);

         this.jakajanPisteet.setFont(PISTERUUDUN_FONTTI);
         this.jakajanPisteet.setForeground(TEKSTIVARI);

         this.pelaajanPisteet_1.setFont(PISTERUUDUN_FONTTI);
         this.pelaajanPisteet_1.setForeground(TEKSTIVARI);

         this.pelaajanPisteet_2.setVisible(true); //Piilotus hoidetaan pelaajanPistealue_2:n avulla!
         this.pelaajanPisteet_2.setFont(PISTERUUDUN_FONTTI);
         this.pelaajanPisteet_2.setForeground(TEKSTIVARI);

         this.valepisteruutu.setVisible(false); // Valeruudun on syyt‰ olla piilossa
       
       //-- Korttisarake ja Pistelukusarake liimataan toisiinsa kiinni
         this.add(this.vasen, BorderLayout.WEST); 
         this.add(this.oikea, BorderLayout.EAST);

       //-- Automaattinen p‰ivitys iloiseksi lopuksi
         this.paivitaKortit();
    }


    /**
     * Vaihtaa seurattavaa Pelikierrosta. Vastedes jokainen p‰ivitys koskee t‰m‰n
     * uutden Pelikierroksen tilannetta. Jokaisen vaihdon yhteydessa p‰ivitet‰‰n ikkuna
     * automaattisesti vastaamaan uuden Pelikierroksen tilannetta.
     *
     * @param uusiPeli  Pelikierros, jota t‰st‰ l‰htien aletaan seurata.
     */
    public void vaihdaPelia(Pelikierros uusiPeli) {
         this.peli = uusiPeli;
         this.paivitaKortit();
    }

    /**
     * P‰ivitt‰‰ elementtins‰ vastaamaan Pelikierroksen tilannetta. Metodia on
     * syyt‰ kutsua joka kerta kun seurattava Pelikierros muuttuu jollakin tavalla
     * (esim. pelaajalle lis‰‰ kortteja).
     */
    public void paivitaKortit() {
         if (this.peli == null)
             return;

         Kasi[] korttitaulukko = this.peli.annaKaikkiKadet();

         // Vaihdetaan Kasiin nyt pelissa olevat kortit
         Kasi jakajanKasi = korttitaulukko[0];
         Kasi pelaajanKasi_1 = korttitaulukko[1];         
         Kasi pelaajanKasi_2 = korttitaulukko[2];         

         // Paivitetaan piirtoalustan vastaava Kasi. (pelaajan 2 Kasi vaihdetaan myˆhemmin)
         this.jakaja.vaihdaKasi(jakajanKasi);
         this.pelaaja_1.vaihdaKasi(pelaajanKasi_1);

         // Asetetaan Kasien pisteluku (pelaajan 2 pisteluku vaihdetaan myˆhemmin)
         this.jakajanPisteet.setText( "" + jakajanKasi.kadenArvo() );
         this.pelaajanPisteet_1.setText( "" + pelaajanKasi_1.kadenArvo() );

  
         // Onko pelaaja 2 nakyvissa ja mit‰ tulostetaan
         if (this.peli.kerroErikoistoiminto() == 2) { // PELI ON JAETTU
              this.pelaaja_2.vaihdaKasi(pelaajanKasi_2); //p‰ivitet‰‰n pelaajan 2 Kasi
              this.pelaaja_2.setVisible(true); // korttiruutu n‰kyviin
              this.pelaajanPisteet_2.setText( "" + pelaajanKasi_2.kadenArvo() ); //pisteluvun p‰ivitys

              this.pelaajanPistealue_2.setVisible(true); // pelaaja 2 n‰kyviin
         }
         else {                                       // PELIƒ EI OLE JAETTU
              this.pelaajanPistealue_2.setVisible(false); //piilotetaan pelaajan 2 ruudut
              this.pelaaja_2.vaihdaKasi(null);
              this.pelaajanPisteet_2.setText("");
              this.pelaaja_2.setVisible(false);
         }

         // Jos pelikierros on paattynyt ja pelia ei juuri kaynnistetty, varjataan
         // voittaja ja haviaja. Muuten nollataan varjays.
         // (Kun peli on juuri k‰ynnistetty, niin erikoistoiminto == -1)
         if (this.peli.kenenVuoro() == -1 && this.peli.kerroErikoistoiminto() != -1)
              this.voittajanVarjays(); //erillinen metodi
         else {
              this.jakajanPisteet.setForeground(TEKSTIVARI);
              this.pelaajanPisteet_1.setForeground(TEKSTIVARI);
              this.pelaajanPisteet_2.setForeground(TEKSTIVARI);
         }

         this.merkitseKenenVuoro(); //erillinen metodi

         // Piirretaan pelaajan kortit
         this.jakaja.repaint();
         this.pelaaja_1.repaint();
         this.pelaaja_2.repaint();

         this.validate();
    }


    /**
     * Metodi tulostaa inforuudussa parametrina saadun String-olion.
     * Tarkoitettu l‰hinn‰ virheilmoituksiin. Samalla metodilla m‰‰ritell‰‰n
     * myˆs inforuudun n‰kyvyys (normaalitilanteessa piilossa)
     *
     * @param teksti  Mit‰ teksti‰ inforuudulle halutaan
     * @param onkoNakyvilla  Asetetaanko inforuutu esille vai piilotetaanko se
     */
    public void muutaInforuutua(String teksti, boolean onkoNakyvilla) {
         this.inforuutu.setText(teksti);
         this.inforuutu.setVisible(onkoNakyvilla);
         this.validate();
    }

    /**
     * V‰rj‰‰ vuorossa olevan pelaajan pisteruudun otsakkeen. Otsake hehkuu
     * v‰ri‰ vain silloin kun on ko. osapuolen vuoro.
     */
    private void merkitseKenenVuoro() {
         if (this.peli == null)
              return;

         // Asetetaan merkki aktiivisena olevalle pelaajalle otsakeruutuun
         if (this.peli.kenenVuoro() == 1 || this.peli.kenenVuoro() == 2) //Pelaajan 1 vuoro
              this.pelaajanOtsake_1.setForeground(VOITTOVARI);
         else this.pelaajanOtsake_1.setForeground(TEKSTIVARI);

         if (this.peli.kenenVuoro() == 3) //Pelaajan 2 vuoro
              this.pelaajanOtsake_2.setForeground(VOITTOVARI);
         else this.pelaajanOtsake_2.setForeground(TEKSTIVARI);
         
         if (this.peli.kenenVuoro() == 4) //Jakajan vuoro
              this.jakajanOtsake.setForeground(VOITTOVARI);
         else this.jakajanOtsake.setForeground(TEKSTIVARI);
    }

    /**
     * Kun pelikierros on lopussa, t‰m‰ metodi v‰rj‰‰ voittajan ja
     * h‰vi‰j‰n pistekent‰t lopputuloksen mukaisella v‰rill‰. V‰rit
     * vaihtelevat sen mukaan, tuliko voitto, tappio, Blackjack vai
     * "ep‰m‰‰r‰inen". Ep‰m‰‰r‰isell‰ viitataan tilanteeseen, jossa
     * jaetun k‰den pistem‰‰r‰t ovat 26 ja 20. Jos jakajan pistem‰‰r‰
     * on 18, on vaikea sanoa onko jakaja voittanut tai h‰vinnyt.
     */
    private void voittajanVarjays() {
         if (this.peli == null)
              return;

         int tulos_1 = peli.lopputulosPelaaja_1(); //Miten k‰vi ottelussa "pelaaja_1" vs. "jakaja"?
         int tulos_2 = -1; //alkuarvo

//Varjayksessa tulee ongelmia kun pelaaja_1 voittaa ja pelaaja_2 haviaa. Pelaaja_1
//voidaan varjata VOITTOVARIlla ja Pelaaja_2 TAPPIOVARIlla, mutta entapa jakaja?
         if ( peli.kerroErikoistoiminto() == 2 ) { //PELI ON JAETTU
              tulos_2 = peli.lopputulosPelaaja_2(); //Miten k‰vi ottelussa "pelaaja_2" vs. "jakaja"?

              if (tulos_2 == 0) //tappio
                   pelaajanPisteet_2.setForeground(TAPPIOVARI);
              else if (tulos_2 == 1) //voitto
                   pelaajanPisteet_2.setForeground(VOITTOVARI);
              else if (tulos_2 == 2) //tasapeli-tappio
                   pelaajanPisteet_2.setForeground(TAPPIOVARI); 
              else if (tulos_2 == 3) //Blackjack-tappio
                   pelaajanPisteet_2.setForeground(TAPPIOVARI); 
              //jaetussa pelissa pelaaja ei voi saada Blackjackia  
         }
         else {                                   //Varjataan jakaja
              if (tulos_1 == 0) //pelaajan tappio on jakajan voitto
                   jakajanPisteet.setForeground(VOITTOVARI);
              else if (tulos_1 == 1) //pelaajan voitto on jakajan tappio
                   jakajanPisteet.setForeground(TAPPIOVARI);
              else if (tulos_1 == 2) //pelaajan tasapeli-tappio on jakajan voitto
                   jakajanPisteet.setForeground(VOITTOVARI); 
              else if (tulos_1 == 3) //Blackjack-tappio on jakajan Blackjack-voitto
                   jakajanPisteet.setForeground(BLACKJACKVARI);
              else if (tulos_1 == 4) //pelaajan Blackjack-voitto on jakajan tappio
                   jakajanPisteet.setForeground(TAPPIOVARI);
              else if (tulos_1 == 5) //Blackjack-tasapeli
                   jakajanPisteet.setForeground(BLACKJACKVARI);
              else if (tulos_1 == 6) //Vakuutusvoitto (panos palautuu pelaajalle)
                   jakajanPisteet.setForeground(BLACKJACKVARI);
         }

         //Varjataan pelaaja_1 aina!
         if (tulos_1 == 0) //tappio
              pelaajanPisteet_1.setForeground(TAPPIOVARI);
         else if (tulos_1 == 1) //voitto
              pelaajanPisteet_1.setForeground(VOITTOVARI);
         else if (tulos_1 == 2) //tasapeli-tappio
              pelaajanPisteet_1.setForeground(TAPPIOVARI); 
         else if (tulos_1 == 3) //Blackjack-tappio
              pelaajanPisteet_1.setForeground(TAPPIOVARI);
         else if (tulos_1 == 4) //Blackjack-voitto
              pelaajanPisteet_1.setForeground(BLACKJACKVARI);
         else if (tulos_1 == 5) //Blackjack-tasapeli
              pelaajanPisteet_1.setForeground(BLACKJACKVARI);
         else if (tulos_1 == 6) //Vakuutusvoitto (panos palautuu pelaajalle)
              pelaajanPisteet_1.setForeground(TAPPIOVARI);

         //Kun molemmat pelaajat haviavat tai voittavat:
         if (peli.kerroErikoistoiminto() == 2 ) {  //PELI ON JAETTU
              if (tulos_1 == 3 || tulos_1 == 5) //Jakajalla oli blackjack
                   jakajanPisteet.setForeground(BLACKJACKVARI);
              else if ( (tulos_1 == 0 || tulos_1 == 2) && (tulos_2 == 0 || tulos_2 == 2) )
                   jakajanPisteet.setForeground(VOITTOVARI); //Molemmille normaali tappio
              else if (tulos_1 == 1 && tulos_2 == 1) //Molemmille normaali voitto (Blackjack on mahdoton)
                   jakajanPisteet.setForeground(TAPPIOVARI);
         }
    }

}
