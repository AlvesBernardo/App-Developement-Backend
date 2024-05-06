import org.ktorm.database.Database
object DatabaseProvider {
    val database: Database = Database.connect(
        url = Config.dbUrl,
        driver = Config.dbDriver,
        user = Config.dbUser,
        password = Config.dbPassword
    )
}