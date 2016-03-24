/*
 * @(#)Pelikierros.java 29.4.2003
 *
 * Copyright 2003 Esa Junttila
 */



/**
 * Luokan ilmentym‰ edustaa Blackjack-pelin yht‰ pelikierrosta. Pelikierroksen
 * kautta toteutetaan kaikki peliin liittyv‰t tapahtumat. Pelikierroksessa
 * korttien ottaminen on j‰tetty sit‰ k‰ytt‰v‰n luokan teht‰v‰ksi. T‰m‰
 * mahdollistaa jouston k‰yttˆliittym‰‰ ajatellen - Pelikierros ei rajoita
 * k‰yttˆliittym‰n luontia mill‰‰n tavalla. <p>Kun pelikierros-olio on luotu,
 * on ensin kutsuttava kolmasti alkukortit()-metodia, jolloin pelaaja voi suorittaa
 * toimintoja.<p>Kun pelaaja on toimintonsa tehnyt ja vuoro on jakajalla
 * (vuoro==4), Pelikierrosta k‰ytt‰v‰ss‰ luokassa toteutetaan jakajan kortinotot.
 * <p>Pelikierros valvoo t‰ysin pelaajalla olevia rahavaroja ja pelin panosta ja
 * laskee saldon uusiksi kierroksen p‰‰tytty‰.
 *
 * @author  Esa Junttila
 * @version 29.4.2003
 */
public class Pelikierros{

    /** Pelikierroksella k‰ytett‰v‰ Korttipakka*/
    private Korttipakka pakka;

    /**
     * Pelikierroksella esiintyv‰t Kadet: jakajan ainoa Kasi,
     * pelaajan varsinainen Kasi ja pelaajan toinen Kasi(jaettua peli‰ varten)
     */
    private Kasi jakajanKasi = null;
    private Kasi pelaajanKasi_1 = null;
    private Kasi pelaajanKasi_2 = null;
 
    /**
     * Kertoo Pelikierroksen sis‰isen tilan, eli kenell‰ on nyt vuoro.
     *<br><ul>
     * <li>-1, Pelikierros on ohi</li>
     * <li>0, Pelikierros on alussa (alkukortti tai useampia puuttuu)</li>
     * <li>1, Pelaajan ainoan Kaden vuoro</li>
     * <li>2, Pelaajan jaetun pelin ensimm‰isen Kaden vuoro</li>
     * <li>3, Pelaajan jaetun pelin toisen Kaden vuoro</li>
     * <li>4, Jakajan vuoro</li>
     * </ul>
     */
    private int vuoro = 0;

    /**
     * Kertoo, onko kierroksella tehty erikoistoimintoja (toiminnot 
     * poissulkevat kaikki toisensa)
     * <br><ul>
     * <li>-1, Pelin k‰ynnistystila (Pelikierrosta k‰ytt‰v‰ luokka asettaa t‰m‰n)</li>
     * <li>0, ei erikoistoimintoja</li>
     * <li>1, peli on tuplattu</li>
     * <li>2, peli on jaettu</li>
     * <li>3, peli on vakuutettu</li>
     * </ul>
     */
    private int erikoistoiminto = 0;

    /** Paljonko rahaa pelaajalla on juuri nyt taskussaan */
    private int rahaa;

    /** Mik‰ on pelin panos juuri nyt (panos voi muuttua tuplauksen ja vakuutuksen vuoksi) */
    private int panos;

