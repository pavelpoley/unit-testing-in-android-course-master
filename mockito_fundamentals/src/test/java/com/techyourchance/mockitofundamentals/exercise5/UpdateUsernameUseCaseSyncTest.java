package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.techyourchance.mockitofundamentals.exercise5.UpdateUsernameUseCaseSync.UseCaseResult;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {

    private static final String USER_ID = "user_id";
    private static final String USERNAME = "new_username";

    private UpdateUsernameUseCaseSync sut;

    @Mock
    private UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSyncMock;
    @Mock
    private EventBusPoster eventBusPosterMock;
    @Mock
    private UsersCache usersCacheMock;

    @Before
    public void setUp() throws Exception {

        sut = new UpdateUsernameUseCaseSync(updateUsernameHttpEndpointSyncMock, usersCacheMock, eventBusPosterMock);
        success();
    }



    @Test
    public void update_success_returnSuccess() throws Exception{
        UseCaseResult useCaseResult = sut.updateUsernameSync(USER_ID, USERNAME);

        assertThat(useCaseResult, is(UseCaseResult.SUCCESS));
    }




    @Test
    public void update_networkError_returnNetworkError() throws Exception{
        networkError();
        UseCaseResult result = sut.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }

    @Test
    public void update_authError_returnFailure() throws Exception{
        authError();
        UseCaseResult useCaseResult = sut.updateUsernameSync(USER_ID, USERNAME);

        assertThat(useCaseResult, is(UseCaseResult.FAILURE));
    }

    @Test
    public void update_generalError_returnFailure() throws Exception{
        generalError();
        UseCaseResult useCaseResult = sut.updateUsernameSync(USER_ID, USERNAME);

        assertThat(useCaseResult, is(UseCaseResult.FAILURE));
    }


    @Test
    public void update_success_verifyUserCached() throws Exception{
        sut.updateUsernameSync(USER_ID, USERNAME);

        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        Mockito.verify(usersCacheMock).cacheUser(ac.capture());

        List<User> captures = ac.getAllValues();
        assertThat(captures.get(0).getUserId(), is(USER_ID));
        assertThat(captures.get(0).getUsername(), is(USERNAME));

    }

    @Test
    public void update_serverError_UserNotCached() throws Exception{
        serverError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        Mockito.verifyNoMoreInteractions(usersCacheMock);

    }

    @Test
    public void update_authError_UserNotCached() throws Exception{
        authError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        Mockito.verifyNoMoreInteractions(usersCacheMock);

    }

    @Test
    public void update_generalError_UserNotCached() throws Exception{
        generalError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        Mockito.verifyNoMoreInteractions(usersCacheMock);

    }

    @Test
    public void update_networkError_UserNotCached() throws Exception{
        networkError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        Mockito.verifyNoMoreInteractions(usersCacheMock);

    }


    @Test
    public void update_sucess_eventBusTrigered() throws Exception{
        sut.updateUsernameSync(USER_ID, USERNAME);
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        Mockito.verify(eventBusPosterMock).postEvent(ac.capture());
        assertThat(ac.getAllValues().get(0),instanceOf(UserDetailsChangedEvent.class));
    }


    @Test
    public void update_networkError_eventBusNotTrigered() throws Exception{
        networkError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        Mockito.verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void update_generalError_eventBusNotTrigered() throws Exception{
        generalError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        Mockito.verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void update_serverError_eventBusNotTrigered() throws Exception{
        serverError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        Mockito.verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void update_authError_eventBusNotTrigered() throws Exception{
        authError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        Mockito.verifyNoMoreInteractions(eventBusPosterMock);
    }


    @Test
    public void update_success_verifyIdAndUsernamePassedTOEndPoint() throws Exception{
        sut.updateUsernameSync(USER_ID, USERNAME);

        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        Mockito.verify(updateUsernameHttpEndpointSyncMock).updateUsername(ac.capture(), ac.capture());

        List<String> captures = ac.getAllValues();
        assertThat(captures.get(0), is(USER_ID));
        assertThat(captures.get(1), is(USERNAME));
    }



    private void serverError() throws Exception{
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class),any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,"",""));
    }

    private void authError() throws Exception{
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class),any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,"",""));
    }

    private void generalError() throws Exception{
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class),any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,"",""));
    }



    private void success() throws Exception{
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS,USER_ID,USERNAME));
    }

    private void networkError() throws Exception {
        doThrow(new com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException())
                .when(updateUsernameHttpEndpointSyncMock).updateUsername(anyString(), anyString());
    }


}