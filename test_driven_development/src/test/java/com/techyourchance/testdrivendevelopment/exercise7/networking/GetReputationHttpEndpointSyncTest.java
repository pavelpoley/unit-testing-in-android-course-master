package com.techyourchance.testdrivendevelopment.exercise7.networking;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetReputationHttpEndpointSyncTest {

    private static final int REPUTATION = 10;
    public static final int NO_REPUTATION = 0;

    private FetchReputationUseCaseSync sut;

    @Mock
    private GetReputationHttpEndpointSync getReputationHttpEndpointSyncMock;

    @Before
    public void setUp() throws Exception {
        sut = new FetchReputationUseCaseSync(getReputationHttpEndpointSyncMock);

        success();
    }

    @Test
    public void getRep_success_returnSuccess() {

        FetchReputationUseCaseSync.UseCaseResult result = sut.getReputationSync();

        assertThat(result.status, is(FetchReputationUseCaseSync.Status.SUCCESS));
    }

    @Test
    public void getRep_success_returnReputation() {
        FetchReputationUseCaseSync.UseCaseResult result = sut.getReputationSync();

        assertThat(result.reputation,is(REPUTATION));
    }

    @Test
    public void getRep_generalError_returnFailure() {
        generalError();
        FetchReputationUseCaseSync.UseCaseResult result = sut.getReputationSync();

        assertThat(result.status,is(FetchReputationUseCaseSync.Status.FAILURE));
    }
    @Test
    public void getRep_networkError_returnFailure() {
        networkError();
        FetchReputationUseCaseSync.UseCaseResult result = sut.getReputationSync();

        assertThat(result.status,is(FetchReputationUseCaseSync.Status.FAILURE));
    }

    @Test
    public void getRep_generalError_returnReputationZero() {
        generalError();
        FetchReputationUseCaseSync.UseCaseResult result = sut.getReputationSync();

        assertThat(result.reputation,is(NO_REPUTATION));
    }

    @Test
    public void getRep_networkError_returnReputationZero() {
        networkError();
        FetchReputationUseCaseSync.UseCaseResult result = sut.getReputationSync();

        assertThat(result.reputation,is(NO_REPUTATION));
    }



    private void success() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.SUCCESS, REPUTATION));
    }

    private void generalError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR, REPUTATION));
    }

    private void networkError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR, REPUTATION));
    }

    private void nullError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(null, REPUTATION));
    }
}