    /**
     * Luo uuden Pelikierroksen, jossa Peli alkaa alkutilasta 0. T‰m‰n kierroksen
     * panoksen suuruuteen ei voi en‰‰ vaikuttaa. Panos veloitetaan rahavaroista,
     * joten on tarkistettava, ett‰ rahat riitt‰v‰t ennen kuin kutsutaan t‰t‰
     * konstruktoria. Panoksen on oltava positiivinen ja jaollinen kahdella!<p>
     * Ennen kuin pelin normaalit toiminnot muuttuvat mahdollisisksi, on jaettava kolme
     * aloituskorttia kutsumalla metodia alkukortit() kolmasti.
     *
     * @param pelipakka Mit‰ Korttipakkaa Peliss‰ k‰ytet‰‰n
     * @param pelaajallaRahaa Paljonko pelaajalla on rahaa juuri ennen pelin alkamista
     * @param pelinPanos Paljonko pelaaja tahtoisi asettaa rahaa pelin panokseksi
     *
     * @exception Exception heitet‰‰n, jos Pelikierrosta kutsuttiin
     * Korttipakka-olion asemesta arvolla 'null'.
     */
    public Pelikierros(Korttipakka pelipakka, int pelaajallaRahaa, int pelinPanos) 
                                                                   throws Exception{
         if (pelipakka == null)
              throw new Exception("Pelikierrosta ei voi luoda jos Korttipakan tilalla on 'null'");
         
         this.pakka = pelipakka;           
         this.jakajanKasi = new Kasi(this.pakka);
         this.pelaajanKasi_1 = new Kasi(this.pakka);                      
         this.pelaajanKasi_2 = new Kasi(this.pakka);

         this.vuoro = 0;
         this.erikoistoiminto = 0;

         this.panos = pelinPanos;

         //Pelaajan panos siirtyy pˆyd‰lle
         this.rahaa = pelaajallaRahaa - pelinPanos;
    }


    /**
     * Luo uuden Pelikierroksen, jossa Peli alkaa "edellisen kierroksen" lopputilasta -1.
     * T‰t‰ on tarkoitus k‰ytt‰‰ kun koko Blackjack-peli on juuri aloitettu. T‰ll‰ konstruktorilla
     * panoksen suuruutta on mahdollista muuttaa heti seuraavaa (ensimm‰ist‰) kierrosta varten.
     * Konstruktori ei v‰henn‰ rahoja, sill‰ normaali pelaaminen ei ole mahdollista.
     *
     * @param rahaa Paljonko rahaa pelaajalla on taskussaan juuri asetetun panoksen lis‰ksi
     * @param panos Mik‰ on palaajan asettaman panoksen suuruus
     */
    public Pelikierros(int rahaa, int panos) {

         //Lumekorttipakat (n‰ist‰ ei vedet‰ koskaan kortteja)
         this.jakajanKasi = new Kasi((Korttipakka)null);                         
         this.pelaajanKasi_1 = new Kasi((Korttipakka)null);                      
         this.pelaajanKasi_2 = new Kasi((Korttipakka)null);                      

         this.rahaa = rahaa;
         this.panos = panos;
         this.vuoro = -1;     //Blackjack-ohjelman aloitustilanne on "edellisen kierroksen" loppu.
         this.erikoistoiminto = -1; //Ohjelman kaynnistystila
    }

    /** Ei kutsuta koskaan. Ilman pakkaa ja rahaa on ik‰v‰ pelata*/
    private Pelikierros() {}



    /**
     * Kertoo mit‰ erikoistoimintoja peliss‰ on tehty, jos on.
     * @return <ul>
     * <li>-1, Pelin k‰ynnistystila (Pelikierrosta k‰ytt‰v‰ luokka asettaa t‰m‰n)</li>
     * <li>0, ei erikoistoimintoja</li>
     * <li>1, peli on tuplattu</li>
     * <li>2, peli on jaettu</li>
     * <li>3, peli on vakuutettu</li>
     * </ul>
     */
    public int kerroErikoistoiminto() { return this.erikoistoiminto; }


    /**
     * Kertoo Pelikierroksen sis‰isen vuoron. Kenell‰ on vuoro?
     *
     * @return <ul>
     * <li>-1, Pelikierros on ohi</li>
     * <li>0, Pelikierros on alussa (alkukortti tai useampia puuttuu)</li>
     * <li>1, Pelaajan ainoan Kaden vuoro</li>
     * <li>2, Pelaajan jaetun pelin ensimm‰isen Kaden vuoro</li>
     * <li>3, Pelaajan jaetun pelin toisen Kaden vuoro</li>
     * <li>4, Jakajan vuoro</li>
     * </ul>
     */
    public int kenenVuoro() { return this.vuoro; }


