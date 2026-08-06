package br.com.dwnl.proposalmanagement.proposal.application.input;

import br.com.dwnl.proposalmanagement.proposal.domain.Owner;
import br.com.dwnl.proposalmanagement.proposal.domain.Proposal;

import java.util.Optional;

public record CreateProposalInput(String title, Optional<String> description) {
    public Proposal toDomain(Owner owner){
        return new Proposal(title, description, owner);
    }
}
