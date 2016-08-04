package com.lewiswatson.camel.sandbox;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import static org.apache.camel.builder.xml.XPathBuilder.xpath;

public class XmlProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Message inMessage = exchange.getIn();
		Object inBody = inMessage.getBody();
		CamelContext context = exchange.getContext();

		inMessage.setHeader("orderId", xpath("//order/id").evaluate(context, inBody));
		inMessage.setHeader("item", xpath("//order/item").evaluate(context, inBody));
	}

}
