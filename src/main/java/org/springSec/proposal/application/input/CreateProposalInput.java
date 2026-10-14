package org.springSec.proposal.application.input;

import java.util.Optional;
import org.springSec.proposal.domain.Proposal;
import org.springSec.proposal.domain.Owner;

public record CreateProposalInput(String title, Optional<String> description) {
    public Proposal toDomain(Owner owner) {
        return new Proposal(title, description, owner);
    }
}
