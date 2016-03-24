/*
 * @(#)Blackjack.java 29.4.2003
 *
 * Tiedosto sis‰lt‰‰ myˆs luokkien HoiteleIkkunanSulkeminen
 * ja PiilotaOhjeIkkuna l‰hdekoodin.
 * 
 * Copyright 2003 Esa Junttila
 */

import java.awt.*;
import java.awt.event.*;

/**
 * Blackjack on pelin p‰‰ohjelmaluokka. Blackjack huolehtii korttien vet‰misest‰,
 * korttien v‰lisist‰ viiveist‰ ja k‰yttˆliittym‰n oikeasta puolesta (napit,
 * vinkkiruutu, panos ja saldo).<p>
 * Ruudun p‰ivityksest‰ vastaa metodi <tt>refresh()</tt>, jota kutsutaan joka kerta kun
 * Kasien korttim‰‰r‰ vaihtuu (ja useamminkin). Refresh sisaltaa myos kutsun
 * k‰yttˆliittym‰n vasemman puolen p‰ivitt‰v‰‰n <tt>KorttiGUI</tt>-luokan metodiin
 * <tt>paivitaKortit()</tt>. Jokaisen p‰ivityksen yhteydess‰ p‰ivitet‰‰n kaikki mahdollinen.
 * <p> Jos ohjelma yritt‰‰ asettaa <tt>null</tt>-arvoa Kasi-olioon v‰kisin tai
 * yritt‰‰ luoda Pelikierrosta Korttipakan asemesta <tt>null</tt>-arvolla, ohjelman
 * annetaan kaatua.
 *
 * @author  Esa Junttila
 * @version 29.4.2003
 */
