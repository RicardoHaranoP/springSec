package org.springSec.proposal.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springSec.proposal.infrastructure.persistence.entity.ProposalEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProposalEntityRepository extends CrudRepository<ProposalEntity, UUID>{
    List<ProposalEntity> findAllByOwnerId(UUID ownerId);
}
