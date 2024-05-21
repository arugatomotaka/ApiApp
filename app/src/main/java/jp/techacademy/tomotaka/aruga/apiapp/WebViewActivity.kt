package jp.techacademy.tomotaka.aruga.apiapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import jp.techacademy.tomotaka.aruga.apiapp.databinding.ActivityWebViewBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.Sort
import java.security.Key

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var couponUrls = intent.getStringExtra(KEY_URL)

        var shop = Shop(
            CouponUrls(couponUrls ?: "",couponUrls ?: ""),
            intent.getStringExtra(KEY_id)!!,
            intent.getStringExtra(Key_image)!!,
            intent.getStringExtra(Key_name)!!,
            intent.getStringExtra(Key_address)!!
        )

        binding.webView.loadUrl(intent.getStringExtra(KEY_URL).toString())

//        var shop = Shop(
//            couponUrls,
//            intent.getStringExtra(KEY_id)!!,
//            intent.getStringExtra(Key_image)!!,
//            intent.getStringExtra(Key_name)!!,
//            intent.getStringExtra(Key_address)!!
//        )

        binding.favoriteImageView.apply {
            // お気に入り状態を取得
            val isFavorite = FavoriteShop.findBy(shop.id) != null

            // 白抜きの星を設定→星の色の処理
            setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border)

            // 星をタップした時の処理
            setOnClickListener {
                if (isFavorite) {
                    dialogokornot(shop.id)

                } else {
                    tourokusyori(shop)
                }


            }
        }


    }

    private fun deletefavorite(id: String) {val config = RealmConfiguration.create(schema = setOf(FavoriteShop::class))
        val realm = Realm.open(config)

        // 削除処理
        realm.writeBlocking {
            val favoriteShops = query<FavoriteShop>("id=='$id'").find()
            favoriteShops.forEach {
                delete(it)
            }
        }

        // Realmデータベースとの接続を閉じる
        realm.close()

    }

    private fun dialogokornot(id:String){
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deletefavorite(id)}.setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
            .show()
    }

    private fun tourokusyori(shop:Shop) {
        FavoriteShop.insert(FavoriteShop().apply {
            id = shop.id
            name = shop.name
            imageUrl = shop.logoImage
            url = shop.couponUrls.sp.ifEmpty { shop.couponUrls.pc }
            address = shop.address
        })
    }


    companion object {
        private const val KEY_URL = "url"
        private const val KEY_id = "id"
        private const val Key_image = "image"
        private const val Key_address = "address"
        private const val Key_name = "name"

        fun start(activity: Activity, url: String, id: String, image: String, address: String,name: String) {

            activity.startActivity(
                Intent(activity, WebViewActivity::class.java)
                    .putExtra(KEY_URL, url)
                    .putExtra(KEY_id, id)
                    .putExtra(Key_image, image)
                    .putExtra(Key_address, address)
                    .putExtra(Key_name, name)
            )
        }
    }


}







