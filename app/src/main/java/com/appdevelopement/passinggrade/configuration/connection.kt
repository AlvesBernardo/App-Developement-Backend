//import android.util.Log
//import org.ktorm.database.Database
//object DatabaseProvider {
//
//    val database: Database? = try {
//        Database.connect(
//            url = Config.dbUrl,
//            driver = Config.dbDriver,
//            user = Config.dbUser,
//            password = Config.dbPassword
//        )
//    } catch (ex: Exception) {
//        Log.e("Database Connection", "Failed to connect to the database", ex)
//        null
//    }
//}