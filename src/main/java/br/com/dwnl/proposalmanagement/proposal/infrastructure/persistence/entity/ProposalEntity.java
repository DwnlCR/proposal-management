package br.com.dwnl.proposalmanagement.proposal.infrastructure.persistence.entity;

import br.com.dwnl.proposalmanagement.proposal.domain.Owner;
import br.com.dwnl.proposalmanagement.proposal.domain.OwnerId;
import br.com.dwnl.proposalmanagement.proposal.domain.Proposal;
import br.com.dwnl.proposalmanagement.proposal.domain.ProposalId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private String ownerName;

    public static ProposalEntity from(Proposal proposal){
        return new ProposalEntity(
                proposal.getId().id(),
                proposal.getTitle(),
                proposal.getDescription().orElse(null),
                proposal.getOwner().id().id(),
                proposal.getOwner().name()
        );
    }

    public static Proposal toDomain(ProposalEntity proposalEntity){
        return new Proposal(
                new ProposalId(proposalEntity.id),
                proposalEntity.title,
                Optional.ofNullable(proposalEntity.description),
                new Owner(new OwnerId(proposalEntity.ownerId), proposalEntity.ownerName)
        );
    }
}