    /**
     * Palauttaa kopiot kaikista Pelikierroksen Kasista. Kaden Kortit
     * ovat identtisi‰ kopioita alkuper‰isist‰, mutta Kasilla ole pakkaa
     * (ne eiv‰t voi siis vet‰‰ Kortteja). Kadet voivat myˆs olla null.
     * Taulukossa Kadet ovat j‰rjestyksess‰: jakaja, pelaaja_1, pelaaja_2.
     *
     * @return 3-paikkainen Kasi-taulukko, jonka
     * <br><ul>
     * <li>tauluindeksiss‰ 0: jakajan Kaden kopio tai <tt><null></tt></li>
     * <li>tauluindeksiss‰ 1: pelaajan ensimm‰isen Kaden kopio tai <tt>null</tt></li>
     * <li>tauluindeksiss‰ 2: pelaajan toisen Kaden kopio tai <tt>null</tt></li>
     * </ul>
     */
    public Kasi[] annaKaikkiKadet() {
         Kasi[] kasitaulu = new Kasi[3];
              kasitaulu[0] = new Kasi(this.jakajanKasi);
              kasitaulu[1] = new Kasi(this.pelaajanKasi_1);
              kasitaulu[2] = new Kasi(this.pelaajanKasi_2);
         return kasitaulu;
    }

    /**
     * Hakee vinkkitaulukoista sopivat vinkin pelitilanteeseen.
     * Osaa hakea erikseen vinkin pelaajille 1 ja 2 vuoron
     * perusteella, mutta ei hae vinkki‰ ellei ole jomman kumman
     * pelaajan vuoro.
     *
     * @return Tekstimuotoinen vinkki:
     * <br><ul>
     * <li>"OTA", jos vuorossa olevan pelaajan on edullista ottaa kortti</li>
     * <li>"Jƒƒ", jos vuorossa olevan pelaajan on edullista j‰‰d‰ pistelukuunsa</li>
     * <li>"TUPLAA/OTA", jos vuorossa olevan pelaajan on edullista tuplata (jos rahat
     * eiv‰t riit‰, kannattaa ottaa kortti)</li>
     * <li>"JAA PELI", jos vuorossa olevan pelaajan on edullista jakaa pelins‰</li>
     * <li>"", jos vuoro ei ole pelaajalla tai osapuolilla ei ole tarpeeksi kortteja
     * j‰rkev‰n vinkin antamiseen</li>
     * <li>muu, Vinkki-luokan oma virheilmoitus</li>
     * </ul>
     */
    public String haeVinkki() {
         Kasi pelaaja;
         if (this.vuoro == 1 || this.vuoro == 2)     //Kaden 1 vuoro
              pelaaja = new Kasi(this.pelaajanKasi_1);
         else if (this.vuoro == 3)                  //Kaden 2 vuoro
              pelaaja = new Kasi(this.pelaajanKasi_2);
         else return ""; //Jos vuoro on muualla kuin pelaajalla, vinkkia ei ole jarkevaa antaa.


         String vinkki = Vinkki.annaVinkki(pelaaja, new Kasi(this.jakajanKasi));
         if (vinkki == null)
              return "INDEKSIVIRHE";
         else if (vinkki.equals("H"))
              return "OTA";
         else if (vinkki.equals("S"))
              return "Jƒƒ";
         else if (vinkki.equals("D"))
              return "TUPLAA/OTA";
         else if (vinkki.equals("SP"))
              return "JAA PELI";
         else return vinkki;    //Tassa voi tulla Vinkki-luokasta "" tai jokin virheilmoitus
    }




    /**
     * Vaihtaa vuoron seuraavalle osapuolelle. Toimii kuten "j‰‰ pistelukuun".
     * Ensimm‰inen vaihto vuorosta 0 vuoroon 1 (pelaaja) tapahtuu metodin
     * alkukortit() avulla. Muuten k‰ytet‰‰n vuoron vaihtoon aina t‰t‰ metodia,
     * kunnes vuoro on jo -1 (pelikierros on loppu).<p>
     * Kun metodia k‰ytet‰‰n, Pelikierrosta k‰ytt‰v‰n luokan on annettava toinen
     * kortti vuoroon tulevalle pelaajalle 1, pelaajalle 2 tai jakajalle. Koska
     * Pelikierros itsekin k‰ytt‰‰ t‰t‰ metodia, Pelikierrosta k‰ytt‰v‰ss‰ metodissa
     * on tarkkailtava vuoron vaihtumista joka kerta kun nostetaan kortteja.
     */
    public void vaihdaVuoroa() {
         if (this.vuoro == 1) {
              if (this.erikoistoiminto == 2) //Peli on juuri jaettu
                   this.vuoro = 2;  //Vuoro ekalle jaetulle kadelle
              else 
                   this.vuoro = 4; //Vuoro jakajalle
              }
         else if (this.vuoro == 2) //Jaetun pelin 1 jalkeen vuoro siirtyy Jaettu peli 2:lle.
              this.vuoro = 3;
         else if (this.vuoro == 3) //Kun JaettuPeli_2 paattyy, tulee jakajan vuoro.
              this.vuoro = 4;
         else {
              this.vuoro = -1;      //Jakajakin lopetti, vuoro paattyy.
              this.laskeUusiSaldo();
         }
    }


