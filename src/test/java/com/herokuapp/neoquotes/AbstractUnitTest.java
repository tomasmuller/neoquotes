package com.herokuapp.neoquotes;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration({"/neoquotes-test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractUnitTest {

	protected Logger logger = Logger.getLogger("neoquotes-tests");
	
}
