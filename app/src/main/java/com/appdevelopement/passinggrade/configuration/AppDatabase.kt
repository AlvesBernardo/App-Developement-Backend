import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Teacher::class, Student::class, Course::class, Exam::class, TeacherCourse::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teacherDao(): TeacherDao
    abstract fun studentDao(): StudentDao
    abstract fun courseDao(): CourseDao
    abstract fun examDao(): ExamDao
    abstract fun teacherCourseDao(): TeacherCourseDao
}