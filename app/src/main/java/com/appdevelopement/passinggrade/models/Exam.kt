import androidx.room.*


@Entity(indices = [Index("idTeacher"), Index("idStudent")],
    foreignKeys = [
        ForeignKey(entity = Teacher::class,
            parentColumns = arrayOf("idTeacher"),
            childColumns = arrayOf("idTeacher"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Student::class,
            parentColumns = arrayOf("idStudent"),
            childColumns = arrayOf("idStudent"),
            onDelete = ForeignKey.CASCADE)
    ])
data class Exam(
    @PrimaryKey(autoGenerate = true) val examId: Int,
    val idTeacher: Int,
    val idStudent: Int,
    val grade: Int
)