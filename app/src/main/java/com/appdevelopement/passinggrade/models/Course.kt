import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity


@Entity
data class Course(
    @PrimaryKey(autoGenerate = true) val idCourse : Int,
    @ColumnInfo(name = "dtDescription") val dtDescription: String?
)
