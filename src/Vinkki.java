/*
 * @(#)Vinkki.java 29.4.2003
 *
 * Copyright 2003 Esa Junttila
 */



 /**
 * Luokka toimii vinkkikirjastona Blackjackin pelitilanteisiin.
 * Todennäköisyyslaskennalla valmiiksi lasketut taulukot antavat
 * oikean vinkin pelitilanteisiin. 
 *
 * @author  Esa Junttila
 * @version 29.4.2003
 */




public final class Vinkki{

    /** Kirjastoluokasta ei ole syytä tehdä ilmentymiä */
    private Vinkki() {}


    /**
     * Palauttaa pelitilanteeseen sopivan pelitavan String-muotoisena valmiiksi
     * laskettujen taulukoiden perusteella. Metodi ei kuitenkaan tunne pelaajan
     * rahatilannetta, joten se saattaa ehdottaa pelaajalle vaikkapa tuplausta
     * vaikka rahat eivät siihen riittäisikään. (metodi ei palauta insurancea eli
     * vakuutusta, koska se on yleensä epäedullinen pelata)
     *
     * @param  pelaajanKasi    Se pelaajan Kasi, jolle vinkkiä haetaan
     * @param  jakajanKasi     Mikä Kasi jakajalla on
     *
     * @return <ul><li>"H", jos pelaajan pitäisi ottaa lisäkortti</li>
     *             <li>"S", jos pelaajan pitäisi jäädä pistemääräänsä</li>
     *             <li>"SP", jos pelaajan pitäisi jakaa Katensa kahtia</li>
     *             <li>"D", jos pelaajan pitäisi tuplata pelinsä</li>
     *             <li>"", jos pelaajalle ei ole vihjettä tähän tilanteeseen. Esim.
     * pelaajalla on kortteja vähemmän kuin kaksi TAI jakajan korttien määrä poikkeaa yhdestä</li>
     *             <li>null, jos metodin taulukot ovat sotkeutuneet</li>
     *             <li>"VIRHE: [jotakin]", jos metodi toimii väärin</li></ul>
     */
    public static String annaVinkki(Kasi pelaajanKasi, Kasi jakajanKasi) {
         

         //Vinkit on taulukoitu siten, etta jakajan kortti vaihtuu vaakatasossa
         //ja pelaajan kortit pystytasossa. Pelaajan korttiyhdistelmasta saadaan
         //koordinaatti y ja jakajan kortista koordinaatti x. Oikea vinkki tahan
         //pelitilanteeseen loytyy sitten vastaavasta taulupaikasta.
         int x;
         int y;
//-------
         if (pelaajanKasi == null || jakajanKasi == null) {return "";} //Kasia ei ole olemassa
         if (jakajanKasi.montakoKorttia() != 1) { return "";} //Jakajalla vaara maara kortteja
         if (pelaajanKasi.montakoKorttia() < 2) {return "";}  //Pelaajalla vaara maara kortteja     
         if (pelaajanKasi.kadenArvo() > 21) { return "";}  //Pelaaja on jo mennyt yli

         Kortti jakaja = jakajanKasi.katsoKortti(1);//Tarkistettu jakajan kortti. Jos arvo>10 niin arvo=10
         if (jakaja.ARVO > 10)
              jakaja = Kortti.luoKortti(jakaja.MAA, 10);


         // TASTA ALKAA VARSINAINEN VINKIN ETSINTA:
         //----------------------------------------
         
//SOFT-KASI, ELI MUKANA ON "KAYTTAMATON" ASSA (assan arvo on siis kadessa 11, Kortti-oliossa 1)
         if (pelaajanKasi.onkoSoft()) {
              int korttienSumma = pelaajanKasi.kadenArvo(true) - 1;  //Kaden raakapisteet. Assa on laskettu arvoksi 1.
              //Otettiin se soft-assa pois laskuista niin ohjelmointi helpottuu (tuo vahennys -1)
              
              if (korttienSumma == 1) { return softtaulu(jakaja.ARVO -1, 1); }   //A1
              if (korttienSumma == 2) { return softtaulu(jakaja.ARVO -1, 2); }   //A2
              if (korttienSumma == 3) { return softtaulu(jakaja.ARVO -1, 3); }   //A3
              if (korttienSumma == 4) { return softtaulu(jakaja.ARVO -1, 4); }   //A4
              if (korttienSumma == 5) { return softtaulu(jakaja.ARVO -1, 5); }   //A5
              if (korttienSumma == 6) { return softtaulu(jakaja.ARVO -1, 6); }   //A6
              if (korttienSumma == 7) { return softtaulu(jakaja.ARVO -1, 7); }   //A7
              if (korttienSumma == 8) { return softtaulu(jakaja.ARVO -1, 8); }   //A8
              if (korttienSumma == 9) { return softtaulu(jakaja.ARVO -1, 9); }   //A9
              if (korttienSumma == 10) { return softtaulu(jakaja.ARVO -1, 10); } //VIRHE! (tama on blackjack) 
              return "VIRHE: softtaulu"; //VIRHE! Olisi pitanyt osua jo kohdalle! 
                                         //SOFT-kadessa ei ole muita vaihtoehtoja!
         }

//-------MUUTAMAT ERIKOISTAPAUKSET: KAKSI KORTTIA JA MOLEMMAT ARVOLTAAN SAMOJA
//----------------------------------------------------------------------------
         Kortti kortti_1 = pelaajanKasi.katsoKortti(1);
         if (kortti_1.ARVO > 10) { kortti_1 = Kortti.luoKortti(kortti_1.MAA, 10);} //Jos arvo > 10 niin asetetaan 10.
         Kortti kortti_2 = pelaajanKasi.katsoKortti(2);
         if (kortti_1.ARVO > 10) { kortti_1 = Kortti.luoKortti(kortti_1.MAA, 10);}

         if (pelaajanKasi.montakoKorttia() == 2 && kortti_1.ARVO == kortti_2.ARVO) {
              if (kortti_1.ARVO == 2) { return paritaulu(jakaja.ARVO -1, 0); }  //22       
              if (kortti_1.ARVO == 3) { return paritaulu(jakaja.ARVO -1, 1); }  //33       
              if (kortti_1.ARVO == 6) { return paritaulu(jakaja.ARVO -1, 2); }  //66       
              if (kortti_1.ARVO == 7) { return paritaulu(jakaja.ARVO -1, 3); }  //77       
              if (kortti_1.ARVO == 8) { return paritaulu(jakaja.ARVO -1, 4); }  //88       
              if (kortti_1.ARVO == 9) { return paritaulu(jakaja.ARVO -1, 5); }  //99       
              if (kortti_1.ARVO == 10) { return paritaulu(jakaja.ARVO -1, 6); } //1010        
         }

//-------LOPUT TILANTEET OVAT HARD-TAULUKOSSA:
//--------------------------------------------
         int korttienSumma = pelaajanKasi.kadenArvo();

         if (korttienSumma == 4) { return hardtaulu(jakaja.ARVO -1, 4); }   //4
         if (korttienSumma == 5) { return hardtaulu(jakaja.ARVO -1, 5); }   //5
         if (korttienSumma == 6) { return hardtaulu(jakaja.ARVO -1, 6); }   //6
         if (korttienSumma == 7) { return hardtaulu(jakaja.ARVO -1, 7); }   //7
         if (korttienSumma == 8) { return hardtaulu(jakaja.ARVO -1, 8); }   //8
         if (korttienSumma == 9) { return hardtaulu(jakaja.ARVO -1, 9); }   //9
         if (korttienSumma == 10) { return hardtaulu(jakaja.ARVO -1, 10); } //10
         if (korttienSumma == 11) { return hardtaulu(jakaja.ARVO -1, 11); } //11
         if (korttienSumma == 12) { return hardtaulu(jakaja.ARVO -1, 12); } //12
         if (korttienSumma == 13) { return hardtaulu(jakaja.ARVO -1, 13); } //13
         if (korttienSumma == 14) { return hardtaulu(jakaja.ARVO -1, 14); } //14
         if (korttienSumma == 15) { return hardtaulu(jakaja.ARVO -1, 15); } //15
         if (korttienSumma == 16) { return hardtaulu(jakaja.ARVO -1, 16); } //16
         if (korttienSumma == 17) { return hardtaulu(jakaja.ARVO -1, 17); } //17
         if (korttienSumma == 18) { return hardtaulu(jakaja.ARVO -1, 18); } //18
         if (korttienSumma == 19) { return hardtaulu(jakaja.ARVO -1, 19); } //19
         if (korttienSumma == 20) { return hardtaulu(jakaja.ARVO -1, 20); } //20
         if (korttienSumma == 21) { return hardtaulu(jakaja.ARVO -1, 21); } //21

         return "VIRHE: loppu"; //VIRHE! Kortteja vastaavaa vihjetta ei loytynyt vaikka olisi pitanyt!    
    }
//-------------------------------------------------------------------------------------------