public class Blackjack extends Frame 
                       implements ActionListener, MouseListener {

    /**
     * Blackjack-pelin k‰ynnistysmetodi
     */
    public static void main(String[] args) {
         Blackjack ikkuna = new Blackjack();
         ikkuna.setSize(640,480);
         ikkuna.setTitle("Blackjack by Esa");
         ikkuna.setVisible(true);
    }

  //-------KOMPONENTTIEN KOOT:
    /** Kuinka leve‰ on vinkkiruutu*/
    private static final int VINKKIRUUDUN_LEVEYS = 6;


  //-------VƒRIT:
    /** Blackjackin peli-ikkunan taustav‰ri*/
    private static final Color IKKUNAN_TAUSTAVARI = new Color(0, 128, 0); //tummanvihrea
    
    /** Vinkkiruudun taustav‰ri */
    private static final Color VINKKIRUUDUN_VARI = new Color(128, 255, 255); //vaaleansininen
    
    /** Editoitavan panosruudun v‰ri */
    private static final Color PANOSRUUDUN_VARI = Color.WHITE;
    
    /** Ei-editoitavan panosruudun v‰ri */
    private static final Color PANOSRUUTU_EI_EDITOINTIA = SystemColor.control; //Tekstikenttien haalea tausta.
    
    /** Mink‰ v‰risi‰ ovat virheilmoitukset */
    private static final Color VIRHEVARI = new Color(255, 100, 100); //melko vahva punainen


  //-------FONTIT:
    /** Millaisella fontilla vinkkitekstit tulostuvat vinkkiruutuun*/
    private static final Font VINKKIFONTTI = new Font("SansSerif", Font.BOLD, 16); //fontti, tehosteet, koko
    
    /** Millaisella fontilla panoksen ja saldon suuruus kirjoitetaan vastaaviin ruutuihin*/
    private static final Font SALDOPANOSFONTTI = new Font("SansSerif", Font.BOLD, 16);

    /** Millaisella fontilla "Panos"- ja "Saldo"-tekstit kirjoitetaan ikkunaan */
    private static final Font SALDOPANOSTEKSTIFONTTI = new Font("SansSerif", Font.PLAIN, 14);


  //-------VIIVE:
    /** Mink‰ pituista viivett‰ k‰ytet‰‰n kortinottojen v‰liss‰ */
    private static final int ODOTUSAIKA = 0; //hyv‰ arvo n. 1000 = 1 s


  //-------MUUT VAKIOT:
    /** Mit‰ vinkkiruudussa lukee oletuksena kun vinkki‰ ei ole viel‰ haettu */
    private static final String OLETUSVINKKI = "VINKKI";


  //-------MUUTTUJAT:
    /** Mit‰ ja mink‰ kokoista pakkaa t‰m‰ Blackjack-peli k‰ytt‰‰*/
    private Korttipakka pakka = new Korttipakka(Saannot.PAKALLISIA_PELISSA);
    
    /** Blackjack-pelin alussa annettavan raham‰‰r‰n suuruus*/
    private int rahaa = 100;

    /** Kun peli k‰ynnistet‰‰n, mik‰ panoksen suuruus on oletuksena*/
    private int peruspanos = 10;
    
    /** Apumuuttuja pelin panoksen selvitt‰miseen*/
    private int pelinPanos = this.peruspanos;

    /** Meneill‰‰n oleva Pelikierros*/
    private Pelikierros peli;


//------- GRAAFISEN KAYTTOLIITTYMAN KOMPONENTIT:
//----------------------------------------------
    private KorttiGUI korttialue;   // KorttiGUI extends Panel. K‰yttˆliittym‰n vasen puoli.
  //-------
    private Panel nappialue; //K‰yttˆliittym‰n oikea puoli

    private Panel erikoisnapit;
    private Button nappi_tuplaus = new Button("Tuplaa");
    private Button nappi_jaaPeli = new Button("Jaa k‰si");
    private Button nappi_vakuutus = new Button("Vakuuta");

    private Panel saldopanosalue;
    private Panel panosrivi;
    private Panel saldorivi;
    private TextField panosruutu = new TextField("", 3);
    private Label saldoruutu = new Label("", Label.RIGHT);
    private Label panosteksti = new Label("Panos:", Label.RIGHT);
    private Label saldoteksti = new Label("Saldo:", Label.RIGHT);

    private Button nappi_ota = new Button("Ota kortti");
    private Button nappi_jaaTahan = new Button("J‰‰ t‰h‰n");
    private Button nappi_uusiPelikierros = new Button("Jaa uusi peli");

    private Panel ohjealue;
    private Button nappi_ohje = new Button("Ohje");
    private Button valenappi_1 = new Button("valenappi_1"); //luomassa tyhj‰‰ tilaa

    private Panel lopetusalue;
    private Button nappi_lopetus = new Button("Lopeta peli");
    private Button valenappi_2 = new Button("valenappi_2"); //luomassa tyhj‰‰ tilaa

    private Panel vinkkialue;
    private TextField vinkkiruutu = new TextField(OLETUSVINKKI, VINKKIRUUDUN_LEVEYS);

    private Dialog ohjeikkuna;


    /**
     * Asettaa k‰yttˆliittym‰n ja valmistautuu aloittamaan ensimm‰isen Pelikierroksen.
     */
    public Blackjack() {
         //Haetaan "edellisen kierroksen" lopputila, jotta voitaisiin muokata panosta
         peli = new Pelikierros(this.rahaa, this.peruspanos); 

         this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));  //align, h-gap, v-gap
         this.setBackground(IKKUNAN_TAUSTAVARI);
       //-------         
         this.korttialue = new KorttiGUI(this.peli);
         this.nappialue = new Panel(new GridLayout(4, 2, 8, 20) );

