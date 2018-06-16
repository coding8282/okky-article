package org.okky.article.domain.repository;

import org.junit.runner.RunWith;
import org.okky.article.TestMother;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
//@MybatisTest
public abstract class MapperTestMother extends TestMother {
}