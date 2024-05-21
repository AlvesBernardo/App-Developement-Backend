import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Course(
    @PrimaryKey(autoGenerate = true) val idCourse: Int,
    @ColumnInfo(name = "dtDescription") val dtDescription: String?
)
