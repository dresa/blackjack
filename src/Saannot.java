/*
 * @(#)Saannot.java 29.4.2003
 *
 * Copyright 2003 Esa Junttila
 */

/**
 * Luokka on kirjastoluokka, joka tarkistaa mitkä Blackjackin toiminnot voidaan
 * suorittaa Pelikierroksen missäkin vaiheessa. Mukana myös määritykset
 * sille kuinka suurta pakkaa käytetään ja milloin pakka viimeistään
 * sekoitetaan. Myös pelin ohjeet löytyvät tästä luokasta.
 *
 * @author  Esa Junttila
 * @version 29.4.2003
 */
public final class Saannot {

    /** Kuinka montaa pakallista Kortteja Blackjack käyttää Korttipakassaan*/
    public static final int PAKALLISIA_PELISSA = 6;

    /** Missä vaiheessa pakka viimeistään sekoitetaan (paljonko jäljellä alkuperäisestä koosta)*/
    private static final double SEKOITUSRAJA = 0.25;  
    
    /** Ei ilmentymiä*/
    private Saannot() {}

    /**
     * Onko kortin vetäminen pelaajalle nyt sallittua. Toimii
     * tarkistuksena kortinvetometodille ja käyttöliittymälle.
     *
     * @param peli   Pelikierros, jonka kortinvetomahdollisuuksia tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos vuoro on jommalla kummalla pelaajalla ja kortin
     * veto on tässä tilanteessa järkevää.</li>
     * <li><tt>False</tt>, jos vuoro ei ole pelaajalla tai kortinveto ei ole nyt järkevää
     * tai Pelitilanne hoitaa kierroksen loppuun automaattisesti.</li></ul>
     */
    public static boolean voikoPelaajaOttaaKortin(Pelikierros peli) {
         if (peli == null)
              return false;

         Kasi pelaaja_1 = peli.annaKaikkiKadet()[1];
         Kasi pelaaja_2 = peli.annaKaikkiKadet()[2];

         //Ensimmaisen Kaden vuoro
         if (peli.kenenVuoro() == 1 || peli.kenenVuoro() == 2) {
              if (pelaaja_1 == null)
                   return false;
              
              //Tuplauksessa pelaaja ei enaa vaikuta pelin kulkuun
              if (peli.kerroErikoistoiminto() == 1)
                   return false;
              
              //Aito Blackjack (jaetussa pelissä ei ole Blackjackia)              
              if (peli.kerroErikoistoiminto() == 0 && pelaaja_1.onkoBJ() )
                   return false;
              
              //Pelaaja on mennyt yli.
              if (pelaaja_1.kadenArvo() > 21)
                   return false;
              
              //Pelaaja jakoi kaksi assaa eika saa enempaa kuin yhden lisakortin.
              if (peli.kerroErikoistoiminto() == 2    &&
                  pelaaja_1.montakoKorttia() == 2     &&
                  pelaaja_1.katsoKortti(1).ARVO == 1)
                   return false;
         }
         //Toisen Kaden vuoro
         else if (peli.kenenVuoro() == 3) {
              if (pelaaja_2 == null)
                   return false;
              
              //Pelaaja on mennyt yli.
              if (pelaaja_2.kadenArvo() > 21)
                   return false;

              //Pelaaja jakoi kaksi assaa eika saa enempaa kuin yhden lisakortin.
              if (peli.kerroErikoistoiminto() == 2    && //jaettu
                  pelaaja_2.montakoKorttia() == 2     && //kaksi korttia
                  pelaaja_2.katsoKortti(1).ARVO == 1)    //jaettu kaksi assaa
                   return false;

         }
         else
              return false; //Kortin ottaminen on sallittua vain pelaajan vuoroilla

         return true; //Kortin ottaminen on sallittua


    }

