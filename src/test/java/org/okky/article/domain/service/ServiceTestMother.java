package org.okky.article.domain.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.okky.article.TestMother;
import org.okky.article.domain.event.DomainEventPublisher;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DomainEventPublisher.class)
public abstract class ServiceTestMother extends TestMother {
    @Before
    public void setUp() {
        PowerMockito.mockStatic(DomainEventPublisher.class);
    }
}
