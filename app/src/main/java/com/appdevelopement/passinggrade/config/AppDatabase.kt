import androidx.room.Database
import androidx.room.RoomDatabase
import com.appdevelopement.passinggrade.models.TeacherCourseCrossRef

@Database(
    entities = [Teacher::class, Course::class, TeacherCourseCrossRef::class, Exam::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teacherDao(): TeacherDao
    abstract fun courseDao(): CourseDao
    abstract fun examDao(): ExamDao
}