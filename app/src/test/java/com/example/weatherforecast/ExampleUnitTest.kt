package com.example.weatherforecast

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.junit.Test

import org.junit.Assert.*
import org.w3c.dom.Element

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun testJsoup(dayNumber:Int){
        var doc = Jsoup.connect("https://www.gismeteo.ru/weather-frankfurt-am-main-2589/").get()
        println(doc.title())

        val elementList = doc.select("span.unit_temperature_c").toList()
        for (element in elementList) {
            println(element.text())
        }
        var night = 0
        var day =  0
        if (dayNumber == 1) {
            var night = elementList.get(2).text()
            var day = elementList.get(3).text()
        }else {
            var night = elementList.get(4).text()
            var day = elementList.get(5).text()
        }

        var docNow = Jsoup.connect("https://www.gismeteo.ru/weather-frankfurt-am-main-2589/now/").get()
             var element = docNow.select("div.now-desc")

        println("Сегодня ${element.text()}")
        println("сегодня Днем $day")
        println("сегодня Ночью $night")
    }


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}