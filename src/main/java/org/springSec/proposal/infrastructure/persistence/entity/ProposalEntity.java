package org.springSec.proposal.infrastructure.persistence.entity;

import java.util.Optional;
import java.util.UUID;

import org.springSec.proposal.domain.Owner;
import org.springSec.proposal.domain.OwnerId;
import org.springSec.proposal.domain.Proposal;
import org.springSec.proposal.domain.ProposalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static ProposalEntity from(Proposal proposal) {
        return new ProposalEntity(
                proposal.getId().id(),
                proposal.getTitle(),
                proposal.getDescription().orElse(null),
                proposal.getOwner().id().id(),
                proposal.getOwner().name());
    }

    public Proposal toDomain() {
        return new Proposal(
                new ProposalId(this.id),
                this.title,
                Optional.ofNullable(this.description),
                new Owner(new OwnerId(this.ownerId), this.ownerName));
    }
}
