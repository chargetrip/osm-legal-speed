package com.chargetrip.osmLegalSpeed.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class RegexOrSet {
    /**
     * Pattern to identify the regular expressions
     */
    private static final Pattern anyRegexStuffExceptPipe = Pattern.compile("[.\\[\\]{}()<>*+=!?^$-]", Pattern.UNICODE_CASE);

    /**
     * Checking if the string matches the set of rules
     *
     * @param string The string to check against the rules
     */
    public abstract boolean matches(String string);

    /**
     * Extracting the type of rules we need for a specific input string
     *
     * @param string The specific rules string
     */
    public static RegexOrSet from(String string) {
        if (!anyRegexStuffExceptPipe.matcher(string).find()) {
            if (string.contains("|")) {
                Set<String> items = new HashSet<>(Arrays.asList(string.split("\\|")));

                return new SetRegex(items);
            }

            return new SetRegex(new HashSet<>(List.of(string)));
        }

        return new RealRegex(Pattern.compile(string));
    }

    public static class RealRegex extends RegexOrSet {
        private final Pattern regex;

        public RealRegex(Pattern regex) {
            this.regex = regex;
        }

        @Override
        public boolean matches(String string) {
            return regex.matcher(string).find();
        }
    }

    public static class SetRegex extends RegexOrSet {
        private final Set<String> set;
        public SetRegex(Set<String> set) {
            this.set = set;
        }

        @Override
        public boolean matches(String string) {
            return set.contains(string);
        }
    }

}
