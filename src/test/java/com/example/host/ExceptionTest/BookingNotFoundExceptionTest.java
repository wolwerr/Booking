package com.example.host.ExceptionTest;

import com.example.host.Exception.BookingNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookingNotFoundExceptionTest {

    @Test
    public void test_instantiation_with_message_string() {
        BookingNotFoundException exception = new BookingNotFoundException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void test_inheritance_from_runtime_exception() {
        BookingNotFoundException exception = new BookingNotFoundException("Test message");
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void test_get_message_method() {
        BookingNotFoundException exception = new BookingNotFoundException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void test_none() {

        assertTrue(true);
    }

    @Test
    public void test_instantiation_without_message_string() {
        BookingNotFoundException exception = new BookingNotFoundException(null);
        assertNull(exception.getMessage());
    }

    @Test
    public void test_instantiation_with_null_message_string() {
        BookingNotFoundException exception = new BookingNotFoundException(null);
        assertNull(exception.getMessage());
    }

    @Test
    public void test_instantiation_with_empty_message_string() {
        BookingNotFoundException exception = new BookingNotFoundException("");
        assertEquals("", exception.getMessage());
    }

    @Test
    public void test_instantiation_with_special_characters_message_string() {
        BookingNotFoundException exception = new BookingNotFoundException("!@#$%^&*()");
        assertEquals("!@#$%^&*()", exception.getMessage());
    }

}
