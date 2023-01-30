package com.example.kisileruygulamasi

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

/*
KisilerAdapter sınıfı Kisiler sınıfından nesneleri içeren listeyi uygulamadaki
RecycleView'da düzenli biçimde görüntüleyebilmek için kullanılır.
*/

/*mContext şu an için ezbere kalsa olur.
* kisilerListe:List<Kisiler> diyerek Kisiler sınıfından nesnelerden oluşan bir liste yolluyoruz. Zaten recycleView un amacı bu listeyi düzenli bir şekilde görüntüleyebilmek
* vt:VeritabaniYardimcisi gönderiyoruz çünkü database'den veri okuma veya yazma yapacağız
* RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu>() kısmı tasarım dosyasını bağladıgımız yer*/
class KisilerAdapter (private val mContext: Context , private var kisilerListe:List<Kisiler> , private val vt : VeritabaniYardimcisi )
    : RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu>() {

    /*Bu kısım kisi_card_tasarim.xml deki görsel nesneleri yazılımsal şekilde bağladığımız yer. Zaten bu adapter kisi_card_tasarim ile çalışacak
    * */
    inner class CardTasarimTutucu(tasarim:View) : RecyclerView.ViewHolder(tasarim){

        var textViewKisiBilgi:TextView
        var imageViewNokta:ImageView

        init{ //bağlama işlemi init içerisinde yapılıyor
            textViewKisiBilgi = tasarim.findViewById(R.id.textViewKisiBilgi)
            imageViewNokta = tasarim.findViewById(R.id.imageViewNokta)
        }
    }

    /*RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu>() dan sonra hazır metod istiyor kotlin. bu o metodlardan birisi
    * burada dosyayı bağlıyoruz layoutInflater ile */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.kisi_card_tasarim,parent,false)
        return CardTasarimTutucu(tasarim)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {  //burası biraz karısık :)
        val kisi = kisilerListe.get(position)
        holder.textViewKisiBilgi.text = "${kisi.kisi_ad} - ${kisi.kisi_tel}"

        holder.imageViewNokta.setOnClickListener{

            val popupMenu = PopupMenu(mContext,holder.imageViewNokta)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.action_sil->{
                        Snackbar.make(holder.imageViewNokta,"${kisi.kisi_ad} silinsin mi?",Snackbar.LENGTH_SHORT)
                            .setAction("EVET"){

                                Kisilerdao().kisiSil(vt,kisi.kisi_id)
                                     //arayüz yenilensin diye anlık olarak değişimi görmek için

                            }.show()
                        true
                    }
                    R.id.action_guncelle->{
                        alertGoster(kisi)
                        true
                    }
                    else->false
                }
            }

            popupMenu.show()

        }

    }

    override fun getItemCount(): Int {
        return kisilerListe.size
    }

    fun alertGoster(kisi:Kisiler){
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.alert_tasarim,null)
        val editTextAd = tasarim.findViewById(R.id.editTextAd) as EditText
        val editTextTel = tasarim.findViewById(R.id.editTextTel) as EditText

        editTextAd.setText(kisi.kisi_ad)
        editTextTel.setText(kisi.kisi_tel)

        val ad = AlertDialog.Builder(mContext)
        ad.setTitle("Kişi Güncelle")
        ad.setView(tasarim)

        ad.setPositiveButton("Güncelle"){ dialogInterface,i ->
            val kisi_ad = editTextAd.text.toString().trim()
            val kisi_tel = editTextTel.text.toString().trim()

            Kisilerdao().kisiGuncelle(vt,kisi.kisi_id, kisi_ad , kisi_tel)
            kisilerListe = Kisilerdao().tumKisiler(vt)
            notifyDataSetChanged()

            Toast.makeText(mContext,"$kisi_ad - $kisi_tel", Toast.LENGTH_SHORT).show()
        }
        ad.setNegativeButton("İptal"){ dialogInterface,i ->

        }

        ad.create().show()
    }




}