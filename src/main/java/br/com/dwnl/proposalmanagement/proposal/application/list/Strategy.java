package br.com.dwnl.proposalmanagement.proposal.application.list;

import br.com.dwnl.proposalmanagement.proposal.domain.OwnerId;
import br.com.dwnl.proposalmanagement.proposal.domain.Proposal;

import java.util.List;

public interface Strategy {
    List<Proposal> getProposals(OwnerId ownerId);
    AccessScope getScope();
}
