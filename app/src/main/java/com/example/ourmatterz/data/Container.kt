package com.example.ourmatterz.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.ourmatterz.BuildConfig
import com.example.ourmatterz.data.network.ApiService
import com.example.ourmatterz.data.room.BookmarksDatabase
import com.google.gson.internal.GsonBuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

interface Container {
    val repository: Repository
}

class AppContainer(private val context: Context) : Container {

    private val baseUrl = "https://newsapi.org/v2/"
    private val apiKey = BuildConfig.NEWS_API_KEY

    val cacheSize = (10 * 1024 * 1024).toLong()
    val cacheDir = File(context.cacheDir, "news_cache")
    val cache = Cache(directory = cacheDir, maxSize = cacheSize)


    private fun okHttpClient() = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor (
            Interceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .header(name = "Authorization", value = "Bearer $apiKey")
                    .build()
                chain.proceed(request)
            }
        )
        .addInterceptor(
            Interceptor { chain ->
                var request: Request = chain.request()
                request = if (isNetworkAvailable(context)) {
                    request.newBuilder()
                        .removeHeader("Pragma")
                        .header(name = "Cache-Control", value = "public, max-age=300")
                        .build()
                } else {
                    request.newBuilder()
                        .removeHeader("Pragma")
                        .header(name = "Cache-Control", value = "public, only-if-cached, max-stale=604800")
                        .build()
                }
                return@Interceptor chain.proceed(request)
            }
        )
        .addNetworkInterceptor(
            Interceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                return@Interceptor response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=300")
                    .build()
            }
        )


    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }


    private val retrofit = Retrofit.Builder()
        .client(okHttpClient().build())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }


    override val repository: Repository by lazy {
        AppRepository(
            apiService = retrofitService,
            dao = BookmarksDatabase.getDatabase(context).dao()
        )
    }
}