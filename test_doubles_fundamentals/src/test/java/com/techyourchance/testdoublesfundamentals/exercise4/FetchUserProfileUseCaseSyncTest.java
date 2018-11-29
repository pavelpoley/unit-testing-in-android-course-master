package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import static com.techyourchance.testdoublesfundamentals.exercise4.FetchUserProfileUseCaseSync.UseCaseResult;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class FetchUserProfileUseCaseSyncTest {

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "pavel";
    public static final String IMAGE_URI = "image_uri";

    FetchUserProfileUseCaseSync userProfileUseCaseSync;
    UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
    UsersCacheTd usersCacheTd = new UsersCacheTd();

    @Before
    public void setUp() throws Exception {

        userProfileUseCaseSync = new FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, usersCacheTd);


    }

    @Test
    public void userProfile_success_returnSuccess() {

        userProfileHttpEndpointSyncTd.isSuccess = true;
        UseCaseResult useCaseResult = userProfileUseCaseSync.fetchUserProfileSync(USER_ID);

        assertThat(useCaseResult, is(UseCaseResult.SUCCESS));
    }

    @Test
    public void userProfile_isAuthError_returnAuthError() {

        userProfileHttpEndpointSyncTd.isAuthError = true;
        UseCaseResult useCaseResult = userProfileUseCaseSync.fetchUserProfileSync(USER_ID);

        assertThat(useCaseResult, is(UseCaseResult.FAILURE));

    }

    @Test
    public void userProfile_isServerError_returnServerError() {

        userProfileHttpEndpointSyncTd.isServerError = true;
        UseCaseResult useCaseResult = userProfileUseCaseSync.fetchUserProfileSync(USER_ID);

        assertThat(useCaseResult, is(UseCaseResult.FAILURE));

    }

    @Test
    public void userProfile_isGeneralError_returnGeneralError() {

        userProfileHttpEndpointSyncTd.isGeneralError = true;
        UseCaseResult useCaseResult = userProfileUseCaseSync.fetchUserProfileSync(USER_ID);

        assertThat(useCaseResult, is(UseCaseResult.FAILURE));

    }


    @Test
    public void userProfile_isNetworkError_returnGeneralError() {

        userProfileHttpEndpointSyncTd.isNetworkError = true;
        UseCaseResult useCaseResult = userProfileUseCaseSync.fetchUserProfileSync(USER_ID);

        assertThat(useCaseResult, is(UseCaseResult.NETWORK_ERROR));

    }

    @Test
    public void userProfile_isCorrectUser_returnTrue() {

        userProfileHttpEndpointSyncTd.isSuccess = true;
        UseCaseResult useCaseResult = userProfileUseCaseSync.fetchUserProfileSync(USER_ID);

        assertThat(usersCacheTd.user.getUserId(), is(USER_ID));
        assertThat(usersCacheTd.user.getFullName(), is(USER_NAME));
        assertThat(usersCacheTd.user.getImageUrl(), is(IMAGE_URI));

    }


    class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {

        public boolean isSuccess;
        public boolean isAuthError;
        public boolean isGeneralError;
        public boolean isServerError;
        public boolean isNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {

            if (isSuccess){
                return new EndpointResult(EndpointResultStatus.SUCCESS, userId, USER_NAME, IMAGE_URI);
            }else if(isAuthError){
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, userId, "", "");
            }else if(isGeneralError){
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, userId, "", "");
            }else if(isServerError){
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, userId, "", "");
            }else if(isNetworkError){
                throw new NetworkErrorException();
            }else {
                return null;

            }
        }
    }

    class UsersCacheTd implements UsersCache {
        User user;

        @Override
        public void cacheUser(User user) {
            this.user = user;
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            return user;
        }
    }
}