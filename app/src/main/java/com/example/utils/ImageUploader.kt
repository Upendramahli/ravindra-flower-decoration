package com.example.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

object ImageUploader {

    private val client = OkHttpClient.Builder()
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .build()

    // ImgBB Free API Key for anonymous image hosting
    private const val IMGBB_API_KEY = "6d207e02198a847aa98d0a2a901485a5"

    suspend fun uploadBitmap(context: Context, bitmap: Bitmap): String = withContext(Dispatchers.IO) {
        try {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 88, baos)
            val bytes = baos.toByteArray()

            val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)

            val formBody = FormBody.Builder()
                .add("key", IMGBB_API_KEY)
                .add("image", base64Image)
                .build()

            val request = Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(formBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val json = JSONObject(responseBody)
                if (json.getBoolean("success")) {
                    val data = json.getJSONObject("data")
                    return@withContext data.getString("url")
                }
            }

            // Fallback: save bitmap to local file
            saveBitmapLocally(context, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            saveBitmapLocally(context, bitmap)
        }
    }

    suspend fun uploadImageFromUri(context: Context, uri: Uri): String = withContext(Dispatchers.IO) {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes() ?: throw Exception("इमेज फ़ाइल नहीं पढ़ी जा सकी")
            inputStream.close()

            val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)

            val formBody = FormBody.Builder()
                .add("key", IMGBB_API_KEY)
                .add("image", base64Image)
                .build()

            val request = Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(formBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val json = JSONObject(responseBody)
                if (json.getBoolean("success")) {
                    val data = json.getJSONObject("data")
                    return@withContext data.getString("url")
                }
            }

            copyUriToInternalStorage(context, uri)
        } catch (e: Exception) {
            e.printStackTrace()
            copyUriToInternalStorage(context, uri)
        }
    }

    private fun saveBitmapLocally(context: Context, bitmap: Bitmap): String {
        return try {
            val file = File(context.filesDir, "crop_${System.currentTimeMillis()}.jpg")
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            fos.flush()
            fos.close()
            Uri.fromFile(file).toString()
        } catch (e: Exception) {
            ""
        }
    }

    private fun copyUriToInternalStorage(context: Context, uri: Uri): String {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return uri.toString()
            val file = File(context.filesDir, "img_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            Uri.fromFile(file).toString()
        } catch (e: Exception) {
            uri.toString()
        }
    }
}
