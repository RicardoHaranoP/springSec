package org.springSec.proposal.application.list;

import java.util.List;

import org.springSec.proposal.domain.OwnerId;
import org.springSec.proposal.domain.Proposal;

public interface Strategy {
    List<Proposal> getProposals(OwnerId ownerId);
    AccessScope getScope();
}