    /**
     * Antaa Kortin vuorossa olevalle Pelikierroksen osapuolelle. Jos kortinotto
     * onnistuu, palautetaan true, muuten palautetaan false.<p>
     * Jos pelaaja saa Blackjackin tai menee yli, vuoro vaihtuu seuraavalle
     * osapuolelle automaattisesti.<p>
     * Jos vuorossa on jakaja, metodi palauttaa true tai false sen mukaan,
     * onko jakajan otettava viel‰ lis‰kortteja. Pelikierrosta k‰ytt‰v‰n
     * luokan riitt‰‰ siis pist‰‰ jakajan kortinotto while(lisaa())-luuppiin.
     *
     * @return Kun vuoro on virheellinen (peli alussa 0, kierros ohi -1 tai 
     * virheellinen 5), palautetaan false.<p>
     * Vuoro pelaajalla (vuoro 1, 2 tai 3)
     * <br><ul>
     * <li><tt>True</tt>, jos pelaajan kortinotto onnistui</li>
     * <li><tt>False</tt>, jos kortinotto ep‰onnistui (pakka loppui tai Kasi on jo t‰ynn‰ Kortteja)</li>
     * </ul>
     * <p>
     * Vuoro jakajalla (vuoro 4):
     * <br><ul>
     * <li><tt>True</tt>, jos kortinotto onnistui ja jakajan on otettava viel‰ ainakin yksi kortti</li>
     * <li><tt>False</tt>, jos jakaja ei saa ottaa en‰‰ kortteja</li>
     * <li><tt>False</tt>, jos jakajan kortinotto ep‰onnistui</li>
     * </ul>
     */
    public boolean lisaa() {
         if (this.vuoro == 1 || this.vuoro == 2) {
              boolean onnistuiko;
              onnistuiko = this.pelaajanKasi_1.otaKortti();
              
              if (this.vuoro == 1 && this.pelaajanKasi_1.onkoBJ() ) //Aito Blackjack!
                   this.vaihdaVuoroa();   //Seuraavaksi jakajan vuoro.
              if (this.pelaajanKasi_1.kadenArvo() > 21 ) //yli meni!
                   this.vaihdaVuoroa();   //Seuraavaksi pelaaja_2:n tai jakajan vuoro.

              //Pelaaja oli jakanut kaksi assaa. Silloin vain yksi lisakortti.
              if (this.vuoro == 2 && this.pelaajanKasi_1.montakoKorttia() == 2 &&
                                     this.pelaajanKasi_1.katsoKortti(1).ARVO == 1)
                   this.vaihdaVuoroa();

              return onnistuiko;
         } 
         else if (this.vuoro == 3) {
              boolean onnistuiko;
              onnistuiko = this.pelaajanKasi_2.otaKortti();
              if (this.pelaajanKasi_2.kadenArvo() > 21)
                   this.vaihdaVuoroa(); //Vuoro jakajalle.

              //Pelaaja oli jakanut kaksi assaa. Silloin vain yksi lisakortti.
              if (this.pelaajanKasi_2.montakoKorttia() == 2 &&
                  this.pelaajanKasi_2.katsoKortti(1).ARVO == 1) {
                   this.vaihdaVuoroa(); //Vuoro on seuraavaksi jakajalla.                
              }

              return onnistuiko;
         }
         else if (this.vuoro == 4) {
              if ( !Saannot.ottaakoJakajaKortin(this))
                   return false; //Jakajalle ei tule enaa kortteja! Summa on jo 17 tai yli!

              boolean onnistuiko = this.jakajanKasi.otaKortti();
              if (onnistuiko == false) { //Kortinnosto meni pieleen.
                   System.out.println("VIRHE: Jakajan kortinotto epaonnistui! Pakka lopussa? Kasi taynna?");
                   return false;
              }

              return Saannot.ottaakoJakajaKortin(this); //Kertoo onko otettava viela lisaa!
         }                                         
         else
              return false;
    }


