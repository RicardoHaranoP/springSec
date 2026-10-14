package org.springSec.proposal.application.list;

import java.util.List;

import org.springSec.proposal.domain.OwnerId;
import org.springSec.proposal.domain.Proposal;
import org.springSec.proposal.domain.ProposalRepository;
import org.springframework.stereotype.Service;

@Service 
public class OwnStrategy implements Strategy{
    private final ProposalRepository proposalRepository;

    public OwnStrategy (ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }
    
    @Override 
    public List<Proposal> getProposals(OwnerId ownerId) {
        return proposalRepository.findAllByOwner(ownerId);
    }

    @Override 
    public AccessScope getScope() {
        return AccessScope.OWN;
    }
}
