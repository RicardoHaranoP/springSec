package org.springSec.proposal.application.output;

import java.util.Optional;
import org.springSec.proposal.domain.Proposal;

public record ProposalOutput(String id, String title, Optional<String> description, String ownerId, String ownerName) {
    public static ProposalOutput from (Proposal proposal) {
        return new ProposalOutput(proposal.getId().id().toString(),
            proposal.getTitle(),
            proposal.getDescription(),
            proposal.getOwner().id().toString(),
            proposal.getOwner().name());
    }
}
