package com.example.collegeschedule.data.api

import com.example.collegeschedule.data.dto.GroupDto
import retrofit2.http.GET

interface GroupsApi {
    @GET("api/groups")
    suspend fun getAllGroups(): List<GroupDto>
}