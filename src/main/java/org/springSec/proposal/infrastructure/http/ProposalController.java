package org.springSec.proposal.infrastructure.http;

import java.util.List;

import org.springSec.auth.domain.UserRole;
import org.springSec.proposal.application.CreateProposalUseCase;
import org.springSec.proposal.application.ListProposalsUseCase;
import org.springSec.proposal.application.list.AccessScope;
import org.springSec.proposal.domain.Owner;
import org.springSec.proposal.domain.OwnerId;
import org.springSec.proposal.infrastructure.http.request.CreateProposalRequest;
import org.springSec.proposal.infrastructure.http.response.ProposalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/proposals")
public class ProposalController {
    private final CreateProposalUseCase createProposalUseCase;
    private final ListProposalsUseCase listProposalsUseCase;

    public ProposalController(CreateProposalUseCase createProposalUseCase,
                            ListProposalsUseCase listProposalsUseCase) {
        this.createProposalUseCase = createProposalUseCase;
        this.listProposalsUseCase = listProposalsUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('USERNAME')")
    public ResponseEntity<ProposalResponse> createProposal(@RequestBody CreateProposalRequest request,
                                                          @AuthenticationPrincipal org.springSec.auth.infrastructure.persistence.entity.User user) {
        var owner = new Owner(new OwnerId(user.getId()), user.getUsername());
        var output = this.createProposalUseCase.execute(request.toInput(), owner);

        return ResponseEntity.ok(ProposalResponse.from(output));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USERNAME','BRAND')")
    public List<ProposalResponse> findAllProposals(@AuthenticationPrincipal org.springSec.auth.infrastructure.persistence.entity.User user) {
        var accessScope = getAccessScope(user.getRole());
        var ownerId = new OwnerId(user.getId());

        return listProposalsUseCase.execute(accessScope, ownerId)
                .stream()
                .map(ProposalResponse::from)
                .toList();
    }

    private static AccessScope getAccessScope(UserRole role) {
        return switch (role) {
            case ROLE_USERNAME -> AccessScope.OWN;
            case ROLE_BRAND -> AccessScope.ALL;
        };
    }
}