    /**
     * Onko pisteisiin jääminen nyt sallittua pelaajalle. Toimii
     * tarkistuksena käyttöliittymälle.
     *
     * @param peli   Pelikierros, jonka pisteisiinjäämismahdollisuuksia tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos vuoro on jommalla kummalla pelaajalla ja pelaajalla
     * on ainakin kaksi korttia.</li>
     * <li><tt>False</tt>, jos vuoro ei ole pelaajalla tai pelaaja on jo mennyt yli tai
     * Pelikierros hoitaa kierroksen loppuun automaattisesti.</li></ul>
     */
    public static boolean voikoPelaajaJaada(Pelikierros peli) {
         if (peli == null)
              return false;

         Kasi pelaaja_1 = peli.annaKaikkiKadet()[1];
         Kasi pelaaja_2 = peli.annaKaikkiKadet()[2];
         
         //Kun peli on tuplattu, pelaaja ei enaa vaikuta pelin kulkuun.
         if (peli.kerroErikoistoiminto() == 1)
              return false;

         int vuoro = peli.kenenVuoro();

         //Pelaajan 1 vuoro
         if (vuoro == 1 || vuoro == 2) {
              if (pelaaja_1 == null)
                   return false;
              return (pelaaja_1.montakoKorttia() >= 2);
         }
         //Pelaajan 2 vuoro
         else if (vuoro == 3) {
              if (pelaaja_2 == null)
                   return false;
              return (pelaaja_2.montakoKorttia() >= 2);
         }
         else 
              return false;
    }


    /**
     * Onko tuplaaminen nyt sallittua pelaajalle. Toimii
     * tarkistuksena tuplausmetodille ja käyttöliittymälle.
     *
     * @param peli   Pelikierros, jonka tuplausmahdollisuuksia tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos säännöt sallivat tuplauksen pelaajalle 1.</li>
     * <li><tt>False</tt>, jos säännöt kieltävät tuplauksen tai rahat eivät riitä</li></ul>
     */
    public static boolean voikoPelaajaTuplataKaden(Pelikierros peli) {
         if (peli == null)
              return false;

         //Vuoron tulee olla pelaajalla 1.
         if (peli.kenenVuoro() != 1)
              return false;

         Kasi pelaaja = peli.annaKaikkiKadet()[1]; //Pelaajan 1 Kasi
         if (pelaaja == null)
              return false;

         int raakapisteet = pelaaja.kadenArvo(true);   //raakapisteet

         if ( peli.kerroErikoistoiminto() == 0 && //Erikoistoimintoja ei ole viela tehty.
              pelaaja.montakoKorttia() == 2    && //Pelaajalla on tasan kaksi korttia kadessa.
              !pelaaja.onkoBJ()                && //Kasi ei saa olla Blackjack!
              ( raakapisteet == 9 || raakapisteet == 10 || raakapisteet == 11 ) &&
              peli.rahatilanne() >= peli.panostilanne()                       ) // Rahat riittavat.
              
              return true; //kaikki oli OK!

         return false;
    }



    /**
     * Onko pelin jakaminen nyt sallittua pelaajalle. Toimii
     * tarkistuksena tuplausmetodille ja käyttöliittymälle.
     *
     * @param peli   Pelikierros, jonka pelinjakomahdollisuuksia tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos säännöt sallivat pelin jaon pelaajalle 1.</li>
     * <li><tt>False</tt>, jos säännöt kieltävät pelin jaon tai rahat eivät riitä</li></ul>
     */
    public static boolean voikoPelaajaJakaaKaden(Pelikierros peli) {
         if (peli == null)
              return false;

         Kasi pelaaja = peli.annaKaikkiKadet()[1]; //Pelaajan 1 Kasi
         if (pelaaja == null)
              return false;

         //Kortteja on oltava tasan kaksi
         if (pelaaja.montakoKorttia() != 2)
              return false;
         
         if ( peli.kerroErikoistoiminto() == 0          && //Erikoistoimintoja ei ole viela tehty.
              peli.kenenVuoro() == 1                    && //Vuoro on pelaajalla 1.
              peli.rahatilanne() >= peli.panostilanne() && //rahat riittavat.
              (pelaaja.katsoKortti(1).ARVO == pelaaja.katsoKortti(2).ARVO || //Kahden kortin pisteluvut ovat samat. (tama estaa myos Blackjackin)
               pelaaja.katsoKortti(1).ARVO >= 10 && pelaaja.katsoKortti(2).ARVO >= 10) )
              return true; //kaikki OK!
         return false;
    }


