package com.project.veganlife.community.data.remote

import android.util.Log
import com.project.veganlife.community.data.model.Feeds
import org.json.JSONObject
import javax.inject.Inject

class CommunityRemoteDataSource @Inject constructor() {
    private val communityApiService = CommunityClient.communityApi

    suspend fun getFeeds(
        accessToken: String,
    ): Feeds? {
        try {
            val feedsGetResponse = communityApiService.getFeedAll(
                accessToken,
                0,
                30,
                "createdAt,DESC",
            )

            if (feedsGetResponse.code() != 200) {
                val stringToJson = JSONObject(feedsGetResponse.errorBody()?.string()!!)
                Log.d("FeedsGetFailure", feedsGetResponse.code().toString())
                Log.d("FeedsGetFailure", "$stringToJson")
                return null
            }

            Log.d("FeedsGetSuccess", feedsGetResponse.code().toString())
            return feedsGetResponse.body()
        } catch (e: Exception) {
            Log.e("FeedsGetException", e.toString())
            return null
        }
    }
}