    /**
     * Softtaulusta löytyy vihjeet tilanteisiin, joissa pelaajalla
     * on kädessään ässä, jonka arvo on 11.
     */
    private static String softtaulu(int x, int y) {
          // A, 2, 3, 4, 5, 6, 7, 8, 9 tai 10 on jakajalla nakyvilla
    //Hit, Stand, Double, SPlit,Insurance (ei ole taulukoissa)
    //kasikortit jossa on soft-assa
        String[][] softtaulu =
                          {
         {null,null,null,null,null,null,null,null,null,null},          //A0 VIRHE (mukana ohjelmoinnin helpottamiseksi)
         {"SP", "SP", "SP", "SP", "SP", "SP", "SP", "SP", "SP", "SP"}, //AA
         {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},           //A2
         {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},           //A3
         {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},           //A4
         {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},           //A5
         {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},           //A6
         {"H", "S", "S", "S", "S", "S", "S" ,"S", "H", "H"},           //A7
         {"S", "S", "S", "S", "S", "S", "S", "S", "S", "S"},           //A8
         {"S", "S", "S", "S", "S", "S", "S", "S", "S", "S"},           //A9
         {"S", "S", "S", "S", "S", "S", "S", "S", "S", "S"}            //A10 (tama on Blackjack) 
                           };
         return softtaulu[y][x];  //Taulukossa x ja y ovat eri toistensa paikoilla verrattuna vihjeiden asetteluun ruudulla
    }

//-------
    /**
     * Paritaulusta löytyy vihjeet tilanteisiin, joissa pelaajalla
     * on tasan kaksi kasikorttia ja molempien arvo on sama.
     */
    private final static String paritaulu(int x, int y) {
         // A, 2, 3, 4, 5, 6, 7, 8, 9 tai 10 on jakajalla nakyvilla

         String[][] paritaulu =
                        {
         {"H", "H", "H", "SP", "SP", "SP", "SP", "H", "H", "H"},       //22
         {"H", "H", "H", "SP", "SP", "SP", "SP", "H", "H", "H"},       //33
         {"H", "H", "SP", "SP", "SP", "SP", "H", "H", "H", "H"},       //66
         {"H", "SP", "SP", "SP", "SP", "SP", "SP", "H", "H", "H"},     //77
         {"SP", "SP", "SP", "SP", "SP", "SP", "SP", "SP", "SP", "SP"}, //88
         {"S", "SP", "SP", "SP", "SP", "SP", "S", "SP", "SP", "S"},    //99
         {"S", "S", "S", "S", "S", "S", "S", "S", "S", "S"}            //1010
                        };
         return paritaulu[y][x]; //Taulukossa x ja y ovat eri toistensa paikoilla verrattuna vihjeiden asetteluun ruudulla
    }


