package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {

    private FetchUserHttpEndpointSync fetchUserHttpEndpointSync;
    private UsersCache usersCache;

    public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache usersCache) {
        this.fetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        this.usersCache = usersCache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {

        FetchUserHttpEndpointSync.EndpointResult endpointResult;

        User user = usersCache.getUser(userId);

        if (isNotCached(user)) {

            try {
                endpointResult = fetchUserHttpEndpointSync.fetchUserSync(userId);
            } catch (NetworkErrorException e) {
                return new UseCaseResult(Status.NETWORK_ERROR, null);
            }

            if (isSuccess(endpointResult)) {

                usersCache.cacheUser(new User(endpointResult.getUserId(), endpointResult.getUsername()));
                return new UseCaseResult(Status.SUCCESS, new User(endpointResult.getUserId(), endpointResult.getUsername()));
            } else {
                return new UseCaseResult(Status.FAILURE, null);
            }

        } else {
            return new UseCaseResult(Status.SUCCESS, user);
        }


    }

    private boolean isNotCached(User user) {
        return user == null;
    }

    private boolean isSuccess(FetchUserHttpEndpointSync.EndpointResult endpointResult) {
        return endpointResult.getStatus().equals(FetchUserHttpEndpointSync.EndpointStatus.SUCCESS);
    }
}
