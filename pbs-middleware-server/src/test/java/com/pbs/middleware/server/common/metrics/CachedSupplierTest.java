package com.pbs.middleware.server.common.metrics;

import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("CachedSupplier tests")
class CachedSupplierTest {

    @Mock
    private Supplier<Integer> supplier;

    @BeforeEach
    void reset() {
        Mockito.reset(supplier);
    }

    @Test
    @DisplayName("In cache limit supplier is called only once")
    void in_cache_limit_test() throws InterruptedException {
        final Integer value= 1;
        when(supplier.get()).thenReturn(value);
        CachedSupplier<Integer> cache = new CachedSupplier<>(supplier, 6);

        Integer returned = cache.get();
        assertEquals(value, returned);

        Thread.sleep(2000);
        returned = cache.get();
        assertEquals(value, returned);

        Thread.sleep(2000);
        returned = cache.get();
        assertEquals(value, returned);

        verify(supplier, times(1)).get();
    }

    @Test
    @DisplayName("After cache limit supplier is called twice")
    void after_cache_test() throws InterruptedException {
        final Integer value= 1;
        when(supplier.get()).thenReturn(value);
        CachedSupplier<Integer> cache = new CachedSupplier<>(supplier, 6);

        Integer returned = cache.get();
        assertEquals(value, returned);

        Thread.sleep(3000);
        returned = cache.get();
        assertEquals(value, returned);

        Thread.sleep(5000);
        returned = cache.get();
        assertEquals(value, returned);

        verify(supplier, times(2)).get();
    }

}