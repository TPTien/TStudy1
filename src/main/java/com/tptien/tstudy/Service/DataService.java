package com.tptien.tstudy.Service;

        import com.google.gson.JsonElement;
        import com.tptien.tstudy.Course;
        import com.tptien.tstudy.User;
        import com.tptien.tstudy.Vocabulary;

        import org.json.JSONArray;

        import java.lang.reflect.Array;
        import java.util.ArrayList;
        import java.util.List;

        import io.reactivex.Observable;
        import okhttp3.MultipartBody;
        import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.DELETE;
        import retrofit2.http.Field;
        import retrofit2.http.FormUrlEncoded;
        import retrofit2.http.GET;
        import retrofit2.http.Multipart;
        import retrofit2.http.POST;
        import retrofit2.http.Part;
        import retrofit2.http.Query;

public interface DataService {

    @GET("register.php")
    Call<User> Register(@Query("username")String username, @Query("password") String password,@Query("displayname")String displayName);
    @GET("login.php")
    Call<User> Login(@Query("username") String username, @Query("password") String password);
    @GET("recentCourses.php")
//    Call<List<Course>> getRecentCourses(@Query("idcourse")String idCourse
//                                        ,@Query("coursename")String courseName
//                                        ,@Query("username")String username
//                                        ,@Query("topic")String topic,@Query("nummember")String numMember,@Query("numVoca")String numVoca);
    Call<List<Course>> getRecentCourses(@Query("iduser") String idUser);
    @GET("allCourses.php")
    Call<List<Course>> getAllCourses();
    @GET("createdCourses.php")
    Call<List<Course>> getCreatedCourses(@Query("iduser")String idUser);
    @GET("joinedCourses.php")
    Call<List<Course>> getJoinedCourses(@Query("iduser")String idUser);
    //    Call<Course> restoreCourses(@Query("idcourse")String idCourse
//                            ,@Query("courname")String courseName
//                            ,@Query("idhost")String idHost,@Query("topic")String topic,@Query("nummember")String numMember
//                            ,@Query("numVoca")String numVocab
//                            ,@Query("datecreate")String dateCreate);
    @GET("deleteCourse.php")
    Call<Void> deleteCourse(@Query("idcourse") String idCourse);
    @GET("leaveCourse.php")
    Call<Void> leaveCourse(@Query("iduser")String idUser,@Query("idcourse")String idCourse);
    @GET("suggestedCourses.php")
    Call<List<Course>> getSuggestedCourses(@Query("iduser")String idUser);

    @GET("friendInCourse.php")
    Observable<List<User>> getFriendCourse(@Query("idhost")String idHost, @Query("select")String select);
    @FormUrlEncoded
    @POST("inviteFriend.php")
    Call<Void> inviteFriend(@Field("array[]") Object[] mList, @Field("idHost")String idHost, @Field("namecourse")String nameCourse, @Field("topic") String topic);

    @Multipart
    @POST("uploadVocab.php")
    Observable<String> createNewCourse(@Part List<MultipartBody.Part> file, @Part("word[]") ArrayList<String> wordList,
                              @Part("meaning[]") ArrayList<String> meaningList,
                              @Part("fullpath[]") ArrayList<String> fullPathList,
                              @Part("listfriend[]")ArrayList<String>listFriendInvited,
                              @Part("nameCourse")String  nameCourse,
                              @Part("topic")String topic,
                              @Part("idHost")String idHost);
}
