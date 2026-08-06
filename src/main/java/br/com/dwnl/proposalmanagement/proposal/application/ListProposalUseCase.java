package br.com.dwnl.proposalmanagement.proposal.application;

import br.com.dwnl.proposalmanagement.proposal.application.list.AccessScope;
import br.com.dwnl.proposalmanagement.proposal.application.list.Factory;
import br.com.dwnl.proposalmanagement.proposal.application.output.ProposalOutput;
import br.com.dwnl.proposalmanagement.proposal.domain.OwnerId;
import br.com.dwnl.proposalmanagement.proposal.domain.Proposal;
import br.com.dwnl.proposalmanagement.proposal.domain.ProposalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListProposalUseCase {
    private final Factory factory;

    public ListProposalUseCase(Factory factory) {
        this.factory = factory;
    }

    public List<ProposalOutput> execute(AccessScope scope, OwnerId ownerId){
        var proposals = factory.getStrategy(scope).getProposals(ownerId);

        return proposals.stream().map(ProposalOutput::from).toList();
    }
}
