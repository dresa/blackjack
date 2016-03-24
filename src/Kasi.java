/*
 * @(#)Kasi.java 29.4.2003
 *
 * Copyright 2003 Esa Junttila
 */



 /**
 * Luokan ilmentym‰t edustavat Blackjackin yht‰ korttik‰tt‰. Kasi voi sis‰lt‰‰
 * Kortti-olioita nollasta maksimim‰‰r‰‰n MAXKORTTEJAKƒDESSA (oletus 22). Kasi on
 * sama sek‰ pelaajille ett‰ jakajalle. Katt‰ ei voi tyhjent‰‰, sill‰
 * Blackjackissa k‰den tyhjent‰minen merkitsisi pelikierroksen p‰‰ttymist‰.
 *
 * @author  Esa Junttila
 * @version 29.4.2003
 */

public class Kasi{

    /** Ehdoton teoreettinen maksimi korttien m‰‰r‰lle Blackjack-pelin Kadessa */
    public static final int MAXKORTTEJAKADESSA = 22; 

    /** Kortti-olioiden m‰‰r‰ K‰dess‰ */
    private int kortteja = 0;

    /** Kaden yksitt‰isten Kortti-olioiden s‰ilytysrakenne */
    private Kortti[] kortit;

    /** Kaden k‰ytt‰m‰ Korttipakka */
    private Korttipakka pakka;   //Kaytossa oleva korttipakka


    /** 
     * Luo tyhj‰n Kaden, jossa on nolla Kortti-oliota valmiina. Kasi
     * k‰ytt‰‰ parametrina saatua Korttipakkaa.
     *
     * @param kaytettavaPakka      Mist‰ Korttipakasta Kasi nostaa Kortti-olionsa.
     */
    public Kasi(Korttipakka kaytettavaPakka) {
         this.kortit = new Kortti[MAXKORTTEJAKADESSA];
         this.pakka = kaytettavaPakka;
    }

    /** 
     * Luo Kaden, jonka ainoa Kortti-olio on parametrina saatu Kortti.
     * Kasi toimii aivan normaalisti, mutta Kadessa on parametrina saatu
     * Kortti heti alusta mukana. Kasi k‰ytt‰‰ parametrina saatua Korttipakkaa.
     *
     * @param kaytettavaPakka     Mist‰ Korttipakasta Kasi nostaa Kortti-olionsa.
     * @param ekaKortti           Uuden Kaden aloituskortti
     *
     * @exception Exception Heitet‰‰n, jos konstruktorille annettu
     * Kortti-parametri oli 'null'.
     */
    public Kasi(Korttipakka kaytettavaPakka, Kortti ekaKortti) 
                                              throws Exception {
         
         if (ekaKortti == null)
             throw new Exception("Kateen ei voi asettaa null-arvoa Kortti-olion asemesta!");

         this.kortit = new Kortti[MAXKORTTEJAKADESSA];
         this.pakka = kaytettavaPakka;
         this.kortit[0] = ekaKortti;
         this.kortteja = 1;
    }

    /**
     * Tekee parametrina saadusta Kasi-oliosta kopion. Kopion
     * sis‰lt‰m‰t Kortit ovat identtisi‰ kopioita alkuper‰isist‰.
     * Kopion k‰ytt‰m‰ Korttipakka on kuitenkin turvallisuussyist‰
     * <tt>null</tt>, joten kopioidulla Kadella EI VOI NOSTAA kortteja.
     *
     * @param kopioitava      Kasi-olio, josta on tarkoitus tehd‰ kopio.
     */
    public Kasi(Kasi kopioitava) {
         this.kortit = new Kortti[MAXKORTTEJAKADESSA];
         this.pakka = null; //HUOM! Kopiota ei voi kayttaa korttien nostoon tai pelaamiseen.
         for (int i=1; i <= kopioitava.montakoKorttia(); i++)
              this.kortit[i -1] = kopioitava.katsoKortti(i);
         this.kortteja = kopioitava.montakoKorttia();
    }

    /** Tyhj‰ Kasi ilman Korttipakkaa on huono ajatus. */
    private Kasi() { } //Tata ei ole tarkoitus kayttaa koskaan. Ei ole Katta ilman pakkaa!

    /**
     * Kasi ottaa yhden lis‰kortin k‰ytt‰m‰st‰‰n Korttipakasta.
     * Kortti pysyy Kadessa loppuun asti.
     *
     * @return     <tt>True</tt>, jos kortinotto onnistui. <p><tt>False</tt>, jos <ul><li>Korttipakka
     * ei voinut antaa Korttia</li> <li>Kortteja on Kadessa jo maksimim‰‰r‰</li>
     * <li>Korttipakkaa ei ole</li> </ul>
     *
     */
    public boolean otaKortti() {
         if (this.kortteja >= MAXKORTTEJAKADESSA) { return false;}
         if (this.pakka == null) { return false;}

         Kortti otettavaKortti = this.pakka.nostaKortti();
         if (otettavaKortti == null) { return false;} //Kortinotto ei onnistunut

         this.kortit[this.kortteja] = otettavaKortti;
         this.kortteja++;
         return true;
    }


