import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["idTeacher", "idCourse"],
    foreignKeys = [
        ForeignKey(
            entity = Teacher::class,
            parentColumns = ["idTeacher"],
            childColumns = ["idTeacher"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Course::class,
            parentColumns = ["idCourse"],
            childColumns = ["idCourse"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idTeacher"]),
        Index(value = ["idCourse"])
    ]
)
data class TeacherCourse(
    val idTeacher: Int,
    val idCourse: Int
)
