import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Teacher : Table<Nothing>
    ("tblTeacher"){
    val idTeacher = int("idTeacher").primaryKey()
    val dtEmail = varchar("dtEmail")
    val dtPassword = varchar("dtPassword")
    val dtName = varchar("dtName")
}