//-------
         this.erikoisnapit = new Panel(new GridLayout(3, 1, 0, 10) );
         this.erikoisnapit.add(this.nappi_tuplaus);
         this.erikoisnapit.add(this.nappi_jaaPeli);
         this.erikoisnapit.add(this.nappi_vakuutus);
         this.nappialue.add(this.erikoisnapit);      
       //--
         this.nappialue.add(this.vinkkiruutu);
         this.vinkkiruutu.setEditable(false);
         this.vinkkiruutu.setFont(VINKKIFONTTI);
         this.vinkkiruutu.setBackground(VINKKIRUUDUN_VARI);
       //--
         this.nappialue.add(this.nappi_ota);
         this.nappialue.add(this.nappi_jaaTahan);
         
       //-------
         this.saldopanosalue = new Panel(new GridLayout(2,1,0,10) ); //rows,cols,hgap,vgap

         this.panosrivi = new Panel(new FlowLayout(FlowLayout.LEFT,0,0)); //align, hgap, vgap
         this.panosrivi.add(this.panosteksti);
         this.panosrivi.add(this.panosruutu);
         this.saldopanosalue.add(this.panosrivi);

         this.saldorivi = new Panel(new FlowLayout(FlowLayout.LEFT,0,0));
         this.saldorivi.add(this.saldoteksti);
         this.saldorivi.add(this.saldoruutu);
         this.saldopanosalue.add(this.saldorivi);

         this.panosruutu.setFont(SALDOPANOSFONTTI);
         this.panosteksti.setFont(SALDOPANOSTEKSTIFONTTI);
         this.saldoruutu.setFont(SALDOPANOSFONTTI);
         this.saldoteksti.setFont(SALDOPANOSTEKSTIFONTTI);

         this.nappialue.add(this.saldopanosalue);
       //-------

         this.nappialue.add(this.nappi_uusiPelikierros);

         this.ohjealue = new Panel(new GridLayout(2,1,0,10)); //rows,cols,hgap,vgap
         this.ohjealue.add(this.valenappi_1);
         this.ohjealue.add(this.nappi_ohje);
         this.nappialue.add(this.ohjealue);

         this.lopetusalue = new Panel(new GridLayout(2,1,0,10)); //rows,cols,hgap,vgap
         this.lopetusalue.add(this.valenappi_2);
         this.lopetusalue.add(this.nappi_lopetus);
         this.nappialue.add(this.lopetusalue);

         //valenapit ovat aina piilossa. Ne ovat vain luomassa tyhjaa tilaa ruudulle.
         this.valenappi_1.setVisible(false);
         this.valenappi_2.setVisible(false);

         this.add(this.korttialue);
         this.add(this.nappialue);

      //-------
         this.luoOhjeikkuna();
    //------------
    //--TAPAHTUMAKUUNTELIJAT:
         this.nappi_uusiPelikierros.addActionListener(this);
         this.nappi_lopetus.addActionListener(this);
         this.nappi_ohje.addActionListener(this);
         this.nappi_ota.addActionListener(this);
         this.nappi_jaaTahan.addActionListener(this);
         this.nappi_tuplaus.addActionListener(this);
         this.nappi_jaaPeli.addActionListener(this);
         this.nappi_vakuutus.addActionListener(this);

         this.vinkkiruutu.addMouseListener(this);
        
         this.addWindowListener(new HoiteleIkkunanSulkeminen());

         this.refresh();
    }

    /**
     * Metodi suorittaa halutun Blackjackin toiminnon ja
     * ohjaa peli‰ oikeaan suuntaan. T‰ss‰ tapahtuu koko
     * Blackjack-pelin ylin kontrolli. Metodi hoitaa myˆs
     * pelikierroksen lopussa teht‰v‰t toimenpiteet.
     */
    public void actionPerformed(ActionEvent tapahtuma) {
         Object aiheuttaja = tapahtuma.getSource();
         
         // Kenell‰ oli vuoro ennen kuin tehtiin *toiminto*
         int vanhaVuoro = this.peli.kenenVuoro();

         if (aiheuttaja == this.nappi_ota)
              this.suoritaLisakortti();
         else if (aiheuttaja == this.nappi_jaaTahan)
              this.suoritaSeis();
         else if (aiheuttaja == this.nappi_tuplaus)
              this.suoritaTuplaus();
         else if (aiheuttaja == this.nappi_jaaPeli)
              this.suoritaKadenJako();
         else if (aiheuttaja == this.nappi_vakuutus)
              this.suoritaVakuutus();
         else if (aiheuttaja == this.nappi_uusiPelikierros)
              this.jaaUusiKierros();
         else if (aiheuttaja == this.nappi_ohje)
              this.ohjeikkuna.show(); //Tuodaan ohjeikkuna n‰kyviin.
         else if (aiheuttaja == this.nappi_lopetus)
              this.lopetus(); //Ohjelma sammuu.

         this.refresh();

         // Kenell‰ on vuoro *toiminnon* j‰lkeen
         int uusiVuoro = this.peli.kenenVuoro();

         //Peli jaettiin ja vuoro siirtyi pelaajan ekalle jaetulle k‰delle
         if (vanhaVuoro == 1 && uusiVuoro == 2) {
              this.odota(ODOTUSAIKA);
              this.peli.lisaa();         //vuoro==2, toinen kortti pelaajan ekalle jaetulle kadelle
              this.refresh();

              //(Jos pelaaja jakoi kaksi ‰ss‰‰, lisaa()-metodi vaihtoi heti vuoron pelaajalle tokalle k‰delle)
              if (this.peli.kenenVuoro() == 3) { //Kavi siis niin etta pelaaja jakoi kaksi assaa.
                   this.odota(ODOTUSAIKA);       //Seka pelaaja 1 etta 2 saavat (vain) yhden kortin.
                   this.peli.lisaa();         
                   this.refresh();              
              }
         }
         //Vuoro siirtyy normaalisti pelaajan ekalta jaetulta k‰delt‰ toiselle
         else if (vanhaVuoro == 2 && uusiVuoro == 3) {
              this.odota(ODOTUSAIKA);
              this.peli.lisaa();         //vuoro==3, toinen kortti pelaajan tokalle jaetulle kadelle
              this.refresh();
         }
         
         // Lopulta vuoro ei ole en‰‰ pelaajalla vaan jakajalla
         if (this.peli.kenenVuoro() == 4) { //jakajan vuoro
              if ( Saannot.ottaakoJakajaKortin(peli) ) {  //Otetaanko kortteja ylipaataan?
                   boolean jatketaan = true;
                   while( jatketaan ) {
                        this.odota(ODOTUSAIKA);
                        jatketaan = this.peli.lisaa(); //Jakaja saa kortteja kunnes lisaa()==false
                        this.refresh();
                   }
              }

              //pakan sekoitus kierroksen loppuun:
              if (Saannot.pitaakoPakkaSekoittaa(this.pakka) ) {
                   this.pakka.sekoitaPakka();
                   this.ilmoitaPakanSekoittamisesta();
              }
              //Lopetetaan pelikierros ( vuoro 4 --> -1 )
              this.peli.vaihdaVuoroa();
              this.rahaa = this.peli.rahatilanne(); //Rahojen paivitys kierroksen p‰‰tteksi
         }
         this.refresh();
    }


    /** Pelaaja halusi ottaa lis‰kortin*/
    private void suoritaLisakortti() {
         this.peli.lisaa();
    }

    /** Pelaaja halusi j‰‰d‰ pistelukuunsa */
    private void suoritaSeis() {
         this.peli.vaihdaVuoroa();
    }

    /** Pelaaja halusi tuplata pelins‰ */
    private void suoritaTuplaus() {  //Peli tuplataan. Yksi lisakortti ja siirrytaan jakajan vuoroon.
         this.peli.tuplaa();
         this.peli.lisaa(); //ainoa lis‰kortti (pakollinen)
         this.peli.vaihdaVuoroa(); //(vuoro siirtyy v‰kisinkin jakajalle)
         this.refresh();
         this.odota(ODOTUSAIKA);
    }

    /** Pelaaja halusi jakaa pelins‰*/
    private void suoritaKadenJako() {
         try {
              this.peli.jaaPeli();       //(AIHEUTTAA POIKKEUKSEN, JOS kutsutaan 'new Kasi(pakka, null)' )
         } catch (Exception e) {
              System.out.println("Tapahtui mahtivirhe: " + e + "  Jokin metodi kutsui 'new Kasi(pakka, null)'!");
         }
         this.peli.vaihdaVuoroa(); //Vuoro siirret‰‰n pelaajan ekalle jaetulle k‰delle
    }

    /** Pelaaja halusi vakuuttaa pelins‰*/
    private void suoritaVakuutus() {
         this.peli.vakuuta();
    }

    /** Pelaaja halusi osallistui uuteen Pelikierrokseen*/
    private void jaaUusiKierros() {

              //Panoksen ja saldon tarkistus: (panos haetaan k‰yttˆliittym‰ss‰ olevasta panosruudusta)
              int asetettavaPanos;
              try{
                   asetettavaPanos = Integer.parseInt(this.panosruutu.getText() );
              }
              catch (NumberFormatException e) {
                   this.virheellinenPanos();
                   return;
              }

              //Panoksen on oltava kahdella jaollinen kokonaisluku.
              //Myos rahaa pitaa olla tarpeeksi. Nollapanos hyv‰ksyt‰‰n.
              if (asetettavaPanos < 0 || asetettavaPanos > this.rahaa || asetettavaPanos%2 == 1) {
                   
                   //Tehd‰‰n virheilmoitus
                   this.virheellinenPanos();
                   
                   //Jos panos oli liian suuri, ehdotetaan rahavaroille suurinta mahdollista panosta
                   if (asetettavaPanos > this.rahaa)
                        this.peruspanos = this.rahaa - this.rahaa%2;
                   return;
              }
              else this.korttialue.muutaInforuutua("", false); // Panos kunnossa --> Inforuutu piiloon

              this.peruspanos = asetettavaPanos;

              //vinkkiruudun oletusarvot:
              this.vinkkiruutu.setText(OLETUSVINKKI);

              //Otetaan k‰yttˆˆn uusi Pelikierros (AIHEUTTAA POIKKEUKSEN, JOS this.pakka==null)
              try {
                   this.peli = new Pelikierros(this.pakka, this.rahaa, this.peruspanos);
              } catch (Exception e) {
                   System.out.println("Tapahtui mahtivirhe: " + e + "  Jokin metodi kutsui 'new Pelikierros(null, int, int)'!");
              }

              //P‰ivitet‰‰n k‰yttˆliittym‰n vasemman puolen tiedot Pelikierroksesta
              this.korttialue.vaihdaPelia(this.peli);
              this.refresh();

              //Jaetaan Pelikierroksen alussa tarvittavat kolme korttia
              boolean jatketaan = true;
              while (jatketaan) {
                   this.odota(ODOTUSAIKA);
                   jatketaan = this.peli.alkukortit();
                   this.refresh();
              }

    }

