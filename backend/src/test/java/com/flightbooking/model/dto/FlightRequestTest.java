package com.flightbooking.model.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlightRequestTest {

    // ─────────────────────────────────────────────────────────────
    // NORMAL cases – valid IATA codes that must NOT throw
    // ─────────────────────────────────────────────────────────────

    @Test
    void validate_withValidThreeLetterCodes_doesNotThrow() {
        FlightRequest request = new FlightRequest("CDG", "JFK");
        assertDoesNotThrow(request::validate);
    }

    @Test
    void validate_withValidOneLetterCodes_doesNotThrow() {
        FlightRequest request = new FlightRequest("C", "J");
        assertDoesNotThrow(request::validate);
    }

    @Test
    void validate_withValidTwoLetterCodes_doesNotThrow() {
        FlightRequest request = new FlightRequest("LA", "NY");
        assertDoesNotThrow(request::validate);
    }

    @Test
    void validate_withBothCodesNull_doesNotThrow() {
        FlightRequest request = new FlightRequest(null, null);
        assertDoesNotThrow(request::validate);
    }

    @Test
    void validate_withOnlyDepIata_doesNotThrow() {
        FlightRequest request = new FlightRequest("CDG", null);
        assertDoesNotThrow(request::validate);
    }

    @Test
    void validate_withOnlyArrIata_doesNotThrow() {
        FlightRequest request = new FlightRequest(null, "JFK");
        assertDoesNotThrow(request::validate);
    }

    // ─────────────────────────────────────────────────────────────
    // EDGE cases – blank strings treated as "not provided"
    // ─────────────────────────────────────────────────────────────

    @Test
    void validate_withBlankDepIata_doesNotThrow() {
        // Blank is explicitly skipped by the isBlank() guard in validate()
        FlightRequest request = new FlightRequest("   ", "JFK");
        assertDoesNotThrow(request::validate);
    }

    @Test
    void validate_withBlankArrIata_doesNotThrow() {
        FlightRequest request = new FlightRequest("CDG", "  ");
        assertDoesNotThrow(request::validate);
    }

    @Test
    void validate_withBothBlank_doesNotThrow() {
        FlightRequest request = new FlightRequest("", "");
        assertDoesNotThrow(request::validate);
    }

    // ─────────────────────────────────────────────────────────────
    // ERROR cases – invalid codes must throw IllegalArgumentException
    // ─────────────────────────────────────────────────────────────

    @Test
    void validate_withLowercaseDepIata_throwsIllegalArgumentException() {
        FlightRequest request = new FlightRequest("cdg", "JFK");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                request::validate
        );
        assertEquals("Invalid dep_iata: must be 1 to 3 uppercase letters (e.g. C or CDG)", ex.getMessage());
    }

    @Test
    void validate_withLowercaseArrIata_throwsIllegalArgumentException() {
        FlightRequest request = new FlightRequest("CDG", "jfk");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                request::validate
        );
        assertEquals("Invalid arr_iata: must be 1 to 3 uppercase letters (e.g. J or JFK)", ex.getMessage());
    }

    @Test
    void validate_withTooLongDepIata_throwsIllegalArgumentException() {
        // 4 letters → fails [A-Z]{1,3}
        FlightRequest request = new FlightRequest("CDGG", "JFK");
        assertThrows(IllegalArgumentException.class, request::validate);
    }

    @Test
    void validate_withTooLongArrIata_throwsIllegalArgumentException() {
        FlightRequest request = new FlightRequest("CDG", "JFKK");
        assertThrows(IllegalArgumentException.class, request::validate);
    }

    @Test
    void validate_withDigitsInDepIata_throwsIllegalArgumentException() {
        FlightRequest request = new FlightRequest("C1G", "JFK");
        assertThrows(IllegalArgumentException.class, request::validate);
    }

    @Test
    void validate_withSpecialCharInArrIata_throwsIllegalArgumentException() {
        FlightRequest request = new FlightRequest("CDG", "J!K");
        assertThrows(IllegalArgumentException.class, request::validate);
    }

    // ─────────────────────────────────────────────────────────────
    // Getters – sanity checks
    // ─────────────────────────────────────────────────────────────

    @Test
    void getDepIata_returnsConstructorValue() {
        FlightRequest request = new FlightRequest("CDG", "JFK");
        assertEquals("CDG", request.getDepIata());
    }

    @Test
    void getArrIata_returnsConstructorValue() {
        FlightRequest request = new FlightRequest("CDG", "JFK");
        assertEquals("JFK", request.getArrIata());
    }
}