    /**
     * Onko pelin vakuuttaminen nyt sallittua pelaajalle. Toimii
     * tarkistuksena vakuutusmetodille ja käyttöliittymälle.
     *
     * @param peli   Pelikierros, jonka vakuuttamismahdollisuuksia tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos säännöt sallivat pelin vakuuttamisen pelaajalle 1.</li>
     * <li><tt>False</tt>, jos säännöt kieltävät pelin vakuuttamisen tai rahat eivät riitä</li></ul>
     */
    public static boolean voikoPelaajaVakuuttaaKaden(Pelikierros peli) {
         if (peli == null)
              return false;
         
         Kasi  jakaja = peli.annaKaikkiKadet()[0]; //Jakajan Kasi
         Kasi pelaaja = peli.annaKaikkiKadet()[1]; //Pelaajan 1 Kasi
       
         if (jakaja == null || pelaaja == null)
              return false;

         //Jakajalla on oltava tasan yksi kortti.       
         if (jakaja.montakoKorttia() != 1)  
              return false;
    
         if ( peli.kerroErikoistoiminto() == 0  && //Erikoistoimintoja ei ole viela tehty.
              peli.kenenVuoro() == 1            && //Vuoro on pelaajalla 1.
              pelaaja.montakoKorttia() == 2     && //Pelaajalla on kaksi korttia.
              !pelaaja.onkoBJ()                 && //Pelaajalla ei saa olla Blackjackia.
              jakaja.katsoKortti(1).ARVO == 1   && //Jakajan ainoa kortti on assa.
              peli.rahatilanne() >= peli.panostilanne() / 2  ) // Rahat riittavat.
              return true; //kaikki OK!

         return false;
    }


    /**
     * Onko jakajan otettava kortti tässä pelitilanteessa. Toimii
     * tarkistuksena jakajan omalle kierrokselle.
     *
     * @param peli   Pelikierros, jonka tilannetta tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos säännöt pakottavat ottamaan kortin.</li>
     * <li><tt>False</tt>, jos säännöt kieltävät ottamasta korttia.</li></ul>
     */
    public static boolean ottaakoJakajaKortin(Pelikierros peli) {
         if (peli == null)
              return false;

          //Vain jakajan vuorolla.
          if (peli.kenenVuoro() != 4)
              return false;
         
         Kasi[] kasitaulu = peli.annaKaikkiKadet();
         Kasi jakaja = kasitaulu[0];
         Kasi pelaaja_1 = kasitaulu[1];
         Kasi pelaaja_2 = kasitaulu[2];

         if (jakaja == null || pelaaja_1 ==null)
              return false;                     
         
         // PELI ON JAETTU:
         if (peli.kerroErikoistoiminto() == 2) {
              if (pelaaja_2 == null)
                   return false;
              //Molemmat menivat yli --> ei kortteja jakajalle
              if (pelaaja_1.kadenArvo() > 21 && pelaaja_2.kadenArvo() > 21)
                   return false;

         }
         //PELIA EI OLE JAETTU:
         else {
              //Pelaaja meni yli
              if (pelaaja_1.kadenArvo() > 21) {
                   // Jos pelaaja oli vakuuttanut pelinsä, on selvitettävä,
                   // oliko jakajalla Blackjack.
                   if (peli.kerroErikoistoiminto() == 3 && //peli oli vakuutettu
                           jakaja.montakoKorttia() == 1)   //jakajalla oli vain assa esilla
                        return true; // ---> (vain) yksi kortti lisää

                   return false; // peli ei ollut vakuutettu --> ei korttia jakajalle
              }
              
              //Jakaja ei voi vastata pelaajan Blackjackiin --> ei kortteja jakajalle
              if (pelaaja_1.onkoBJ() && !jakaja.onkoBJMahdollinen())
                   return false;
         }
         // Jos jakajan pistemäärä on jo 17 tai yli, lisäkortteja ei saa ottaa
         if (jakaja.kadenArvo() >= 17)
              return false;

         return true; //Jakajan on otettava kortti.
    }

