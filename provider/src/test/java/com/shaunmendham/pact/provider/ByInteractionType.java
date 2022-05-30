package com.shaunmendham.pact.provider;

import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.v4.V4InteractionType;
import au.com.dius.pact.provider.junitsupport.filter.InteractionFilter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ByInteractionType<I extends Interaction> implements InteractionFilter<I> {

    @Override
    public Predicate<I> buildPredicate(String[] values) {

        List<String> supportedInteractionTypes = Arrays.asList(values);

        boolean synchronousHttpSupported = supportedInteractionTypes.contains(V4InteractionType.SynchronousHTTP.toString());
        boolean asynchronousMessagesSupported = supportedInteractionTypes.contains(V4InteractionType.AsynchronousMessages.toString());
        boolean synchronousMessagesSupported = supportedInteractionTypes.contains(V4InteractionType.SynchronousMessages.toString());

        return interaction -> (interaction.isSynchronousRequestResponse() && synchronousHttpSupported) ||
            (interaction.isAsynchronousMessage() && asynchronousMessagesSupported) ||
            (interaction.isSynchronousMessages() && synchronousMessagesSupported);
    }
}