    /**
     * Pelikierroksen tuplaus. Metodi tarkistaa onko tuplaus mahdollista ja
     * tekee tarvittavat muutokset peliin. Pelikierrosta k‰ytt‰v‰n luokan on
     * t‰m‰n j‰lkeen otettava yksi kortti t‰lle pelaajalle. Kyseinen pelaaja
     * lopettaa pelins‰ heti sen j‰lkeen.
     *
     * @return <tt>True</tt>, jos tuplaaminen onnistui
     *     <br><tt>False</tt>, jos tuplaaminen ei ollut s‰‰ntˆjen mukaan mahdollista (esim. rahatilanne)
     */
    public boolean tuplaa() {
         if ( !Saannot.voikoPelaajaTuplataKaden(this) )
              return false;

         this.rahaa = this.rahaa - this.panos;
         this.panos = 2 * this.panos;
         this.erikoistoiminto = 1;             //tuplaus
         return true;
    }



    /**
     * Pelin jakaminen. Metodi tarkistaa, onko pelin jakaminen mahdollista ja
     * tekee tarvittavat muutokset peliin. Molemmilla pelik‰sill‰ on nyt
     * saman suuruiset panokset. Pelikierrosta k‰ytt‰v‰n luokan on
     * t‰m‰n j‰lkeen vaihdettava vuoroa ja otettava yksi kortti seuraavalle
     * pelaajalle.
     *
     * @return <tt>True</tt>, jos pelin jakaminen onnistui
     *     <br><tt>False</tt> jos pelin jakaminen ei ollut s‰‰ntˆjen mukaan mahdollista (esim. rahatilanne)
     *
     * @exception Exception Heitet‰‰n, jos jaaPeli yritti tunkea Kasi-olioon 'null'-arvoa
     * Kortti-olion asemesta.
     */
    public boolean jaaPeli() throws Exception{
         if ( !Saannot.voikoPelaajaJakaaKaden(this) ) 
              return false;

         this.rahaa = this.rahaa - this.panos; //this.rahaa muuttuu, mutta this.panos on
                                               //molemmille peleille yhteinen eik‰ sit‰ tarvitse muuttaa.
         this.erikoistoiminto = 2; //peli jaettu
         
         //Pelaaja_1:n Kaden kaksi korttia jaetaan kahden uuden pelik‰den kesken.
         this.pelaajanKasi_2 = new Kasi(this.pakka, this.pelaajanKasi_1.katsoKortti(2));
         this.pelaajanKasi_1 = new Kasi(this.pakka, this.pelaajanKasi_1.katsoKortti(1));
         return true;
    }



    /**
     * Pelikierroksen vakuuttaminen. Metodi tarkistaa onko vakuuttaminen mahdollista ja
     * tekee tarvittavat muutokset peliin.
     *
     * @return <tt>True</tt>, jos vakuuttaminen onnistui
     *     <br><tt>False</tt> jos vakuuttaminen ei ollut s‰‰ntˆjen mukaan mahdollista (esim. rahatilanne)
     */
    public boolean vakuuta() {         //Peli jatkuu vakuuttamisen j‰lkeen l‰hes normaalisti.
         if ( !Saannot.voikoPelaajaVakuuttaaKaden(this) ) 
              return false;

         this.rahaa = this.rahaa - (this.panos / 2); //vakuutuspanoksen v‰hent‰minen
         this.erikoistoiminto = 3;
         return true;
    }


