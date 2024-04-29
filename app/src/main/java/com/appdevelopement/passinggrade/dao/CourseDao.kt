import androidx.room.Dao
import androidx.room.Query

@Dao
interface CourseDao {

    @Query("SELECT * FROM tblCourse")
    abstract fun getAllCourses(): List<Course>

    // Rest of your CourseDao methods...
}