    /**
     * Ellei vinkkiä löydy soft- tai paritaulusta,
     * se haetaan viimeistään hardtaulusta.
     */
    private final static String hardtaulu(int x, int y) {     
          // A, 2, 3, 4, 5, 6, 7, 8, 9 tai 10 on jakajalla nakyvilla

         String[][] hardtaulu =
                        {
         {null,null,null,null,null,null,null,null,null,null},            //0 VIRHE
         {null,null,null,null,null,null,null,null,null,null},            //1 VIRHE
         {null,null,null,null,null,null,null,null,null,null},            //2 VIRHE
         {null,null,null,null,null,null,null,null,null,null},            //3 VIRHE
         {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},             //4 pienin mahdollinen Blackjack-arvo (2+2)
         {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},             //5
         {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},             //6
         {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},             //7
         {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},             //8
         {"H", "H", "D", "D", "D", "D", "H", "H", "H", "H"},             //9
         {"H", "D", "D", "D", "D", "D", "D", "D", "D", "H"},             //10
         {"H", "D", "D", "D", "D", "D", "D", "D", "D", "D"},             //11
         {"H", "H", "H", "S", "S", "S", "H", "H", "H", "H"},             //12
         {"H", "S", "S", "S", "S", "S", "H", "H", "H", "H"},             //13
         {"H", "S", "S", "S", "S", "S", "H", "H", "H", "H"},             //14
         {"H", "S", "S", "S", "S", "S", "H", "H", "H", "H"},             //15
         {"H", "S", "S", "S", "S", "S", "H", "H", "H", "H"},             //16
         {"S", "S", "S", "S", "S", "S", "S", "S", "S" ,"S"},             //17
         {"S", "S", "S", "S", "S", "S", "S", "S", "S" ,"S"},             //18
         {"S", "S", "S", "S", "S", "S", "S", "S", "S" ,"S"},             //19
         {"S", "S", "S", "S", "S", "S", "S", "S", "S" ,"S"},             //20
         {"S", "S", "S", "S", "S", "S", "S", "S", "S" ,"S"}              //21
                     };
         return hardtaulu[y][x];  //Taulukossa x ja y ovat eri toistensa paikoilla verrattuna vihjeiden asetteluun ruudulla
    }


    /** Testiohjelma */
	public static void main(String[] args) {
         Korttipakka pakka = new Korttipakka(6);
         Kasi pelaaja;
         Kasi jakaja;

         for (int i=0; i < 30; i++) {
              System.out.println();
              
              pelaaja = new Kasi(pakka);
              jakaja = new Kasi(pakka);
              
              jakaja.otaKortti();
              pelaaja.otaKortti();
              pelaaja.otaKortti();
              //Verrataan tulostettua vinkkiä vinkkitaulukkoon
              System.out.println( "Jakaja: " + jakaja +
                                  "\nPelaaja: " + pelaaja +
                                  "\nVinkki:" + annaVinkki(pelaaja, jakaja) );
         }

	}

}