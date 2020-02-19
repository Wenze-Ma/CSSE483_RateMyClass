package edu.rosehulman.ratemyclass

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.lang.Exception
import java.net.URL

class GetImageTask(private var view: ImageView):
    AsyncTask<String, Void, Bitmap>() {

    lateinit var url: URL

    override fun doInBackground(vararg urlStrings: String?): Bitmap? {
        return try {
            url = URL(urlStrings[0])
            val inStream = url.openStream()
            val bitmap = BitmapFactory.decodeStream(inStream)
            return bitmap
        } catch (e: Exception) {
            null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        view.setImageBitmap(result)
    }
}