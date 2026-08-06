package br.com.dwnl.proposalmanagement.proposal.application;

import br.com.dwnl.proposalmanagement.proposal.application.input.CreateProposalInput;
import br.com.dwnl.proposalmanagement.proposal.application.output.ProposalOutput;
import br.com.dwnl.proposalmanagement.proposal.domain.Owner;
import br.com.dwnl.proposalmanagement.proposal.domain.ProposalRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateProposalUseCase {
    private final ProposalRepository proposalRepository;

    public CreateProposalUseCase(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    public ProposalOutput execute(CreateProposalInput input, Owner owner){
        var proposal = input.toDomain(owner);
        var saved = proposalRepository.save(proposal);

        return ProposalOutput.from(saved);
    }
}