    /**
     * Kertoo, onko pakasta kulunut jo liikaa kortteja ja olisi sekoituksen aika.
     * Metodia on syytä käyttää, ettei koskaan kävisi niin, että Kortit loppuisivat pakasta
     * kesken kaiken. Metodi ottaa varman päälle ja ehdottaa uudelleensekoittamista
     * heti kun teoriassa Kortit voivat loppua kesken. On syytä huomata, että
     * metodia tulee muuttaa heti jos korttien määrä pakassa on jotakin muuta kuin 52!
     *
     * @param pakka Korttipakka, jonka sekoittamistarvetta tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos Korttipakassa on alle SEKOITUSRAJAn verran
     * kortteja verattuna alkuperäiseen määrään tai teoreettinen maksimi
     * pienillä pakllisilla iskee vastaan.</li> <li><tt>False</tt>, jos Korttipakalla on
     * täysin turvallista pelata vielä ainakin yksi kierros.
     */
//-------------------------------------------------------
    public static boolean pitaakoPakkaSekoittaa(Korttipakka pakka) {
         if (pakka == null) {
              return false;
         }
         
         int maxKortteja = pakka.maxKorttejaPakassa();
         int kortteja = pakka.korttejaPakassa();
         
         if (maxKortteja == -1) //ikuista pakkaa ei tarvitse sekoittaa
              return false;

         // montako pakallista Korttipakassa on Kortteja
         int montakoPakallista = maxKortteja / (Kortti.SALLITUTMAAT.length * Kortti.SALLITUTARVOT.length);
         
         //Teoreettiset kierroksen tarvitsemat korttimäärät
         if (montakoPakallista <= 4) {
              if (montakoPakallista == 0)
                   return false;
              else if (montakoPakallista == 1 && kortteja >= 25)
                   return false;
              else if (montakoPakallista == 2 && kortteja >= 30)
                   return false;
              else if (montakoPakallista == 3 && kortteja >= 35)
                   return false;
              else if (montakoPakallista == 4 && kortteja >= 40)
                   return false;
              
              return true;
         }
         else
              return (1.0*kortteja/maxKortteja) < SEKOITUSRAJA;
    }

