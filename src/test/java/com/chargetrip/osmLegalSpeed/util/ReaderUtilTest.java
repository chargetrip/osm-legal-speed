package com.chargetrip.osmLegalSpeed.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

public class ReaderUtilTest {
    @Test
    @DisplayName("ReaderUtil readInputStreamContent success")
    void testReadInputStreamContentSuccess() {
        new ReaderUtil();

        MockedConstruction<InputStreamReader> mockedInputStreamReader = mockConstruction(InputStreamReader.class,
                (instance, context) -> {
                });
        MockedConstruction<BufferedReader> mockedBufferedReader = mockConstruction(BufferedReader.class,
                (instance, context) -> {
                    when(instance.readLine()).thenAnswer(new Answer<String>() {
                        private int count = 0;

                        public String answer(InvocationOnMock invocation) {
                            if (count++ == 0) {
                                return "{}";
                            }

                            return null;
                        }
                    });
                });

        String content = ReaderUtil.readInputStreamContent(null);

        assertEquals("{}", content);

        mockedBufferedReader.close();
        mockedInputStreamReader.close();
    }

    @Test
    @DisplayName("ReaderUtil readInputStreamContent fail")
    void testReadInputStreamContentFail() {
        MockedConstruction<InputStreamReader> mockedInputStreamReader = mockConstruction(InputStreamReader.class,
                (instance, context) -> {
                });
        MockedConstruction<BufferedReader> mockedBufferedReader = mockConstruction(BufferedReader.class,
                (instance, context) -> {
                    when(instance.readLine()).thenThrow(new IOException("Fail"));
                });

        String content = ReaderUtil.readInputStreamContent(null);

        assertEquals("", content);

        mockedBufferedReader.close();
        mockedInputStreamReader.close();
    }
}
