package com.lewiswatson.camel.sandbox;

import org.apache.camel.builder.RouteBuilder;

public class SimpleRoute extends RouteBuilder {

  @Override
  public void configure() throws Exception {
    from("direct:start").to("direct:end");
  }

}
