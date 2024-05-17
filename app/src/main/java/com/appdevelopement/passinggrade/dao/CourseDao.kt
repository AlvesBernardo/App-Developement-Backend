import androidx.room.Dao
import androidx.room.Query


@Dao
interface CourseDao{
    @Query("SELECT * FROM Course")
    fun getAll(): List<Course>
//    @Insert
//    fun insertAll(vararg users: User)
//
//    @Delete
//    fun delete(user: User)
}