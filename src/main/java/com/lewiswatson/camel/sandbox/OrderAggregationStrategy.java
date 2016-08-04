package com.lewiswatson.camel.sandbox;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Simple aggregator strategy for orders that simply concats
 * the bodies
 * 
 * @author lewis watson
 *
 */
public class OrderAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if(oldExchange == null) {
			return newExchange;
		}
		
		String newBody = newExchange.getIn().getBody(String.class);
		String oldBody = oldExchange.getIn().getBody(String.class);
		
		newExchange.getIn().setBody(oldBody.concat(newBody));
		
		return newExchange;
		
	}

}
