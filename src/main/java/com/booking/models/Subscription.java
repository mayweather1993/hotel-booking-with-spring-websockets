package com.booking.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

@Data
@AllArgsConstructor
public class Subscription {

    private Set<String> urisListening;

    public boolean listening(final String uri) {
        return urisListening.stream()
                .anyMatch(uriInStream -> StringUtils.equalsIgnoreCase(uriInStream, uri));
    }

}
