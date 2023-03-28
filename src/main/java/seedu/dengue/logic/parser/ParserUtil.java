package seedu.dengue.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.dengue.commons.core.index.Index;
import seedu.dengue.commons.util.StringUtil;
import seedu.dengue.logic.parser.exceptions.ParseException;
import seedu.dengue.model.person.Age;
import seedu.dengue.model.person.Date;
import seedu.dengue.model.person.Name;
import seedu.dengue.model.person.Postal;
import seedu.dengue.model.person.SubPostal;
import seedu.dengue.model.predicate.PredicateUtil;
import seedu.dengue.model.variant.Variant;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        if (!PredicateUtil.isNameValid(name)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        String trimmedName = name.trim();
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String Postal} into a {@code Postal}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Postal} is invalid.
     */
    public static Postal parsePostal(String postal) throws ParseException {
        if (!PredicateUtil.isPostalValid(postal)) {
            throw new ParseException(Postal.MESSAGE_CONSTRAINTS);
        }
        String trimmedPostal = postal.trim();
        return new Postal(trimmedPostal);
    }


    /**
     * Parses a {@code String age} into an {@code Age}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code age} is invalid.
     */
    public static Age parseAge(String age) throws ParseException {
        if (!PredicateUtil.isAgeValid(age)) {
            throw new ParseException(Age.MESSAGE_CONSTRAINTS);
        }
        String trimmedAge = age.trim();
        return new Age(trimmedAge);
    }

    /**
     * Parses a {@code String date} into an {@code Date}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code date} is invalid.
     */
    public static Date parseDate(String date) throws ParseException {
        if (!PredicateUtil.isDateValid(date)) {
            throw new ParseException(Date.MESSAGE_CONSTRAINTS);
        }
        String trimmedDate = date.trim();
        return new Date(date);
    }

    /**
     * Parses a {@code String variant} into a {@code Variant}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code variant} is invalid.
     */
    public static Variant parseVariant(String variant) throws ParseException {
        if (!PredicateUtil.isVariantValid(variant)) {
            throw new ParseException(Variant.MESSAGE_CONSTRAINTS);
        }
        String trimmedVariant = variant.trim();
        return new Variant(trimmedVariant);
    }

    /**
     * Parses {@code Collection<String> variants} into a {@code Set<Variant>}.
     *
     * @throws ParseException if the given {@code Set<Variant>} is invalid.
     */
    public static Set<Variant> parseVariants(Collection<String> variants) throws ParseException {
        requireNonNull(variants);
        final Set<Variant> variantSet = new HashSet<>();
        for (String variantName : variants) {
            variantSet.add(parseVariant(variantName));
        }
        return variantSet;
    }

    /**
     * Parses a {@code Optional<String> name} into a {@code Optional<Name>}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Optional<Name> parseOptionalName(Optional<String> name) throws ParseException {
        if (name.isPresent()) {
            if (!PredicateUtil.isNameValid(name.get())) {
                throw new ParseException(Name.MESSAGE_CONSTRAINTS);
            }
            String trimmedName = name.get().trim();
            return Optional.of(new Name(trimmedName));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Parses a {@code Optional<String> Postal} into a {@code Optional<Postal>}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code postal} is invalid.
     */
    public static Optional<SubPostal> parseOptionalSubPostal(Optional<String> subPostal) throws ParseException {
        if (subPostal.isPresent()) {
            if (!PredicateUtil.isSubPostalValid(subPostal.get())) {
                throw new ParseException(SubPostal.MESSAGE_CONSTRAINTS);
            }
            String trimmedSubPostal = subPostal.get().trim();
            return Optional.of(new SubPostal(trimmedSubPostal));
        } else {
            return Optional.empty();
        }
    }


    /**
     * Parses a {@code Optional<String> age} into an {@code Optional<Age>}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code age} is invalid.
     */
    public static Optional<Age> parseOptionalAge(Optional<String> age) throws ParseException {
        if (age.isPresent()) {
            if (!PredicateUtil.isAgeValid(age.get())) {
                throw new ParseException(Age.MESSAGE_CONSTRAINTS);
            }
            String trimmedAge = age.get().trim();
            return Optional.of(new Age(trimmedAge));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Parses a {@code Optional<String> date} into an {@code Optional<Date>}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code date} is invalid.
     */
    public static Optional<Date> parseOptionalDate(Optional<String> date) throws ParseException {
        if (date.isPresent()) {
            if (!PredicateUtil.isDateValid(date.get())) {
                throw new ParseException(Date.MESSAGE_CONSTRAINTS);
            }
            String trimmedDate = date.get().trim();
            return Optional.of(new Date(trimmedDate));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Parses a {@code Optional<String> variant} into a {@code Optional<Variant>}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code variant} is invalid.
     */
    public static Optional<Variant> parseOptionalVariant(Optional<String> variant) throws ParseException {
        if (variant.isPresent()) {
            if (!PredicateUtil.isVariantValid(variant.get())) {
                throw new ParseException(Variant.MESSAGE_CONSTRAINTS);
            }
            String trimmedVariant = variant.get().trim();
            return Optional.of(new Variant(trimmedVariant));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Parses {@code Collection<Optional<String>> variants} into a {@code Set<Optional<Variant>>}.
     *
     * @throws ParseException if the given {@code Set<Optional<Variant>>} is invalid.
     */
    public static Set<Optional<Variant>> parseOptionalVariants(Collection<Optional<String>> variants)
            throws ParseException {
        requireNonNull(variants);
        final Set<Optional<Variant>> variantSet = new HashSet<>();
        for (Optional<String> variantName : variants) {
            variantSet.add(parseOptionalVariant(variantName));
        }
        return variantSet;
    }
}
