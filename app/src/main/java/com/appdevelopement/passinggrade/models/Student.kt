import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey(autoGenerate = true) val idStudent: Int,
    val studentName: String,
    val studentNumber: Int, // Change this to camelCase
    val isGraded: Boolean
)
