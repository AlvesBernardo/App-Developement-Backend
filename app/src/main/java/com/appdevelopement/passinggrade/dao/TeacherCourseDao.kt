import androidx.room.Dao
import androidx.room.Query


@Dao
interface TeacherCourseDao{
    @Query("SELECT * FROM TeacherCourse")
    fun getAll(): List<TeacherCourse>
}