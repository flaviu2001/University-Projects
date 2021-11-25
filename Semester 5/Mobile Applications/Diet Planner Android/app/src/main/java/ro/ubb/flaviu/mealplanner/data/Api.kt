package ro.ubb.flaviu.mealplanner.dataimport com.google.gson.GsonBuilderimport okhttp3.Interceptorimport okhttp3.OkHttpClientimport okhttp3.Responseimport retrofit2.Retrofitimport retrofit2.converter.gson.GsonConverterFactoryobject Api {    private const val URL = "http://10.152.2.34:3000/"    var token: String? = null    private val tokenInterceptor = object: Interceptor {        override fun intercept(chain: Interceptor.Chain): Response {            val original = chain.request()            val originalUrl = original.url            if (token == null)                return chain.proceed(original)            val requestBuilder = original.newBuilder()                .addHeader("Authorization", "Bearer $token")                .url(originalUrl)            val request = requestBuilder.build()            return chain.proceed(request)        }    }    private val client: OkHttpClient = OkHttpClient.Builder().apply {        this.addInterceptor(tokenInterceptor)    }.build()    private var gson = GsonBuilder()        .setLenient()        .create()    val retrofit: Retrofit = Retrofit.Builder()        .baseUrl(URL)        .addConverterFactory(GsonConverterFactory.create(gson))        .client(client)        .build()}