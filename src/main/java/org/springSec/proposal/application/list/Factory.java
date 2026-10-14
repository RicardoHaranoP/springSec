package org.springSec.proposal.application.list;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component 
public class Factory {
    private final Map<AccessScope, Strategy> strategies;
    public Factory(List<Strategy> strategies){
        this.strategies = strategies.stream().collect(Collectors.toMap(Strategy::getScope, Function.identity()));
    }

    public Strategy getStrategy(AccessScope scope) {
        return strategies.get(scope);
    }
}
