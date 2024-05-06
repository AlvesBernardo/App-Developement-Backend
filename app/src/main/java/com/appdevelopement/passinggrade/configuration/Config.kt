import java.util.*
object Config{
    private val inputStream = this::class.java.classLoader.getResourceAsStream("config.properties")
    private val properties: Properties = Properties().apply {
        load(inputStream)
    }

    val dbUrl = properties.getProperty("db.url")
    val dbDriver = properties.getProperty("db.driver")
    val dbUser = properties.getProperty("db.user")
    val dbPassword = properties.getProperty("db.password")
}