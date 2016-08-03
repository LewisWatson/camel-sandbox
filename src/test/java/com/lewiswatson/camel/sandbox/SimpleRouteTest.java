package com.lewiswatson.camel.sandbox;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@MockEndpointsAndSkip("direct:end")
@ContextConfiguration("classpath:/META-INF/spring/camel-context.xml")
@DirtiesContext
public class SimpleRouteTest {

  @Autowired
  protected CamelContext camelContext;

  @EndpointInject(uri = "mock:direct:end")
  protected MockEndpoint end;

  @EndpointInject(uri = "direct:start")
  protected ProducerTemplate template;

  @Test
  public void test() throws InterruptedException {

    end.expectedMessageCount(1);

    template.sendBody("test");

    MockEndpoint.assertIsSatisfied(camelContext);
  }

}
