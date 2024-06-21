// import android.content.Context
// import com.appdevelopement.passinggrade.R
// import java.io.InputStreamReader
// import java.util.*
//
// object Config {
//    lateinit var dbUrl: String
//    lateinit var dbDriver: String
//    lateinit var dbUser: String
//    lateinit var dbPassword: String
//
//    fun load(context: Context) {
//        val properties = Properties()
//        val inputStream = context.resources.openRawResource(R.raw.config)
//        properties.load(InputStreamReader(inputStream, "UTF-8"))
//
//        dbUrl = properties.getProperty("db.url")
//        dbDriver = properties.getProperty("db.driver")
//        dbUser = properties.getProperty("db.user")
//        dbPassword = properties.getProperty("db.password")
//    }
// }