    /**
     * Jakaa Pelikierroksen alkuun ensimm‰iset alkukortit. Ennen kuin
     * peli voi alkaa, on jaettava yksi kortti pelaajalle, jakajalle ja
     * viel‰ toinen pelaajalle. T‰t‰ metodia kutsutaan siis kolmasti
     * heti uuden pelikierroksen ilmentym‰n alkuun. Vasta kun tarvittavat
     * kolme korttia on asetettu, muut toiminnot tulevat mahdollisiksi kun
     * vuoro==1 (pelaaja).
     *
     * @return <tt>True</tt>, jos alkukortteja on jaettava viel‰ ainakin yksi
     *     <br><tt>False</tt>, jos alkukortteja ei en‰‰ tarvita.
     */
    public boolean alkukortit() { 
         if (this.pelaajanKasi_1.montakoKorttia() == 0) {
              this.pelaajanKasi_1.otaKortti();
              return true;
         }
         else if (this.jakajanKasi.montakoKorttia() == 0) {
              this.jakajanKasi.otaKortti();
              return true;
         }
         else if (this.pelaajanKasi_1.montakoKorttia() == 1) {
              this.vuoro = 1;
              this.lisaa();    //pelaajan toinen kortti! (lisaa()-metodin Blackjack-tarkistus!)
              return false;
         }
         return false;        
    }


    /**
     * Palauttaa pelikierroksen lopputuloksen, kun vertaillaan pelaajan
     * ensimm‰ist‰ k‰tt‰ ja jakajan k‰tt‰. Kutsutaan vasta kun pelikierros
     * on ohi (vuoro == -1).
     *
     * @return <ul>
     * <li>-1, jos lopputulosta ei voitu selvitt‰‰ n‰iden Kasien pohjalta</li>
     * <li>0, pelaajan Kaden puhdas tappio</li>
     * <li>1, pelaajan Kaden puhdas voitto</li>
     * <li>2, tasapeli (jakaja voittaa)</li>
     * <li>3, Blackjack-tappio (pelaajalle normaali tappio)</li>
     * <li>4, pelaajan Blackjack-voitto (1,5-kertainen voitto)</li>
     * <li>5, Blackjack-tasapeli, panokset palautetaan</li>
     * <li>6, pelaajan vakuutusvoitto (koska pelaaja h‰visi panoksensa, h‰n
     * p‰‰see takaisin omilleen)</li>
     * </ul>
     */
    public int lopputulosPelaaja_1() {
         return this.lopputulos(this.pelaajanKasi_1);
    }

    /**
     * Katso lopputulosPelaaja_1(), mutta pelaajan toisen k‰den kannalta
     *
     * @return Katso lopputulosPelaaja_1()
     */
    public int lopputulosPelaaja_2() {
         return this.lopputulos(this.pelaajanKasi_2);
    }

    /**
     * Metodi selvitt‰‰ pelaajan Kaden ja jakajan Kaden lopputuloksen.
     *
     * @return Katso lopputulosPelaaja_1()
     */
    private int lopputulos(Kasi pelaaja) {
         //N‰iden Kasien pohjalta ei voi laskea lopputulosta
         if (pelaaja == null || this.jakajanKasi == null)
              return -1;

         int pelaajanPisteet = pelaaja.kadenArvo();
         int jakajanPisteet = this.jakajanKasi.kadenArvo();
         
         //Pelaaja on mennyt yli
         if (pelaajanPisteet > 21) {
              //Pelaaja oli vakuuttanut pelins‰ ja jakajalla oli Blackjack
              if (this.erikoistoiminto == 3 && jakajanKasi.onkoBJ())
                   return 6; //Panos palautuu pelaajalle
                        
              return 0;       //Pelaaja meni yli ja havisi
         }

         //Onko pelaajalla aito Blackjack (peli‰ ei ole siis jaettu)
         if (pelaaja.onkoBJ() && this.erikoistoiminto != 2) {
              //Onko jakajallakin Blackjack?
              if (this.jakajanKasi.onkoBJ()) 
                   return 5;  //Molemilla Blackjack
              else return 4;  //Vain pelaajalla Blackjack.
                                             
         }
         //Pelaajalla ei ole Blackjackia
         else {
              if (jakajanPisteet > 21)         //Jakaja on mennyt yli
                   return 1;             
              if (this.jakajanKasi.onkoBJ()) { //Vain jakajalla on Blackjack.
                   return 3;
              }
              //Pisteluvut ovat samat
              if (pelaajanPisteet == jakajanPisteet) //Normaali tasapeli (luetaan pelaajan tappioksi)
                   return 2;            
              else if (pelaajanPisteet < jakajanPisteet) //Jakajan Kaden arvo on suurempi.
                   return 0;            
              else return 1;                       //Pelaajan Kasi oli suurempi kuin jakajan
         }
    }