    /**
     * @return Montako Kortti-oliota Kasi sisaltaa
     */
    public int montakoKorttia() { return this.kortteja;}


    /**
     * Palauttaa halutun Kadessa olevan Kortti-olion kopion.
     * Indeksikutsut v‰lilt‰ 1 - montakoKorttia().
     *
     * @param monesko    Indeksiviittaus kortin j‰rjestysnumeroon.
     * Ensimm‰inen kortti parametrilla "1". Parametrin on oltava
     * v‰lill‰ 1 - montakoKorttia().
     *
     * @return   <ul><li>Haluttu Kortti-olio, jos parametrin indeksissa on Kortti</li>
     *  <li><tt>Null</tt>, jos indeksin arvo oli v‰‰rin</li></ul>
     */
    public Kortti katsoKortti(int moneskoKortti) {
         if ( moneskoKortti < 1 || moneskoKortti > this.kortteja)
              {return null;}                           //Virheellinen indeksi

         return Kortti.luoKortti(kortit[moneskoKortti-1].MAA, kortit[moneskoKortti-1].ARVO);
    }


    /**
     * Katso <tt>kadenArvo(false)</tt>
     * @return Katso <tt>kadenArvo(false)</tt>
     */
    public int kadenArvo() {
         return kadenArvo(false);
    }

    /**
     * Paluttaa Kaden Korttien halutunlaisen yhteispisteluvun. Parametrin arvolla
     * true palautuu Blackjack-pisteet, jossa ‰ss‰t on muunnettu arvoon 11 jos
     * mahdollista (silloin kun yhteissumma ei ylit‰ lukua 21). Arvolla false
     * palautuu raakapisteet, jossa ‰ss‰n arvoksi lasketaan aina 1.
     *
     * @param raakapisteet    Lasketaanko Kaden raakapisteet vai Blackjack-pisteet
     *
     * @return Kaden pisteluku joko raakapistein‰ tai Blackjack-pistein‰. Tyhj‰lle Kadelle
     * palautuu 0.
     */
    public int kadenArvo(boolean raakapisteet) {
         if (raakapisteet)
              return this.raakapisteet();
         else
              return bjPisteet();
    }
    /** Apumetodi laskee Kaden arvon, kun ‰ss‰t muunnetaan arvoon 11 aina kun mahdollista */
    private int bjPisteet() {
         if (this.onkoSoft() )
              return this.raakapisteet() + 10;

         return this.raakapisteet();
    }
    /** Apumetodi laskee Kaden arvon, kun ‰ss‰n arvo on aina 1 */
    private int raakapisteet() {
         int summa = 0;
         for (int i=1; i <= this.kortteja; i++) {
              if (this.katsoKortti(i).ARVO > 10) //Kuvakorttien arvo on tassa 10
                   summa = summa + 10;
              else 
                   summa = summa + this.katsoKortti(i).ARVO;
         }
         return summa;
    }

    /**
     * Onko Kasi teknisesti ottaen Blackjack? Ei ota huomioon esim. Kasien jakamista.
     *
     * @return <tt>True</tt>, jos Kadessa on tasan kaksi Korttia, joista toinen on ‰ss‰ ja
     * toinen arvoltaan 10. Muuten palautuu <tt>false</tt>.
     */
    public boolean onkoBJ() {
         if (this.kortteja == 2)
              if ( (this.kortit[0]).ARVO == 1 && this.kortit[1].ARVO >= 10 ||
                   (this.kortit[0]).ARVO >= 10 && this.kortit[1].ARVO == 1   )

                   return true;
         return false;
    }

    /**
     * Onko Kadella en‰‰ mahdollisuutta saada Blackjack. Jos Kadessa on
     * jo Blackjack, palautuu true.
     *
     * @return   <tt>False</tt>, jos Blackjack ei ole en‰‰ mahdollinen. <tt>True</tt>, jos
     * on viel‰ mahdollisuus TAI Kadessa on jo Blackjack
     *
     */
    public boolean onkoBJMahdollinen() {
         if (this.kortteja > 2) //Liikaa kortteja
              return false;
         if (this.kortteja == 2 && !this.onkoBJ()) //Kadessa on jo kaksi korttia eika Blackjackia.
              return false;
         if (this.kortteja == 2 && this.onkoBJ()) //Kadessa on Blackjack. --> ON mahdollisuus.
              return true;
         if (this.kortteja == 1)
              return ( this.kortit[0].ARVO == 1 || this.kortit[0].ARVO >= 10); //Ainoa kortti assa tai arvoltaan 10.
         return true; // nolla korttia Kadessa
    }

