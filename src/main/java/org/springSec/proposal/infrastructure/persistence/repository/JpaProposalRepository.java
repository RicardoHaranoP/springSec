package org.springSec.proposal.infrastructure.persistence.repository;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springSec.proposal.domain.OwnerId;
import org.springSec.proposal.domain.Proposal;
import org.springSec.proposal.domain.ProposalRepository;
import org.springSec.proposal.infrastructure.persistence.entity.ProposalEntity;
import org.springframework.stereotype.Repository;

@Repository 
public class JpaProposalRepository implements ProposalRepository{
    private final ProposalEntityRepository proposalEntityRepository;

    public JpaProposalRepository(ProposalEntityRepository proposalEntityRepository) {
        this.proposalEntityRepository = proposalEntityRepository;
    }
    
    @Override 
    public List<Proposal> findAll() {
        var iterable = proposalEntityRepository.findAll();
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(ProposalEntity::toDomain)
                .toList();
    }

    @Override 
    public List<Proposal> findAllByOwner(OwnerId ownerId) {
        return proposalEntityRepository.findAllByOwnerId(ownerId.id())
                .stream()
                .map(ProposalEntity::toDomain)
                .toList();
    }

    @Override 
    public Proposal save(Proposal proposal) {
        var entity = ProposalEntity.from(proposal);
        var saved = proposalEntityRepository.save(entity);
        
        return saved.toDomain();
    }
}
