/*
 * @(#)Saannot.java 29.4.2003
 *
 * Copyright 2003 Esa Junttila
 */

/**
 * Luokka on kirjastoluokka, joka tarkistaa mitk� Blackjackin toiminnot voidaan
 * suorittaa Pelikierroksen miss�kin vaiheessa. Mukana my�s m��ritykset
 * sille kuinka suurta pakkaa k�ytet��n ja milloin pakka viimeist��n
 * sekoitetaan. My�s pelin ohjeet l�ytyv�t t�st� luokasta.
 *
 * @author  Esa Junttila
 * @version 29.4.2003
 */
public final class Saannot {

    /** Kuinka montaa pakallista Kortteja Blackjack k�ytt�� Korttipakassaan*/
    public static final int PAKALLISIA_PELISSA = 6;

    /** Miss� vaiheessa pakka viimeist��n sekoitetaan (paljonko j�ljell� alkuper�isest� koosta)*/
    private static final double SEKOITUSRAJA = 0.25;  
    
    /** Ei ilmentymi�*/
    private Saannot() {}

    /**
     * Onko kortin vet�minen pelaajalle nyt sallittua. Toimii
     * tarkistuksena kortinvetometodille ja k�ytt�liittym�lle.
     *
     * @param peli   Pelikierros, jonka kortinvetomahdollisuuksia tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos vuoro on jommalla kummalla pelaajalla ja kortin
     * veto on t�ss� tilanteessa j�rkev��.</li>
     * <li><tt>False</tt>, jos vuoro ei ole pelaajalla tai kortinveto ei ole nyt j�rkev��
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
              
              //Aito Blackjack (jaetussa peliss� ei ole Blackjackia)              
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
     * Onko pisteisiin j��minen nyt sallittua pelaajalle. Toimii
     * tarkistuksena k�ytt�liittym�lle.
     *
     * @param peli   Pelikierros, jonka pisteisiinj��mismahdollisuuksia tutkitaan
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
     * tarkistuksena tuplausmetodille ja k�ytt�liittym�lle.
     *
     * @param peli   Pelikierros, jonka tuplausmahdollisuuksia tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos s��nn�t sallivat tuplauksen pelaajalle 1.</li>
     * <li><tt>False</tt>, jos s��nn�t kielt�v�t tuplauksen tai rahat eiv�t riit�</li></ul>
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
     * tarkistuksena tuplausmetodille ja k�ytt�liittym�lle.
     *
     * @param peli   Pelikierros, jonka pelinjakomahdollisuuksia tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos s��nn�t sallivat pelin jaon pelaajalle 1.</li>
     * <li><tt>False</tt>, jos s��nn�t kielt�v�t pelin jaon tai rahat eiv�t riit�</li></ul>
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
     * tarkistuksena vakuutusmetodille ja k�ytt�liittym�lle.
     *
     * @param peli   Pelikierros, jonka vakuuttamismahdollisuuksia tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos s��nn�t sallivat pelin vakuuttamisen pelaajalle 1.</li>
     * <li><tt>False</tt>, jos s��nn�t kielt�v�t pelin vakuuttamisen tai rahat eiv�t riit�</li></ul>
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
     * Onko jakajan otettava kortti t�ss� pelitilanteessa. Toimii
     * tarkistuksena jakajan omalle kierrokselle.
     *
     * @param peli   Pelikierros, jonka tilannetta tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos s��nn�t pakottavat ottamaan kortin.</li>
     * <li><tt>False</tt>, jos s��nn�t kielt�v�t ottamasta korttia.</li></ul>
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
                   // Jos pelaaja oli vakuuttanut pelins�, on selvitett�v�,
                   // oliko jakajalla Blackjack.
                   if (peli.kerroErikoistoiminto() == 3 && //peli oli vakuutettu
                           jakaja.montakoKorttia() == 1)   //jakajalla oli vain assa esilla
                        return true; // ---> (vain) yksi kortti lis��

                   return false; // peli ei ollut vakuutettu --> ei korttia jakajalle
              }
              
              //Jakaja ei voi vastata pelaajan Blackjackiin --> ei kortteja jakajalle
              if (pelaaja_1.onkoBJ() && !jakaja.onkoBJMahdollinen())
                   return false;
         }
         // Jos jakajan pistem��r� on jo 17 tai yli, lis�kortteja ei saa ottaa
         if (jakaja.kadenArvo() >= 17)
              return false;

         return true; //Jakajan on otettava kortti.
    }

    /**
     * Kertoo, onko pakasta kulunut jo liikaa kortteja ja olisi sekoituksen aika.
     * Metodia on syyt� k�ytt��, ettei koskaan k�visi niin, ett� Kortit loppuisivat pakasta
     * kesken kaiken. Metodi ottaa varman p��lle ja ehdottaa uudelleensekoittamista
     * heti kun teoriassa Kortit voivat loppua kesken. On syyt� huomata, ett�
     * metodia tulee muuttaa heti jos korttien m��r� pakassa on jotakin muuta kuin 52!
     *
     * @param pakka Korttipakka, jonka sekoittamistarvetta tutkitaan
     *
     * @return <ul><li><tt>True</tt>, jos Korttipakassa on alle SEKOITUSRAJAn verran
     * kortteja verattuna alkuper�iseen m��r��n tai teoreettinen maksimi
     * pienill� pakllisilla iskee vastaan.</li> <li><tt>False</tt>, jos Korttipakalla on
     * t�ysin turvallista pelata viel� ainakin yksi kierros.
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
         
         //Teoreettiset kierroksen tarvitsemat korttim��r�t
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
     * Blackjackin s��nn�t ja ohjelman k�ytt�ohjeet. Kaikki yksityiskohdat
     * kirjattu palautettavaksi yhten� String-oliona.
     *
     * @return Kaikki Blackjackin s��nn�t ja ohjelman koko k�ytt�ohje yhdess� paketissa
     */
    public static String annaOhjeteksti() {
         return
         "YLEIST�" +
         "\n*****\n" +
         "Blackjackissa on kaksi osapuolta: jakaja ja pelaaja." +
         " Peliss� k�ytet��n kuuden normaalin korttipakan (52 korttia) kokoista pelipakkaa," +
         " joka sekoitetaan kun siit� on noin nelj�nnes j�ljell�. Sek� jakaja ett� pelaaja" +
         " k�ytt�v�t molemmat samaa pakkaa. Pelikierroksen alussa pelaaja asettaa haluamansa" +
         " suuruisen panoksen pelip�yd�lle (kahdella jaollinen ep�negatiivinen kokonaisluku)." +
         " Jakaja vastaa automaattisesti panokseen ja jakaa kummallekin yhden kortin oikein" +
         " p�in. Kun jakaja jakaa pelaajalle viel� toisen kortin, pelivuoro on pelaajalla." +
         "\n\n" +
         "PELAAJAN VUORO" +
         "\n*****\n" +
         "Pelaaja yritt�� kortteja ottamalla p��st� mahdollisimman l�helle pistelukua 21." +
         " Kortin pisteluku on sama kuin kortin arvo ja k�den pisteluku on korttien" +
         " pisteluvun summa. Kuvakortit ovat kuitenkin kaikki arvoltaan 10 ja �ss� pelaajan" +
         " tahdon mukaan joko 1 tai 11. Pelaajan kaikki kortit yhdess�" +
         " muodostavat pelaajan k�den, ja jos k�den pisteluku ylitt�� 21, pelaaja h�vi��" +
         " panoksensa v�litt�m�sti. Pelaaja voi milloin tahansa lopettaa korttien ottamisen," +
         " jolloin vuoro siirtyy jakajalle." +
         "\n\n" +
         "JAKAJAN VUORO" +
         "\n*****\n" +
         "Kun pelaaja on lopettanut vuoronsa eik� ylitt�nyt pistelukua 21, jakaja jakaa" +
         " itselleen toisen kortin. Jakajan on jatkettava korttien ottamista niin kauan," +
         " kunnes jakajan k�den pisteluku on 17 tai yli. Kun t�m� on saavutettu," +
         " selvitet��n kierroksen voittaja." +
         "\n\n" +
         "KIERROKSEN LOPPUTULOS" +
         "\n*****\n" +
         "Jos pelaajan pisteluku oli yli 21, h�n h�visi heti eik� jakajan tarvinnut" +
         " ottaa yht��n lis�korttia. Jos pelaaja j�tti pelins� pistelukuun 21 tai alle," +
         " vertaillaan pelaajan ja jakajan lopullisia pistelukuja. Jos jakaja meni" +
         " vuorollaan yli, pelaaja on voittanut ja h�nen voittonsa on panoksen suuruus." +
         " Jos pelaajan pisteluku on pienempi kuin jakajan, pelaaja h�vi�� panoksensa." +
         " Jos pelaajan pisteluku on suurempi kuin jakajan, pelaaja voittaa ja saa" +
         " panoksensa suuruisen voiton. Tasapelin sattuessa pelaajan tulkitaan h�vinneen." +
         "\n\n" +
         "BLACKJACK-K�SI" +
         "\n*****\n" +
         "Mik�li pelaaja tai jakaja onnistuu saamaan kahdella ensimm�isell� kortillaan" +
         " k�den arvoksi 21, on kyseess� Blackjack. Mik�li pelaajalla on Blackjack," +
         " vuoro siirtyy automaattisesti jakajalle eik� pelaaja saa tehd� erikoistoimintoja." +
         " Jos jakajalla ei ole mahdollisuutta Blackjackin saamiseen, kierros p��ttyy." +
         " Blackjack voittaa tavallisen, useammalla kortilla saadun pisteluvun 21. Jos" +
         " pelaaja onnistuu saamaan Blackjack-voiton, h�nelle maksetaan 1,5-kertainen" +
         " voitto. Jakajan Blackjack-voitto on pelaajalle aivan tavallinen tappio." +
         " Jos molemmat sek� pelaaja ett� jakaja saavat Blackjackin, peli p��ttyy" +
         " tasapeliin jossa panokset palautetaan pelaajalle." +
         "\n\n" +
         "ERIKOISTOIMINNOT" +
         "\n*****\n" +
         "Peliss� on kolme erikoistoimintoa, joista enint��n yhden voi suorittaa" +
         " pelikierrosta kohden:" +
         "\n\n" +
         "* TUPLAUS: Jos pelaajalla on k�dess��n kaksi korttia ja k�den pisteluku on 9," +
         " 10 tai 11, pelaajalla on mahdollisuus tuplata pelins�. Pelaaja" +
         " kaksinkertaistaa panoksensa, ottaa yhden lis�kortin ja j�� siihen." +
         "\n\n" +
         "* VAKUUTUS: Jos pelaajalla on k�dess��n kaksi korttia ja jakajan ainoa kortti" +
         " on �ss�, pelaajalla on mahdollisuus vakuuttaa pelins� jakajan" +
         " Blackjackin varalta. Vakuutuspanoksen suuruus on puolet kierroksella" +
         " olevan panoksen suuruudesta. Peli jatkuu pelaajan ja jakajan k�sien" +
         " osalta normaalisti, mutta vakuutuspanoksen kohtalo selvi�� toisin:" +
         " Jos paljastui ett� jakajalla ei ollut Blackjackia, vakuutuspanos" +
         " menetet��n; jos jakajalla todellakin oli Blackjack," +
         " vakuutuspanokselle maksetaan voittona 2*vakuutuspanos." +
         " J�lkimm�isess� tilanteessa pelaaja p��see siis omilleen." +
         "\n\n" +
         "* PELIN JAKAMINEN: Jos pelaajalla on k�dess��n kaksi korttia, jotka ovat molemmat" +
         " pisteluvultaan samoja, pelaajalla on mahdollisuus jakaa" +
         " pelins�. Pelaajan alkuper�isen k�den kaksi korttia muodostavat" +
         " molemmat oman uuden k�tens�, joihin toiseen on asetettava" +
         " samansuuruinen panos joka jo ensimm�isess� oli. Kumpikin n�ist�" +
         " peleist� mittel�i nyt itsen�isesti jakajaa vastaan. Ensin" +
         " pelataan k�si 1 loppuun ja sitten siirryt��n k�teen 2. Kun" +
         " k�den vuoro tulee, sille jaetaan automaattisesti toinen kortti," +
         " ja t�m�n j�lkeen pelaaja saa tehd� omat ratkaisunsa. Jaetuille" +
         " k�sille ei voi tehd� lis�� erikoistoimintoja ja jaetun k�den" +
         " Blackjack luetaan normaaliksi pisteluvuksi 21. Jos pelaaja" +
         " jakaa kaksi �ss��, lis�kortteja ei voi ottaa ja molemmat k�det" +
         " j��v�t korttilukuun 2."
                   ;
    }
}