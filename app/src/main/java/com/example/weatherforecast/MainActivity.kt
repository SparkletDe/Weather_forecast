package com.example.weatherforecast

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var connecManager: ConnectivityManager
    lateinit var networkCallback: ConnectivityManager.NetworkCallback

    var arrayList:ArrayList<String> = ArrayList(20);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connecManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object: ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                update()
            }
        }
        printWeather()

    }

    fun printWeather(){
        binding.today.setOnClickListener {
            if (arrayList.isNotEmpty()) {
                binding.textDay.text = "Day ${arrayList[0]}"
                binding.textNight.text = "Night ${arrayList[1]}"
                // binding.textWeather.text = "Today is ${arrayList[4]}"
            }
        }


        binding.tomorrow.setOnClickListener {
            if (arrayList.isNotEmpty()) {
                binding.textDay.text = "Day ${arrayList[2].toString()}"
                binding.textNight.text = "Night ${arrayList[3].toString()}"
                // binding.textWeather.text = ""
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val networkRequest=NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()

        connecManager.registerNetworkCallback(networkRequest,networkCallback)
        if(connecManager.activeNetworkInfo?.isConnected == true){
            update()
        }


    }

    override fun onPause() {
        super.onPause()
        connecManager.unregisterNetworkCallback(networkCallback)
    }

    fun update(){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                var doc =
                    Jsoup.connect("https://www.gismeteo.ru/weather-frankfurt-am-main-2589/").get()
                println(doc.title())

                val elementList = doc.select("span.unit_temperature_c").toList()
                for (element in elementList) {
                    println(element.text())
                }


                    arrayList.add(elementList.get(2).text())
                    arrayList.add(elementList.get(3).text())

                    arrayList.add(elementList.get(4).text())
                    arrayList.add(elementList.get(5).text())

                var docNow =
                    Jsoup.connect("https://www.gismeteo.ru/weather-frankfurt-am-main-2589/now/")
                        .get()
                arrayList.add(docNow.select("div.now-desc").text())


            }catch (e: IOException){
                binding.textDay.text = "not connect"
            }
        }








    }

}