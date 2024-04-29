import androidx.room.Dao
import androidx.room.Query

@Dao
interface ExamDao {

    @Query("SELECT * FROM tblExam")
    abstract fun getAllTeachers(): List<Exam>

    // Rest of your TeacherDao methods...
}