// HIIRITAPAHTUMAT:
    /** Vinkkiruutuun liittyv‰ hiiren napin klikkaus ei tee mit‰‰n*/
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Vinkkiruutuun liittyv‰ hiiren "sis‰‰ntulo" vinkkiruutuun tuo
     * pelitilannetta vastaavan vinkin n‰kyviin
     */
    public void mouseEntered(MouseEvent e) {
          this.vinkkiEsiin();
    }

    /** Vinkkiruutuun liittyv‰ hiiren liikkuminen ruudulla ei tee mit‰‰n*/
    public void mouseExited(MouseEvent e) {
    }

    /** Vinkkiruutuun liittyv‰ hiiren napin painaminen ei tee mit‰‰n*/
    public void mousePressed(MouseEvent e) {
    }

    /** Vinkkiruutuun liittyv‰ hiiren napin vapauttaminen ei tee mit‰‰n*/
    public void mouseReleased(MouseEvent e) {
    }


    /**
     * Ohjelman suoritus pys‰htyy parametrina saadun arvon ajaksi (millisekunteja).
     * <p>Odottamiseen liittyy ongelma. Jos koodissa piirret‰‰n kortin kuva
     * ja pistet‰‰n ohjelma sen j‰lkeen tauolle esim. 1000 millisekunniksi,
     * Java tulkitsee asian niin, ett‰ ohjelma odottaa sekunnin ja piirt‰‰ kortin
     * kuvan vasta sitten! Miss‰ j‰rki?!
     */
    private void odota(int odotusaika) {
         try {
              Thread.sleep(odotusaika);
         } catch (InterruptedException e) {
              System.out.println("Thread.sleep(odotusaika) aiheutti poikkeuksen!");
         }
    }

    /**
     * P‰ivitt‰‰ k‰yttˆliittym‰n vastaamaan Pelikierroksella vallitsevaa tilannetta.
     * Napit, jotka eiv‰t ole pelitilanteen mukaan mahdollisia, poistuvat n‰kyvist‰
     * tai ainakin muuttuvat harmaiksi. Pelaajan saldo ja panos voivat myˆs muuttua
     * metodin kutsun yhteydess‰. Itse metodi hoitaa k‰yttˆliittym‰n oikean puolen,
     * mutta se kutsuu samalla myˆs k‰yttˆliittym‰n vasemman puolen p‰ivittavaa
     * KorttiGUI:n metodia paivitaKortit().
     */
    public void refresh() {

       //-- Ovatko toiminnot mahdollisia
         this.nappi_tuplaus.setVisible( Saannot.voikoPelaajaTuplataKaden(this.peli) );
         this.nappi_jaaPeli.setVisible( Saannot.voikoPelaajaJakaaKaden(this.peli) );
         this.nappi_vakuutus.setVisible( Saannot.voikoPelaajaVakuuttaaKaden(this.peli) );
         this.nappi_ota.setEnabled( Saannot.voikoPelaajaOttaaKortin(this.peli) );
         this.nappi_jaaTahan.setEnabled( Saannot.voikoPelaajaJaada(this.peli) );

         //Uusi jako vain jos edellinen kierros on paattynyt.
         this.nappi_uusiPelikierros.setEnabled( this.peli.kenenVuoro() == -1); 
      
       //-- Jos vinkkiruutua on katsottu pelikierroksen aikana, vinkki paivittyy itsestaan.
         if ( !vinkkiruutu.getText().equals(OLETUSVINKKI)) //onko oletusteksti edelleen n‰kyvill‰?
              this.vinkkiEsiin();
              

       //-- Rahatilanteen paivitys
         this.rahaa = this.peli.rahatilanne();
         this.pelinPanos = this.peli.panostilanne();
       
         this.saldoruutu.setText("" + this.rahaa); //saldoruutu heijastelee aina kadessa olevaa rahamaaraa.
                                              //Panokset ovat "pelialueella".

       //-- Panosruutu:
         if (peli.kenenVuoro() == -1) {  //Kun peli ei ole kaynnissa, sallitaan panoksen muuttaminen
             this.panosruutu.setBackground(PANOSRUUDUN_VARI); //editoitava valkoinen
             
             //Palautetaan panosruutuun edellisen kierroksen alkuperainen panosmaara
             this.panosruutu.setText("" + this.peruspanos); 
             
             //Panosta voi muuttaa
             this.panosruutu.setEditable(true); 
                                                
         }
         else {
             this.panosruutu.setBackground(PANOSRUUTU_EI_EDITOINTIA); //harmaa tausta
             this.panosruutu.setEditable(false);
             this.panosruutu.setText("" + peli.panostilanne());
         }

         this.korttialue.paivitaKortit();
         this.validate();
    }

    /** Sulkee ohjelman aikailematta */
    private void lopetus() {    
         System.exit(0);  // ikkunan ja koko ohjelman sulkeminen
    }

    /** Hakee pelitilanteeseen sopivan vinkin ja tuo sen n‰kyville vinkkiruutuun*/
    private void vinkkiEsiin() {
         String vinkki = this.peli.haeVinkki();
         if (vinkki == null)
              return;
         
         if (vinkki.indexOf("VIRHE") != -1) {
              this.virheellinenVinkki(vinkki); //Vilautetaan virheteksti‰ inforuudussa
         }
         else
              this.vinkkiruutu.setText(vinkki);   
    }

    /**
     * Jos pelaaja yritt‰‰ pelata virheelisell‰ panoksella, panosruutu
     * v‰l‰ht‰‰ virhev‰rill‰ ja inforuutu tuo esiin, miksi panos oli
     * virheellinen.
     */
    private void virheellinenPanos() {
         this.panosruutu.setBackground(VIRHEVARI);
         this.korttialue.muutaInforuutua("Panos on virheellinen!\n" +
                                         "- Panoksen on oltava kahdella jaollinen\n" +
                                         "- Panos ei saa ylitt‰‰ rahavaroja", true);
         this.odota(1000); //Virhev‰ri katoaa panosruudusta sekunnin j‰lkeen
         this.panosruutu.setBackground(PANOSRUUDUN_VARI);
    }

    /**
     * Jos ohjelman vinkkipalvelu k‰ytt‰ytyy omituisesti, kerrotaan
     * virheilmoitus inforuudussa.
     *
     * @param virheilmoitus Vinkkipalvelun antama virheilmoitus,
     * joka tulostetaan inforuudulla.
     */
    private void virheellinenVinkki(String virheilmoitus) {
         this.korttialue.muutaInforuutua("Vinkkipalvelu on seonnut:\n"+virheilmoitus, true);
         this.odota(3000);
         this.korttialue.muutaInforuutua("", false);
    }

    /**
     * Kun ohjelma p‰‰tt‰‰ sekoittaa pakan, inforuudussa vilahtaa tieto siit‰.
     */
    private void ilmoitaPakanSekoittamisesta() {
         this.korttialue.muutaInforuutua("Sekoitetaan pakkaa...", true);
         this.odota(3000);
         this.korttialue.muutaInforuutua("", false);
    }

    /**
     * Muodostetaan ohjelman ohjeet ja Blackjackin s‰‰nnˆt sis‰lt‰v‰ ikkuna.
     * Ikkuna on alunperin piilossa.
     */
    private void luoOhjeikkuna() {
         this.ohjeikkuna = new Dialog(this, "Ohjeet");

         TextArea ohjeet = new TextArea("", 10, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
         ohjeet.setText(Saannot.annaOhjeteksti());
         
         ohjeikkuna.add(ohjeet);
         ohjeikkuna.addWindowListener(new PiilotaOhjeikkuna(this.ohjeikkuna));
         ohjeikkuna.setSize(600,400); //lev, kor
         ohjeikkuna.setVisible(true);
         ohjeikkuna.hide();
    }
}


/**
 * Itse Blackjack-ohjelman sulkemista helpottava luokka
 */
class HoiteleIkkunanSulkeminen extends WindowAdapter {
  public void windowClosing(WindowEvent tapahtuma) {
    System.exit(0);  // ikkunan ja ohjelman sulkeminen  
  }
}

/**
 * Blackjackin ohjeikkunan piilottamista helpottava luokka. Kun
 * ohjeikkuna halutaan sulkea, kutsutaankin t‰t‰ ja piilotetaan ikkuna.
 */
class PiilotaOhjeikkuna extends WindowAdapter {
  Dialog ohjeikkuna;
  public PiilotaOhjeikkuna(Dialog ikkuna) {
        this.ohjeikkuna = ikkuna;
  }
  public void windowClosing(WindowEvent tapahtuma) {
      if (this.ohjeikkuna !=null)
          this.ohjeikkuna.hide();
  }
}