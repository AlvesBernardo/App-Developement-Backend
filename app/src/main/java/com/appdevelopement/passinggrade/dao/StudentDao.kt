import androidx.room.Dao
import androidx.room.Query


@Dao
interface StudentDao{
    @Query("SELECT * FROM Student")
    fun getAll(): List<Student>
}