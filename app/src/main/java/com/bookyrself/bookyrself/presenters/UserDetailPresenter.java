package com.bookyrself.bookyrself.presenters;

import android.support.annotation.NonNull;

import com.bookyrself.bookyrself.models.SearchResponseUsers._source;
import com.bookyrself.bookyrself.services.FirebaseService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by benmedcalf on 1/13/18.
 */

public class UserDetailPresenter {
    private final UserDetailPresenterListener mListener;
    private final FirebaseService mService;

    /**
     * Contract / Listener
     */
    public interface UserDetailPresenterListener {
        void userInfoReady(_source response);

        void presentError();

        void loadingState();

        void emailUser();
    }

    /**
     * Constructor
     */

    public UserDetailPresenter(UserDetailPresenterListener listener) {
        this.mListener = listener;
        this.mService = new FirebaseService();
    }

    /**
     * Methods
     */
    public void getUserInfo(String id) {
        mService.getAPI().getUserDetails(id).enqueue(new Callback<_source>() {
            @Override
            public void onResponse(@NonNull Call<_source> call,@NonNull Response<_source> response) {
                if (response.body() != null) {
                    mListener.userInfoReady(response.body());
                } else {
                    mListener.presentError();
                }

            }

            @Override
            public void onFailure(Call<_source> call, Throwable t) {

            }
        });
    }
}
