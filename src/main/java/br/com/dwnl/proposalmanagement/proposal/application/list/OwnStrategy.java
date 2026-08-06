package br.com.dwnl.proposalmanagement.proposal.application.list;

import br.com.dwnl.proposalmanagement.proposal.domain.OwnerId;
import br.com.dwnl.proposalmanagement.proposal.domain.Proposal;
import br.com.dwnl.proposalmanagement.proposal.domain.ProposalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnStrategy implements Strategy{
    private final ProposalRepository proposalRepository;

    public OwnStrategy(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    @Override
    public List<Proposal> getProposals(OwnerId ownerId) {
        return proposalRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public AccessScope getScope() {
        return AccessScope.OWN;
    }
}
