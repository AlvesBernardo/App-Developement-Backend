import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblTeacher")
data class Teacher(
    @PrimaryKey(autoGenerate = true)
    val idTeacher: Int,
    val dtEmail: String,
    val dtPassword: String,
    val dtName: String
)