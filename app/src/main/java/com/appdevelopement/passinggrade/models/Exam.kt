import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "tblExam")
data class Exam(
    @PrimaryKey(autoGenerate = true)
    val examId: Int,
    val courseId: Int,
)