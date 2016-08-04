package com.lewiswatson.camel.sandbox;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class XmlRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("file:in?noop=true")
			.to("log:com.lewiswatson.camel.sandbox?showAll=true&multiline=true&showFiles=true")
			.choice()
				.when(header(Exchange.FILE_NAME_ONLY).endsWith(".xml"))
					.log("found xml file")
					.to("direct:xml")
				.otherwise()
					.log("found non xml file")
					.to("direct:non-xml")
			.endChoice();
		
		from("direct:xml")
			.split(xpath("//order"))
			.log("order item")
			.process(new XmlProcessor())
			.to("log:com.lewiswatson.camel.sandbox?showAll=true&multiline=true&showFiles=true")
			.to("file:xml");
		
		from("direct:non-xml")
			.to("file:nonxml");
	}

}