    /**
     * Blackjackin säännöt ja ohjelman käyttöohjeet. Kaikki yksityiskohdat
     * kirjattu palautettavaksi yhtenä String-oliona.
     *
     * @return Kaikki Blackjackin säännöt ja ohjelman koko käyttöohje yhdessä paketissa
     */
    public static String annaOhjeteksti() {
         return
         "YLEISTÄ" +
         "\n*****\n" +
         "Blackjackissa on kaksi osapuolta: jakaja ja pelaaja." +
         " Pelissä käytetään kuuden normaalin korttipakan (52 korttia) kokoista pelipakkaa," +
         " joka sekoitetaan kun siitä on noin neljännes jäljellä. Sekä jakaja että pelaaja" +
         " käyttävät molemmat samaa pakkaa. Pelikierroksen alussa pelaaja asettaa haluamansa" +
         " suuruisen panoksen pelipöydälle (kahdella jaollinen epänegatiivinen kokonaisluku)." +
         " Jakaja vastaa automaattisesti panokseen ja jakaa kummallekin yhden kortin oikein" +
         " päin. Kun jakaja jakaa pelaajalle vielä toisen kortin, pelivuoro on pelaajalla." +
         "\n\n" +
         "PELAAJAN VUORO" +
         "\n*****\n" +
         "Pelaaja yrittää kortteja ottamalla päästä mahdollisimman lähelle pistelukua 21." +
         " Kortin pisteluku on sama kuin kortin arvo ja käden pisteluku on korttien" +
         " pisteluvun summa. Kuvakortit ovat kuitenkin kaikki arvoltaan 10 ja ässä pelaajan" +
         " tahdon mukaan joko 1 tai 11. Pelaajan kaikki kortit yhdessä" +
         " muodostavat pelaajan käden, ja jos käden pisteluku ylittää 21, pelaaja häviää" +
         " panoksensa välittömästi. Pelaaja voi milloin tahansa lopettaa korttien ottamisen," +
         " jolloin vuoro siirtyy jakajalle." +
         "\n\n" +
         "JAKAJAN VUORO" +
         "\n*****\n" +
         "Kun pelaaja on lopettanut vuoronsa eikä ylittänyt pistelukua 21, jakaja jakaa" +
         " itselleen toisen kortin. Jakajan on jatkettava korttien ottamista niin kauan," +
         " kunnes jakajan käden pisteluku on 17 tai yli. Kun tämä on saavutettu," +
         " selvitetään kierroksen voittaja." +
         "\n\n" +
         "KIERROKSEN LOPPUTULOS" +
         "\n*****\n" +
         "Jos pelaajan pisteluku oli yli 21, hän hävisi heti eikä jakajan tarvinnut" +
         " ottaa yhtään lisäkorttia. Jos pelaaja jätti pelinsä pistelukuun 21 tai alle," +
         " vertaillaan pelaajan ja jakajan lopullisia pistelukuja. Jos jakaja meni" +
         " vuorollaan yli, pelaaja on voittanut ja hänen voittonsa on panoksen suuruus." +
         " Jos pelaajan pisteluku on pienempi kuin jakajan, pelaaja häviää panoksensa." +
         " Jos pelaajan pisteluku on suurempi kuin jakajan, pelaaja voittaa ja saa" +
         " panoksensa suuruisen voiton. Tasapelin sattuessa pelaajan tulkitaan hävinneen." +
         "\n\n" +
         "BLACKJACK-KÄSI" +
         "\n*****\n" +
         "Mikäli pelaaja tai jakaja onnistuu saamaan kahdella ensimmäisellä kortillaan" +
         " käden arvoksi 21, on kyseessä Blackjack. Mikäli pelaajalla on Blackjack," +
         " vuoro siirtyy automaattisesti jakajalle eikä pelaaja saa tehdä erikoistoimintoja." +
         " Jos jakajalla ei ole mahdollisuutta Blackjackin saamiseen, kierros päättyy." +
         " Blackjack voittaa tavallisen, useammalla kortilla saadun pisteluvun 21. Jos" +
         " pelaaja onnistuu saamaan Blackjack-voiton, hänelle maksetaan 1,5-kertainen" +
         " voitto. Jakajan Blackjack-voitto on pelaajalle aivan tavallinen tappio." +
         " Jos molemmat sekä pelaaja että jakaja saavat Blackjackin, peli päättyy" +
         " tasapeliin jossa panokset palautetaan pelaajalle." +
         "\n\n" +
         "ERIKOISTOIMINNOT" +
         "\n*****\n" +
         "Pelissä on kolme erikoistoimintoa, joista enintään yhden voi suorittaa" +
         " pelikierrosta kohden:" +
         "\n\n" +
         "* TUPLAUS: Jos pelaajalla on kädessään kaksi korttia ja käden pisteluku on 9," +
         " 10 tai 11, pelaajalla on mahdollisuus tuplata pelinsä. Pelaaja" +
         " kaksinkertaistaa panoksensa, ottaa yhden lisäkortin ja jää siihen." +
         "\n\n" +
         "* VAKUUTUS: Jos pelaajalla on kädessään kaksi korttia ja jakajan ainoa kortti" +
         " on ässä, pelaajalla on mahdollisuus vakuuttaa pelinsä jakajan" +
         " Blackjackin varalta. Vakuutuspanoksen suuruus on puolet kierroksella" +
         " olevan panoksen suuruudesta. Peli jatkuu pelaajan ja jakajan käsien" +
         " osalta normaalisti, mutta vakuutuspanoksen kohtalo selviää toisin:" +
         " Jos paljastui että jakajalla ei ollut Blackjackia, vakuutuspanos" +
         " menetetään; jos jakajalla todellakin oli Blackjack," +
         " vakuutuspanokselle maksetaan voittona 2*vakuutuspanos." +
         " Jälkimmäisessä tilanteessa pelaaja pääsee siis omilleen." +
         "\n\n" +
         "* PELIN JAKAMINEN: Jos pelaajalla on kädessään kaksi korttia, jotka ovat molemmat" +
         " pisteluvultaan samoja, pelaajalla on mahdollisuus jakaa" +
         " pelinsä. Pelaajan alkuperäisen käden kaksi korttia muodostavat" +
         " molemmat oman uuden kätensä, joihin toiseen on asetettava" +
         " samansuuruinen panos joka jo ensimmäisessä oli. Kumpikin näistä" +
         " peleistä mittelöi nyt itsenäisesti jakajaa vastaan. Ensin" +
         " pelataan käsi 1 loppuun ja sitten siirrytään käteen 2. Kun" +
         " käden vuoro tulee, sille jaetaan automaattisesti toinen kortti," +
         " ja tämän jälkeen pelaaja saa tehdä omat ratkaisunsa. Jaetuille" +
         " käsille ei voi tehdä lisää erikoistoimintoja ja jaetun käden" +
         " Blackjack luetaan normaaliksi pisteluvuksi 21. Jos pelaaja" +
         " jakaa kaksi ässää, lisäkortteja ei voi ottaa ja molemmat kädet" +
         " jäävät korttilukuun 2."
                   ;
    }
}