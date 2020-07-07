package com.example.samplepos.model

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class LoginWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {

        // Uploader order and order item
        val host = inputData.getString("host")
        val user = inputData.getString("user")
        val pass = inputData.getString("pass")

        var token=validate(host,user,pass)
        // Indicate whether the work finished successfully with the Result
        return Result.success(createOutputData(token))
    }

    private fun createOutputData(token: String): Data {
        return Data.Builder()
            .putString("uriString", token)
            .build()
    }

    private fun validate(host: String?, username:String?, password:String?): String{
        var token="null"

        var reqParam = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")
        reqParam += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")

        val mURL = URL(host+"/login.php")

        with(mURL.openConnection() as HttpURLConnection) {
            requestMethod = "POST"

            val wr = OutputStreamWriter(getOutputStream());
            wr.write(reqParam);
            wr.flush();

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                token=response.toString()
            }
        }

        return token
    }
}
