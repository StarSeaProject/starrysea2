package top.starrysea.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.starrysea.StarryseaApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarryseaApplication.class)
public class StarryseaApplicationTest {
	@Test
	public void test() {
		System.out.println("hello");
	}
}
