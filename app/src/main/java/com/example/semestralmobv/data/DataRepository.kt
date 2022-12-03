package com.example.semestralmobv.data

import com.example.semestralmobv.data.api.ApiService
import com.example.semestralmobv.data.api.models.CheckInPubArgs
import com.example.semestralmobv.data.api.models.FriendArgs
import com.example.semestralmobv.data.api.models.UserArgs
import com.example.semestralmobv.data.api.models.UserResponse
import com.example.semestralmobv.data.db.dao.FriendItemDao
import com.example.semestralmobv.data.db.dao.PubItemDao
import com.example.semestralmobv.data.db.models.FriendItem
import com.example.semestralmobv.data.db.models.PubItem
import com.example.semestralmobv.ui.viewmodels.NearbyPub
import com.example.semestralmobv.ui.viewmodels.SortBy
import com.example.semestralmobv.utils.LatLongLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class DataRepository private constructor(
    private val service: ApiService,
    private val pubCache: PubItemDao,
    private val friendCache: FriendItemDao
) {

    suspend fun getAllPubsFromDb(isAsc: Boolean, sortBy: SortBy?): List<PubItem>? {
        var pubs: List<PubItem>?
        withContext(Dispatchers.IO) {
            pubs = when (sortBy) {
                SortBy.NAME -> pubCache.getAllOrderName(isAsc)
                else -> pubCache.getAll(isAsc)
            }
        }
        return pubs
    }

    suspend fun getAllFriendsFromDb(): List<FriendItem>? {
        var friends: List<FriendItem>?
        withContext(Dispatchers.IO) {
            friends = friendCache.getAll()
        }
        return friends
    }

    suspend fun login(
        name: String,
        password: String,
        onResolved: (response: UserResponse?) -> Unit,
        onError: (response: String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val loginResponse = service.loginUser(UserArgs(name, password)).execute()
                if (loginResponse.isSuccessful) {
                    loginResponse.body()?.let { user ->
                        if (user.uid == "-1") {
                            onResolved(null)
                            onError("Wrong login credentials.")
                        } else {
                            onResolved(user)
                        }
                    }
                } else {
                    onError("Failed to login, try again later.")
                    onResolved(null)
                }
            } catch (ex: IOException) {
                onError("Login failed, check internet connection")
                onResolved(null)
            } catch (ex: Exception) {
                onError("Login in failed, error.")
                onResolved(null)
            }
        }
    }

    suspend fun signup(
        name: String,
        password: String,
        onResolved: (response: UserResponse?) -> Unit,
        onError: (response: String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val signupResponse = service.signupUser(UserArgs(name, password)).execute()
                if (signupResponse.isSuccessful) {
                    signupResponse.body()?.let { user ->
                        if (user.uid == "-1") {
                            onResolved(null)
                            onError("User already exists.")
                        } else {
                            onResolved(user)
                        }
                    }
                } else {
                    onError("Failed to sign up, try again later.")
                    onResolved(null)
                }
            } catch (ex: IOException) {
                onError("Login failed, check internet connection")
                onResolved(null)
            } catch (ex: Exception) {
                onError("Login in failed, error.")
                onResolved(null)
            }
        }
    }

    suspend fun refreshPubs(
        onResolved: (response: String) -> Unit, onError: (response: String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val pubsResp = service.pubList().execute()
                if (pubsResp.isSuccessful) {
                    pubsResp.body()?.let { pubsData ->
                        val pubItems: List<PubItem> = pubsData.map { pub ->
                            pub.asDatabaseModel()
                        }
                        pubCache.deleteAll()
                        pubCache.insertAll(pubItems)
                        onResolved("Successfully loaded pubs.")
                    }
                } else {
                    onError("Failed to load pubs.")
                }
            } catch (ex: IOException) {
                onError("Failed to load pubs, check internet connection")
            } catch (ex: Exception) {
                onError("Failed to load pubs, error.")
            }
        }
    }

    suspend fun nearPubs(
        latLong: LatLongLocation, onError: (response: String) -> Unit
    ): List<NearbyPub> {
        var nearby = listOf<NearbyPub>()
        withContext(Dispatchers.IO) {
            try {
                val q =
                    "[out:json];node(around:250, ${latLong.lat}, ${latLong.long});(node(around:250)[\"amenity\"~\"^pub\$|^bar\$|^restaurant\$|^cafe\$|^fast_food\$|^stripclub\$|^nightclub\$\"];);out body;>;out skel;"
                val resp = service.getNearPubs(q).execute()
                if (resp.isSuccessful) {
                    resp.body()?.let { pubs ->
                        nearby = pubs.elements.map { pub ->
                            NearbyPub(
                                pub.id,
                                pub.tags?.name,
                                pub.tags?.amenity,
                                pub.lat,
                                pub.lon,
                                pub.tags,
                            ).apply { this.distanceTo(latLong) }
                        }.sortedBy { it.distance }
                    } ?: onError("Failed to load pubs.")
                } else {
                    onError("Failed to load pubs.")
                }
            } catch (ex: IOException) {
                onError("Failed to load pubs, check internet connection")
            } catch (ex: Exception) {
                onError("Failed to load pubs, error.")
            }
        }
        return nearby
    }

    suspend fun pubDetail(
        id: String, onResolved: (response: String) -> Unit, onError: (response: String) -> Unit
    ): NearbyPub? {
        var nearbyPub: NearbyPub? = null
        withContext(Dispatchers.IO) {
            try {
                val q = "[out:json];node($id);out body;>;out skel;"
                val resp = service.getPubDetail(q).execute()
                if (resp.isSuccessful) {
                    resp.body()?.let { pubsData ->
                        if (pubsData.elements.isNotEmpty()) {
                            val firstPub = pubsData.elements[0]
                            nearbyPub = NearbyPub(
                                firstPub.id,
                                firstPub.tags?.name,
                                firstPub.tags?.amenity,
                                firstPub.lat,
                                firstPub.lon,
                                firstPub.tags,
                            )
                        }
                    }
                } else {
                    onError("Failed to load pub detail.")
                }
            } catch (ex: IOException) {
                onError("Failed to load pubs, check internet connection")
            } catch (ex: Exception) {
                onError("Failed to load pubs, error.")
            }
        }
        return nearbyPub
    }

    suspend fun checkInPub(
        pub: NearbyPub, onResolved: (response: Boolean) -> Unit, onError: (response: String) -> Unit
    ) {
        if (pub.name == null || pub.type == null) {
            return
        }
        withContext(Dispatchers.IO) {
            try {
                val resp = service.checkInPub(
                    CheckInPubArgs(
                        pub.id, pub.name, pub.type, pub.lat, pub.long
                    )
                ).execute()
                if (resp.isSuccessful) {
                    onResolved(true)
                } else {
                    onError("Failed to check into pub.")
                }
            } catch (ex: IOException) {
                onError("Check into pub failed, check internet connection")
            } catch (ex: Exception) {
                onError("Failed to check into pub, error.")
            }
        }
    }

    suspend fun refreshFriends(
        onResolved: (response: String) -> Unit, onError: (response: String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val resp = service.getFriends().execute()
                if (resp.isSuccessful) {
                    resp.body()?.let { friendsData ->
                        val friendItems: List<FriendItem> = friendsData.map { friend ->
                            friend.asDatabaseModel()
                        }
                        friendCache.deleteAll()
                        friendCache.insertAll(friendItems)
                        onResolved("Successfully loaded friends.")
                    }
                } else {
                    onError("Failed to load friends list.")
                }
            } catch (ex: IOException) {
                onError("Failed to load friends, check internet connection")
            } catch (ex: Exception) {
                onError("Failed to load friends, error.")
            }
        }
    }

    suspend fun addFriend(
        name: String, onResolved: (response: String) -> Unit, onError: (response: String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val resp = service.addFriend    (FriendArgs(name)).execute()
                if(resp.isSuccessful){
                    onResolved("Friend $name successfully added.")
                }else{
                    onError("Failed to add a friend, try a different name.")
                }
            } catch (ex: IOException) {
                onError("Failed to add a friend, check internet connection")
            } catch (ex: Exception) {
                onError("Failed to add a friend, error.")
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(
            service: ApiService, pubCache: PubItemDao, friendCache: FriendItemDao
        ): DataRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: DataRepository(service, pubCache, friendCache).also { INSTANCE = it }
        }

    }
}