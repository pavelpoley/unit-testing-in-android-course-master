package com.techyourchance.testdrivendevelopment.exercise7.networking;

class FetchReputationUseCaseSync {

    private GetReputationHttpEndpointSync getReputationHttpEndpointSync;

    public FetchReputationUseCaseSync(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        this.getReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    public UseCaseResult getReputationSync() {

        GetReputationHttpEndpointSync.EndpointResult reputationSync = getReputationHttpEndpointSync.getReputationSync();

        if (reputationSync.getStatus().equals(GetReputationHttpEndpointSync.EndpointStatus.SUCCESS)) {
            return new UseCaseResult(Status.SUCCESS, reputationSync.getReputation());
        } else if (reputationSync.getStatus().equals(GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR)) {
            return new UseCaseResult(Status.FAILURE, 0);
        } else if (reputationSync.getStatus().equals(GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR)) {
            return new UseCaseResult(Status.FAILURE, 0);
        } else throw new RuntimeException("invalid status");


    }


    enum Status {SUCCESS, FAILURE}

    class UseCaseResult {


        Status status;
        int reputation;

        UseCaseResult(Status status, int reputation) {
            this.status = status;
            this.reputation = reputation;
        }
    }

}
