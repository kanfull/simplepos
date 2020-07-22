package com.example.mvvmmypos.http

import com.example.mvvmmypos.model.LoginResult
import com.example.mvvmmypos.model.Price
import com.example.mvvmmypos.model.Product
import com.example.mvvmmypos.model.Promotion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class Person(
    val name: String,
    val age: Int
)

class WebRequest {
     companion object{
         fun login(host: String, username:String, password:String): LoginResult? {
             var reqParam = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
             reqParam += "&" + URLEncoder.encode(
                 "pass",
                 "UTF-8"
             ) + "=" + URLEncoder.encode(password, "UTF-8")
             val url = "$host/login.php"
             var token = httpPostRequest(url, reqParam)
             if(token=="null"){ return null }

             val result = Gson().fromJson(token, LoginResult::class.java)
             return result
         }

         fun loginWithToken(host: String, token: String): Boolean {
             var reqParam = URLEncoder.encode("jwt", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
             val url = "$host/login.php"
             var result = httpPostRequest(url, reqParam)
             return result != "null"
         }

         fun getProduct(host: String, token: String,dt:Long=0) : List<Product>? {
             var reqParam = URLEncoder.encode("jwt", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
             reqParam += "&" + URLEncoder.encode("dt","UTF-8"
             ) + "=" + URLEncoder.encode(dt.toString(), "UTF-8")
             val url = "$host/product.php"
             var result = httpPostRequest(url, reqParam)

             val gson = Gson()
             val arrayProductType = object : TypeToken<List<Product>>() {}.type
             var list: List<Product> = gson.fromJson(result, arrayProductType)
             return list
         }

         fun getPromotion(host: String, token: String,dt:Long=0) : List<Promotion>?  {
             var reqParam = URLEncoder.encode("jwt", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
             reqParam += "&" + URLEncoder.encode("dt","UTF-8"
             ) + "=" + URLEncoder.encode(dt.toString(), "UTF-8")
             val url = "$host/promotion.php"
             var result = httpPostRequest(url, reqParam)

             val gson = Gson()
             val arrayPromotionType = object : TypeToken<List<Promotion>>() {}.type
             var list: List<Promotion> = gson.fromJson(result, arrayPromotionType)
             return list
         }

         fun getPrice(host: String, token: String,dt:Long=0) : List<Price>?  {
             var reqParam = URLEncoder.encode("jwt", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
             reqParam += "&" + URLEncoder.encode("dt","UTF-8"
             ) + "=" + URLEncoder.encode(dt.toString(), "UTF-8")
             val url = "$host/price.php"
             var result = httpPostRequest(url, reqParam)

             val gson = Gson()
             val arrayPriceType = object : TypeToken<List<Price>>() {}.type
             var list: List<Price> = gson.fromJson(result, arrayPriceType)
             return list
         }

         fun addSale(host: String,token: String,description: String): Long {
             var reqParam = URLEncoder.encode("jwt", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
             reqParam+= "&" +URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8")

             val url = "$host/sale.php"
             var result = httpPostRequest(url, reqParam)
             if(result=="null"){
                 return 0
             }else{
                 return result.toLong()
             }
         }

         fun addSaleItem(host: String,token: String,sale_id: Long,product_id: Long,price_id: Long,promotion_id: Long,sale_price:Double,unit: Int): Long {
             var reqParam = URLEncoder.encode("jwt", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8")
             reqParam+= "&" +URLEncoder.encode("sale_id", "UTF-8") + "=" + URLEncoder.encode(sale_id.toString(), "UTF-8")
             reqParam+= "&" +URLEncoder.encode("product_id", "UTF-8") + "=" + URLEncoder.encode(product_id.toString(), "UTF-8")
             reqParam+= "&" +URLEncoder.encode("price_id", "UTF-8") + "=" + URLEncoder.encode(price_id.toString(), "UTF-8")
             reqParam+= "&" +URLEncoder.encode("promotion_id", "UTF-8") + "=" + URLEncoder.encode(promotion_id.toString(), "UTF-8")
             reqParam+= "&" +URLEncoder.encode("sale_price", "UTF-8") + "=" + URLEncoder.encode(sale_price.toString(), "UTF-8")
             reqParam+= "&" +URLEncoder.encode("unit", "UTF-8") + "=" + URLEncoder.encode(unit.toString(), "UTF-8")

             val url = "$host/sale_item.php"
             var result = httpPostRequest(url,reqParam)
             if(result=="null"){
                 return 0
             }else{
                 return result.toLong()
             }
         }

     }
}

internal fun httpPostRequest(url: String?, reqParam: String?): String {
    val mURL = URL(url)
    var result = "null"
    with(mURL.openConnection() as HttpURLConnection) {
        requestMethod = "POST"

        val wr = OutputStreamWriter(getOutputStream())
        wr.write(reqParam)
        wr.flush()

        BufferedReader(InputStreamReader(inputStream)).use {
            val response = StringBuffer()

            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            result = response.toString()
        }
    }
    return result
}