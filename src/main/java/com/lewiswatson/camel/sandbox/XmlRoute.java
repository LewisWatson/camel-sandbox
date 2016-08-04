package com.lewiswatson.camel.sandbox;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

/**
 * This route showcases various Camel features such as:
 * <ul>
 * <li>Enterprise Integration Patterns
 *  <ul>
 *   <li>Content Based Router (CBR)
 *   <li>Splitter
 *   <li>Recipient List
 *   <li>Aggregator
 *   <li>Filter
 *   <li>Wiretap
 *  </ul>
 * <li> Reading/Writing files
 * <li>Logging
 * </ul>
 * 
 * @author Lewis Watson
 *
 */
public class XmlRoute extends RouteBuilder {

	private static final String LOG_URI = "log:com.lewiswatson.camel.sandbox?showAll=true&multiline=true&showFiles=true";

	@Override
	public void configure() throws Exception {
		
		// Demonstrate Content Based Router (CBR) and wiretap EIP pattern
		from("file:in")
			.to(LOG_URI)
			.wireTap("file:wiretap")
			.choice()
				.when(header(Exchange.FILE_NAME_ONLY).endsWith(".xml"))
					.log("found xml file")
					.to("direct:xml")
				.otherwise()
					.log("found non xml file")
					.to("direct:non-xml")
			.endChoice();
		
		// Demonstrate the splitter pattern and multicast.
		from("direct:xml")
			.split(xpath("//order"))
			.log("order item")
			.process(new XmlProcessor())
			.to(LOG_URI)
			.multicast()
			.to("file:xml", "direct:aggregator", "direct:onlyBelts");
		
		from("direct:non-xml").to("file:nonxml");
		
		// Demonstrate the aggregator pattern
		from("direct:aggregator")
			.aggregate(header("item"), new OrderAggregationStrategy()).completionTimeout(3000)
			.log("aggregated message")
			.setHeader(Exchange.OVERRULE_FILE_NAME, header("item"))
			.to(LOG_URI)
			.to("file:aggregatedXML");
		
		// Demonstrate the filter pattern
		from("direct:onlyBelts").filter(xpath("//order/item[belt]")).to("file:belts");
	}

}
