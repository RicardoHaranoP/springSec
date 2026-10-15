package org.springSec.proposal.application;

import org.springSec.proposal.application.input.CreateProposalInput;
import org.springSec.proposal.application.output.ProposalOutput;
import org.springSec.proposal.domain.Owner;
import org.springSec.proposal.domain.ProposalRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateProposalUseCase {
    private final ProposalRepository proposalRepository;

    public CreateProposalUseCase(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }
    
    public ProposalOutput execute(CreateProposalInput input, Owner owner) {
        var proposal = input.toDomain(owner);
        var saved = proposalRepository.save(proposal);

        return ProposalOutput.from(saved);
    }
}
