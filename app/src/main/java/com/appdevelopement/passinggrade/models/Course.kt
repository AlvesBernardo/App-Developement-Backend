import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Course : Table<Nothing>
    ("tblCourse"){
        val idCourse = int("idCourse").primaryKey()
        val dtDescription = varchar("dtDescription")
    }
