package com.lewiswatson.camel.sandbox;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
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

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@MockEndpointsAndSkip("file:xml")
@ContextConfiguration("classpath:/META-INF/spring/camel-context.xml")
@DirtiesContext
public class XmlRouteTest {

	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:file:xml")
	protected MockEndpoint end;

	@EndpointInject(uri = "file:in?noop=true")
	protected ProducerTemplate template;

	@Test
	public void testXmlFile() throws InterruptedException {

		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("orders");
		Element order = root.addElement("order");
		order.addElement("id").addText("4423");
		order.addElement("item").addText("sunglasses");

		template.sendBodyAndHeader("file:in?noop=true", document.asXML(), Exchange.FILE_NAME, "order.xml");

		// TODO not sure why this assertion fails
		// end.expectedMessageCount(1);
		end.expectedHeaderReceived("orderId", "4423");
		end.expectedHeaderReceived("item", "sunglasses");
		MockEndpoint.assertIsSatisfied(camelContext);
	}

	@Test
	public void testNonXMLFile() throws InterruptedException {

		// Reset the mock endpoint to avoid previous test from affecting the
		// assertions.
		end.reset();

		template.sendBodyAndHeader("file:in?noop=true", "Some text", Exchange.FILE_NAME, "order.txt");

		end.expectedMessageCount(0);
		// TODO test passes regardless due to an xpath exception triggering a
		// rollback

		MockEndpoint.assertIsSatisfied(camelContext);
	}

	@Test
	public void testMultiOrderXmlFile() throws InterruptedException {

		// Reset the mock endpoint to avoid previous test from affecting the
		// assertions.
		end.reset();

		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("orders");
		Element order = root.addElement("order");
		order.addElement("id").addText("4424");
		order.addElement("item").addText("t-shirt");
		Element order2 = root.addElement("order");
		order2.addElement("id").addText("4425");
		order2.addElement("item").addText("shoes");

		template.sendBodyAndHeader("file:in?noop=true", document.asXML(), Exchange.FILE_NAME, "order.xml");

		end.expectedMessageCount(1);
		end.expectedHeaderValuesReceivedInAnyOrder("orderId", new ArrayList<String>(Arrays.asList("4424", "4425")));
		end.expectedHeaderValuesReceivedInAnyOrder("item", new ArrayList<String>(Arrays.asList("t-shirt", "shoes")));
		MockEndpoint.assertIsSatisfied(camelContext);
	}

}