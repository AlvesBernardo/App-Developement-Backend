import androidx.room.Dao
import androidx.room.Query

@Dao
interface TeacherDao {

    @Query("SELECT * FROM tblTeacher")
    abstract fun getAllTeachers(): List<Teacher>

    // Rest of your TeacherDao methods...
}