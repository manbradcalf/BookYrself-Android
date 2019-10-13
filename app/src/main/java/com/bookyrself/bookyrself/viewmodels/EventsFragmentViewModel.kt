package com.bookyrself.bookyrself.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bookyrself.bookyrself.data.ServerModels.EventDetail.EventDetail
import com.bookyrself.bookyrself.services.FirebaseServiceCoroutines
import com.bookyrself.bookyrself.services.clients.UsersClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by benmedcalf on 3/11/18.
 */
class EventsFragmentViewModel : BaseViewModel() {
    var eventDetailsHashMap = MutableLiveData<HashMap<EventDetail, String>>()
    var signedOutMessage = MutableLiveData<String>()

    init {
        if (FirebaseAuth.getInstance().uid != null) {
            //TODO: Why do I have to !! here if i'm null checking above
            loadUsersEventInfo(userId!!)
        } else {
            signedOutMessage.value = "Sign in to see your events!"
        }
    }

    //TODO: Copied over from UserDetailViewModel. This will also be needed in ProfileViewModel. How to consolidte considering they rely on livedata in the activity?
    private fun loadUsersEventInfo(userId: String) {
        //TODO: Check if the cache is dirty here. If it is we need to go to network
        val events = HashMap<EventDetail, String>()

        CoroutineScope(Dispatchers.IO).launch {
            val userResponse = UsersClient.service.getUserDetails(userId)
            if (userResponse.isSuccessful && userResponse.body()?.events?.keys != null) {
                userResponse.body()?.events?.keys?.forEach { eventId ->
                    val eventDetailResponse = service.getEventData(eventId)
                    withContext(Dispatchers.Main) {
                        if (eventDetailResponse.isSuccessful && eventDetailResponse.body() != null) {
                            events[eventDetailResponse.body()!!] = eventId
                            eventDetailsHashMap.value = events
                        } else if (eventDetailResponse.isSuccessful && eventDetailResponse.body() == null) {
                            Log.e("EventsFragmentViewModel", "No data for eventId $eventId")
                        } else {
                            errorMessage.value = eventDetailResponse.message()
                        }
                    }
                }
            }
        }
    }

    //TODO: Genericize this?
    class EventsFragmentViewModelFactory() : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EventsFragmentViewModel() as T
        }
    }
}