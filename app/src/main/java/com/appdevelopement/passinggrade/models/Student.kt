import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey(autoGenerate = true) val idStudent: Int,
    val studentName: String,
    val StudentNumber: Int,
    val isGraded: Boolean
)