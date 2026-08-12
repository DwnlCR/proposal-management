package br.com.dwnl.proposalmanagement.proposal.infrastructure.https.request;

import br.com.dwnl.proposalmanagement.proposal.application.input.CreateProposalInput;

import java.util.Optional;

public record CreateProposalRequest(String title, Optional<String> description) {
    public CreateProposalInput toInput(){
        return new CreateProposalInput(title, description);
    }
}
