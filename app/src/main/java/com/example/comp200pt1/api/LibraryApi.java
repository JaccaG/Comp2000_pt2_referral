package com.example.comp200pt1.api;

import com.example.comp200pt1.api.dto.IssueBookRequest;
import com.example.comp200pt1.api.dto.MemberDto;
import com.example.comp200pt1.api.dto.UpdateMemberRequest;
import com.example.comp200pt1.api.dto.ApiMessage;
import com.example.comp200pt1.api.dto.IssuedBookDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface LibraryApi {

    // GET /members (read)
    @GET("members")
    Call<List<MemberDto>> getMembers();

    // POST /members  (create)
    @POST("members")
    Call<ApiMessage> createMember(@Body com.example.comp200pt1.api.dto.CreateMemberRequest body);

    // PUT /members/{username} (update)
    @PUT("members/{username}")
    Call<ApiMessage> updateMember(
            @Path("username") String username,
            @Body UpdateMemberRequest body
    );

    // DELETE /members/{username} (delete)
    @DELETE("members/{username}")
    Call<ApiMessage> deleteMember(@Path("username") String username);

    // GET /books/{username} (read)
    @GET("books/{username}")
    Call<List<IssuedBookDto>> getIssuedBooks(@Path("username") String username);

    // POST /books (create)
    @POST("books")
    Call<ApiMessage> issueBook(@Body IssueBookRequest body);
}
