package org.springSec.proposal.infrastructure.http.request;

import java.util.Optional;

import org.springSec.proposal.application.input.CreateProposalInput;

public record CreateProposalRequest (String title, Optional<String> description){
    public CreateProposalInput toInput() {
        return new CreateProposalInput(title, description);
    }
}
