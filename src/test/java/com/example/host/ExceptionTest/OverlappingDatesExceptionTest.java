package com.example.host.ExceptionTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.host.exceptions.OverlappingDatesException;

class OverlappingDatesExceptionTest {

    @Test
    void test_instantiation_with_message_string() {
        OverlappingDatesException exception = new OverlappingDatesException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    void test_inheritance_from_RuntimeException() {
        OverlappingDatesException exception = new OverlappingDatesException("Test message");
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void test_getMessage_method() {
        OverlappingDatesException exception = new OverlappingDatesException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    void test_instantiation_without_message_string() {
        OverlappingDatesException exception = new OverlappingDatesException(null);
        assertNull(exception.getMessage());
    }

    @Test
    void test_instantiation_with_null_message_string() {
        OverlappingDatesException exception = new OverlappingDatesException(null);
        assertNull(exception.getMessage());
    }

    @Test
    void test_instantiation_with_empty_message_string() {
        OverlappingDatesException exception = new OverlappingDatesException("");
        assertEquals("", exception.getMessage());
    }

    @Test
    void test_instantiation_with_special_characters_message_string() {
        OverlappingDatesException exception = new OverlappingDatesException("!@#$%^&*()");
        assertEquals("!@#$%^&*()", exception.getMessage());
    }


}