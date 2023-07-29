package com.example.websocket_upbit.data.retrofit

import com.example.websocket_upbit.BuildConfig
import com.example.websocket_upbit.util.FConstrant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL: String = FConstrant.BASE_URL
    private const val TIMEOUT = 15

    @Singleton
    @Provides
    fun startRetrofit(): ApiV1 {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getUnsafeOkHttpClient().build())
            .client(createHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        return retrofit.create(ApiV1::class.java)
    }

    private fun createHttpClient(): OkHttpClient {
        val interceptors = ArrayList<Interceptor>()
        return getOkHttpClient(interceptors)
    }

    private fun getOkHttpClient(interceptors: ArrayList<Interceptor>): OkHttpClient {
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            interceptors.add(loggingInterceptor)
        }
        val builder = getBuilder(0)
        for (interceptor in interceptors) {
            builder.addInterceptor(interceptor)
        }
        return builder.build()
    }

    private fun getBuilder(time: Int): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout((if (time > 0) time else TIMEOUT).toLong(), TimeUnit.SECONDS)
            .writeTimeout((if (time > 0) time else TIMEOUT).toLong(), TimeUnit.SECONDS)
            .readTimeout((if (time > 0) time else TIMEOUT).toLong(), TimeUnit.SECONDS)
    }

    // 인증서 없이 앱에서 HTTPS 우회 접속 통신
    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory = sslContext.socketFactory

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { hostname, session -> true }

        return builder
    }
}