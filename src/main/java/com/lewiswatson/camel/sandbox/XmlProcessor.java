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

		String orderId = xpath("//order/id").evaluate(context, inBody);
		String item = xpath("//order/item").evaluate(context, inBody);
		
		inMessage.setHeader("orderId", orderId);
		inMessage.setHeader("item", item);
		inMessage.setHeader(Exchange.OVERRULE_FILE_NAME, "order-"+orderId+"-"+item+".xml");
	}

}
