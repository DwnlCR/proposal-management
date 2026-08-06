package br.com.dwnl.proposalmanagement.proposal.domain;

import java.util.List;

public interface ProposalRepository {
    List<Proposal> findAll();
    List<Proposal> findAllByOwnerId(OwnerId id);
    Proposal save(Proposal proposal);
}
