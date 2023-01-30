package com.example.kisileruygulamasi

import android.annotation.SuppressLint
import android.content.ContentValues

/*
Kisilerdao = Kisiler Data Access Object anlamına geliyor.
Yani Kisilerdao diyorsak Kisiler sınıfından oluşturulan nesnelere veritabanında erişmek amacımız.
*/

class Kisilerdao {

    //Veritabanından kişi silmeye yarayan fonksiyon
    fun kisiSil(vt:VeritabaniYardimcisi,kisi_id:Int){ //veri tabanıyla iş yapacağımız için vt lazım. Kisi_id'ye ulaşıp sileceğimiz için kişi id lazım.
        val db = vt.writableDatabase //db değişkeni ile verit tabanına yazma izni aldık
        db.delete("kisiler","kisi_id=?", arrayOf(kisi_id.toString())) //kisiSil fonksiyonuna yolladığımız kisi_id burada arrayof kısmına geliyor ve oradanda kisi_id=? kısmında ? kısmına yerleşiyor. yani hangi id yi verdiysek tabloda o id nin alanına ulaşıyoruz
        db.close() //veritabanını kapatıyoruz
    }

    fun kisiEkle(vt:VeritabaniYardimcisi,kisi_ad:String,kisi_tel:String){
        val db = vt.writableDatabase //izini aldık

        val values = ContentValues()
        values.put("kisi_ad",kisi_ad)
        values.put("kisi_tel",kisi_tel)

        db.insertOrThrow("kisiler",null,values)
        db.close()

    }

    fun kisiGuncelle(vt:VeritabaniYardimcisi,kisi_id:Int,kisi_ad:String,kisi_tel:String){
        val db = vt.writableDatabase //izini aldık

        val values = ContentValues()
        values.put("kisi_ad",kisi_ad)
        values.put("kisi_tel",kisi_tel)

        db.update("kisiler",values,"kisi_id=?", arrayOf(kisi_id.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun tumKisiler(vt:VeritabaniYardimcisi) : ArrayList<Kisiler>{ //Kisiler sınıfından bir Arraylist döndürecek demek
        val db = vt.writableDatabase //izinimizi aldık db değişkenini kullanıcaz yazma okuma işlemlerinde
        val kisilerListe = ArrayList<Kisiler>()  //geri döndüreceğimiz listenin değişkenini oluşturduk
        val c = db.rawQuery("SELECT * FROM kisiler",null)  //cursor (c) oluşturduk ve tablodaki tüm verileri okuduk.

        while (c.moveToNext()){ //cursor'a bir sonrakine geç dedik döngü boyunca
            val kisi = Kisiler(c.getInt(c.getColumnIndex("kisi_id")) //tablodaki kisi_id sütunundaki veriyi oku ve kisi nesnesindeki kisi_id ye aktar dedik. Bu şekiilde devam etti bu olay
                ,c.getString(c.getColumnIndex("kisi_ad"))
                ,c.getString(c.getColumnIndex("kisi_tel")))
            kisilerListe.add(kisi)  //tüm verilerin olduğu liste bu. tek tek ekledik her döngüde
        }
        return kisilerListe  //listeyi geri döndürdük
    }

    @SuppressLint("Range")
    fun kisiAra(vt:VeritabaniYardimcisi, aramaKelime:String) : ArrayList<Kisiler>{  //Kisiler sınıfından bir Arraylist döndürecek demek
        val db = vt.writableDatabase
        val kisilerListe = ArrayList<Kisiler>()
        val c = db.rawQuery("SELECT * FROM kisiler WHERE kisi_ad like '%$aramaKelime%'",null) //like diyerek aranan kelimeyi içeren verileri almış olduk

        while (c.moveToNext()){
            val kisi = Kisiler(c.getInt(c.getColumnIndex("kisi_id"))
                ,c.getString(c.getColumnIndex("kisi_ad"))
                ,c.getString(c.getColumnIndex("kisi_tel")))
            kisilerListe.add(kisi)
        }
        return kisilerListe
    }







}