package cn.richinfo.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.richinfo.dao.SubDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath*:config/spring/application-*.xml"
})
public class Test {
	@Autowired
	private SubDao dao;

	@org.junit.Test
	public void test(){
		System.out.println(dao.querySubcribedIds("15813343770"));
	}
}
