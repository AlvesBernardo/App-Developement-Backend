import android.icu.text.CaseMap.Title
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblCourse")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val idCourse: Int,
    val dtTitle: String
)