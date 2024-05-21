import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Teacher(
    @PrimaryKey(autoGenerate = true) val idTeacher: Int,
    @ColumnInfo(name = "dtEmail") val dtEmail: String?,
    @ColumnInfo(name = "dtPassword") val dtPassword: String?,
    @ColumnInfo(name = "dtName") val dtName: String?
)