    /**
     * Onko Kadessa ‰ss‰, jonka arvo on viel‰ 11.
     *
     * @return  <tt>True</tt>, jos Kasi sis‰lt‰‰ ‰ss‰nm jonka arvo on 11.
     * Muuten palautuu <tt>false</tt>.
     *
     */
    public boolean onkoSoft() {  
         if (this.kadessaOnAssa() ) { 
              if (this.kadenArvo(true) + 10 <= 21) //Jos kutsuisi kadenArvo(false), mentaisiin luuppiin.
                   return true;
         }
         return false;
    }

    /** 
     * Onko Kadessa ylip‰‰t‰‰n yht‰‰n ‰ss‰‰
     *
     * @return <tt>True</tt>, jos k‰dess‰ on ainakin yksi ‰ss‰.
     * Muuten palautuu <tt>false</tt>.
     */
    private boolean kadessaOnAssa() {
         for (int i=1; i <= this.kortteja; i++)
              if (this.katsoKortti(i).ARVO == 1)  //K‰yd‰‰n Kortit l‰pi yksi kerrallaan
                   return true;
         return false;
    }


    /**
     * Tuottaa Kaden ja sen Korttien merkkiesityksen.
     * Mukana jokaisen Kortin MAA ja ARVO. Mukana myˆs
     * Kaden Blackjack-pisteet.
     *
     * @return   Kaden ja sen Korttien merkkiesitys
     */
    public String toString() {
         if (this.kortteja == 0)
              return "";

         String kortit = "(";
         for (int i=1; i <= this.kortteja; i++)
              kortit = kortit + this.katsoKortti(i) + ", ";
         return kortit + ")" +
                "\n BJpisteet:" + this.kadenArvo();
    }

    /**
     * Tuottaa Kaden ja sen Korttien laajennetun merkkiesityksen.
     * Mukana jokaisen Kortin MAA ja ARVO. Mukana myˆs
     * Kaden Blackjack- ja raakapisteet, onkoSoft()-arvo, onkoBJMahdollinen()-arvo,
     * onkoBJ()-arvo ja kadessaOnAssa()-arvo.
     *
     * @return   Kaden ja sen Korttien laajennettu merkkiesitys
     */
    private String testiToString() {
         if (this.kortteja == 0)
              return "";

         return this.toString() +
                "\n raakapisteet:" + this.kadenArvo(true) +
                "\n onkoSoft():" + this.onkoSoft() +
                "\n onkoBJMahdollinen:" + this.onkoBJMahdollinen() +
                "\n onkoBJ:" + this.onkoBJ() +
                "\n kadessaOnAssa:" + this.kadessaOnAssa();
    }

    /** Lausekattava testiohjelma! */
    public static void main(String[] args) throws Exception{
         Korttipakka pakka = new Korttipakka(6);
         Kasi testi = new Kasi(pakka);

         //Toistetaan 40 kertaa
         for (int i=0; i < 40; i++)
              //Otetaan kadelle 4 korttia ja tulostetaan joka v‰liss‰:
              for (testi = new Kasi(pakka); testi.montakoKorttia() < 4; testi.otaKortti() )
                   System.out.println(testi.testiToString() + "\n");
         
         System.out.println("\nToinen Kortti on:" + testi.katsoKortti(2));
         System.out.println("\nVirheellinen nollas Kortti on:" + testi.katsoKortti(0));
         System.out.println("\nVirheellinen viides Kortti on:" + testi.katsoKortti(5) + "\n");
         
         //Tehd‰‰n testik‰dest‰ kopio
         Kasi kopio = new Kasi(testi);
         System.out.println("Alkuperainen:" + testi + "\n       Kopio:" + kopio);
         System.out.println("Kopio ei voi ottaa kortteja, vai voiko: " + kopio.otaKortti());

         //Otetaan kortteja niin paljon kuin vain voidaan
         while (testi.otaKortti() != false){ }
         System.out.println("\nKadessa voi olla enentaan "+testi.montakoKorttia()+" korttia\n");
         System.out.println("Kaden ainoa Kortti pit‰isi olla 'risti 3' : " + new Kasi(pakka, Kortti.luoKortti("risti", 3)));
         System.out.println("Ohjelman pit‰isi kaatua kun yritet‰‰n vied‰ k‰teen v‰kisin null-arvo");
         System.out.println(new Kasi(pakka, null));
    }

}