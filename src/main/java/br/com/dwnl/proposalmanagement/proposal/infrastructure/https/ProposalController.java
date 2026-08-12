package br.com.dwnl.proposalmanagement.proposal.infrastructure.https;

import br.com.dwnl.proposalmanagement.auth.domain.UserRole;
import br.com.dwnl.proposalmanagement.auth.infrastructure.persistence.entity.User;
import br.com.dwnl.proposalmanagement.proposal.application.CreateProposalUseCase;
import br.com.dwnl.proposalmanagement.proposal.application.ListProposalUseCase;
import br.com.dwnl.proposalmanagement.proposal.application.list.AccessScope;
import br.com.dwnl.proposalmanagement.proposal.domain.Owner;
import br.com.dwnl.proposalmanagement.proposal.domain.OwnerId;
import br.com.dwnl.proposalmanagement.proposal.infrastructure.https.request.CreateProposalRequest;
import br.com.dwnl.proposalmanagement.proposal.infrastructure.https.response.ProposalResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proposals")
public class ProposalController {
    private final CreateProposalUseCase createProposalUseCase;
    private final ListProposalUseCase listProposalUseCase;

    public ProposalController(CreateProposalUseCase createProposalUseCase, ListProposalUseCase listProposalUseCase) {
        this.createProposalUseCase = createProposalUseCase;
        this.listProposalUseCase = listProposalUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('INFLUENCER')")
    public ProposalResponse createProposal(@RequestBody CreateProposalRequest request,
                                            @AuthenticationPrincipal User user){
        var owner = new Owner(new OwnerId(user.getId()), user.getUsername());
        var output = this.createProposalUseCase.execute(request.toInput(), owner);

        return ProposalResponse.from(output);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('INFLUENCER', 'BRAND')")
    public List<ProposalResponse> findAllProposals(@AuthenticationPrincipal User user){
        var accessScope = getAccessScope(user.getRole());
        var ownerId = new OwnerId(user.getId());
        return listProposalUseCase.execute(accessScope, ownerId).stream()
                .map(ProposalResponse::from)
                .toList();

    }

    private static AccessScope getAccessScope(UserRole role){
        return switch (role){
            case ROLE_INFLUENCER -> AccessScope.OWN;
            case ROLE_BRAND -> AccessScope.ALL;
        };
    }
}
