import androidx.room.Dao
import androidx.room.Query


@Dao
interface ExamDao{
    @Query("SELECT * FROM Exam")
    fun getAll(): List<Exam>
}