package com.example.host.ExceptionTest;

import com.example.host.Exception.BlockNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlockNotFoundExceptionTest {

        @Test
        public void test_creating_instance_with_message_should_set_message_correctly() {
            String message = "Test message";
            BlockNotFoundException exception = new BlockNotFoundException(message);
            assertEquals(message, exception.getMessage());
        }

        @Test
        public void test_throwing_exception_with_message_should_result_in_correct_message() {
            String message = "Test message";
            try {
                throw new BlockNotFoundException(message);
            } catch (BlockNotFoundException exception) {
                assertEquals(message, exception.getMessage());
            }
        }

        @Test
        public void test_catching_exception_and_checking_message_should_return_correct_message() {
            String message = "Test message";
            try {
                throw new BlockNotFoundException(message);
            } catch (BlockNotFoundException exception) {
                assertEquals(message, exception.getMessage());
            }
        }

        @Test
        public void test_creating_instance_with_null_message_should_throw_null_pointer_exception() {
            String message = null;
            new BlockNotFoundException(message);
        }

        @Test
        public void test_creating_instance_with_empty_message_should_not_throw_exception() {
            String message = "";
            BlockNotFoundException exception = new BlockNotFoundException(message);
            assertEquals(message, exception.getMessage());
        }

        @Test
        public void test_creating_instance_with_whitespace_message_should_not_throw_exception() {
            String message = "   ";
            BlockNotFoundException exception = new BlockNotFoundException(message);
            assertEquals(message, exception.getMessage());
        }

        @Test
        public void test_creating_instance_with_long_message_should_throw_illegal_argument_exception() {
            String message = "This is a very long message that exceeds the maximum allowed length";
            new BlockNotFoundException(message);
        }

    }
