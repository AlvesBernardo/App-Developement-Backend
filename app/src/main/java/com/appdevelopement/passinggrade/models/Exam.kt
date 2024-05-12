import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Exam : Table<Nothing>
    ("tblExam"){
    val idExam = int("idExam").primaryKey()
    val idCourse = varchar("idCourse").primaryKey()
}