    /**
     * Palauttaa reaaliaikaisen tiedon siit‰, paljonko rahaa pelaajalla on nyt taskussaan.
     * Pelin vakuuttamisesta, jakamisesta ja tuplaamisesta veloitetaan rahat v‰littˆm‰sti.
     *
     * @return Pelaajan rahavarojen suuruus
     */
    public int rahatilanne() {
         return this.rahaa;
    }

    /**
     * Palauttaa reaaliaikaisen tiedon siit‰, mik‰ on pelin panos. Jos peli on vakuutettu,
     * vakuutuspanosta ei ole laskettu panokseen mukaan. Jos peli on jaettu, panoksen suuruinen
     * summa on MOLEMMILLA k‰sill‰.
     *
     * @return Pelaajan panoksen suuruus ilman vakuutusta
     */
    public int panostilanne() {
         return this.panos;
    }

    /**
     * Metodi laskee pelaajalle uuden rahatilanteen pelikierroksen lopussa.
     * Kutsutaan vain kerran aivan pelin lopussa, samalla kun vuoro vaihtuu arvoon -1.
     * Jos peli on jaettu, lasketaan tulos molemmista k‰sist‰, muuten vain
     * ensimm‰isest‰.
     */
    private void laskeUusiSaldo() {
         if (this.erikoistoiminto == 2) //peli on jaettu
               this.rahaa = this.rahaa 
                            + this.kadenTuotto(this.pelaajanKasi_1) 
                            + this.kadenTuotto(this.pelaajanKasi_2);
         else 
              this.rahaa = this.rahaa + this.kadenTuotto(this.pelaajanKasi_1);
    }

    /**
     * Palauttaa rahasumman, joka annetusta panoksesta saatiin takaisin.
     * Tutkii annetun pelaajan ja jakajan v‰lisen pelin lopputulosta ja
     * palauttaa summan, joka tuli pˆyd‰lt‰ takaisin pelaajalle. Jos pelaaja
     * p‰‰see omilleen, palautuu siis "panos" verran rahaa. Tavallinen voitto
     * on vastaavasti 2 * "panos".
     *
     * @return Paljonko rahaa pˆyd‰lt‰ tuli takaisin pelaajalle
     */
    private int kadenTuotto(Kasi pelaaja) {
         int tulos = this.lopputulos(pelaaja);

         if (tulos == 0)      //tappio
              return 0; 
         else if (tulos == 1) //voitto
              return 2 * this.panos; 
         else if (tulos == 2) //tasapeli (pelaajan tappioksi)
              return 0;
         else if (tulos == 3) { //Blackjack-tappio
              if (this.erikoistoiminto == 3) //jakajalla BJ ja peli oli vakuutettu.
                   return 3* this.panos / 2; //Pelaaja paasee omilleen. (3 * panos/2 = panos + vakuutusmaksu)
              return 0;          //Pelaajan tappio.
         }
         else if (tulos == 4) //Blackjack-voitto
              return 5 * this.panos / 2;
         else if (tulos == 5) //Blackjack-tasapeli
              return this.panos;
         else if (tulos == 6) //Vakuutusvoitto (panos palautuu pelaajalle)
              return 3 * this.panos / 2;
         else {                //VIRHETILANNE!
              System.out.println("VIRHE: lopputuloskoodi ei ollut valilta 0 - 5!");    
              return this.panos; //Palautetaan panos varmuudeksi.
         }
    }

    /**
     * Pelikierroksen tilanne tekstimuotoisena.
     *
     * @return Pelikierroksen tilanne tekstimuotoisena
     */
    public String toString() {

              String apu;
              apu =       "\n\nJakaja:" + this.jakajanKasi;
              apu = apu + "\nPelaaja 1:" + this.pelaajanKasi_1;
              apu = apu + "\nPelaaja 2:" + this.pelaajanKasi_2;
              apu = apu + "\nErikois:" + this.erikoistoiminto +
                          "  vuoro:" + this.vuoro;
              return apu;
    }
}
