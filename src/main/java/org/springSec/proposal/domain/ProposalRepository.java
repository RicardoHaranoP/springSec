package org.springSec.proposal.domain;

import java.util.List;

public interface ProposalRepository {
    List<Proposal> findAll();
    List<Proposal> findAllByOwner(OwnerId ownerId);
    Proposal save(Proposal proposal);
    
}
