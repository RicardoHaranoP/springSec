package org.springSec.proposal.application;

import java.util.List;

import org.springSec.proposal.application.list.AccessScope;
import org.springSec.proposal.application.list.Factory;
import org.springSec.proposal.application.output.ProposalOutput;
import org.springSec.proposal.domain.OwnerId;
import org.springframework.stereotype.Service;

@Service 
public class ListProposalsUseCase {
    private final Factory factory;

    public ListProposalsUseCase(Factory factory){
        this.factory = factory;
    }
    
    public List<ProposalOutput> execute(AccessScope scope, OwnerId ownerId) {
        var proposals = factory.getStrategy(scope).getProposals(ownerId);

        return proposals.stream().map(ProposalOutput::from).toList();
    }
}
