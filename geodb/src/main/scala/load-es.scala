package com.welocally.geodb.services.utils
import scala.io.Source
import scala.util.parsing.json.JSON._

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.json.JSONObject
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.common.settings.ImmutableSettings
import com.welocally.geodb.services.util.WelocallyJSONUtils

object ESLoader {
  @Component
  class Wiring
  {
    @Autowired var welocallyJSONUtils : WelocallyJSONUtils = null
  }
  var wiredBean:Wiring = _

  def readFile(filename :String) = {
    println("Reading file " + filename)
    Source.fromFile(filename).getLines
  }

  def parseFile(filename :String) = 
    for(l <- readFile(filename)) yield new JSONObject(l)

  def makePlaceIndex(place :JSONObject) = {
    wiredBean.welocallyJSONUtils.updatePlaceToWelocally(place)
    wiredBean.welocallyJSONUtils.makeIndexablePlace(place)
  }

  def main(args: Array[String])  {
    val ctx = new ClassPathXmlApplicationContext("geodb-applicationContext.xml", "signpost4j-context.xml");
    wiredBean = ctx.getBean(classOf[Wiring]);
    println("welocallyJSONUtils = " + wiredBean.welocallyJSONUtils)
    val settings = ImmutableSettings.settingsBuilder()
             .put("cluster.name", "es-welocally").build();
    val client = new TransportClient(settings)
        .addTransportAddress(new InetSocketTransportAddress("ec2-107-22-80-168.compute-1.amazonaws.com", 9300))
        .addTransportAddress(new InetSocketTransportAddress("ec2-184-72-135-198.compute-1.amazonaws.com", 9300))
    val bulkRequest = client.prepareBulk();
    val parsedFile = parseFile(args(0))
    for(i <- (0 to 10000)) {
      val placeIndex = makePlaceIndex(parsedFile.next)
      
      val id:String = placeIndex.get("_id").asInstanceOf[String]
      bulkRequest.add(client.prepareIndex("geodb","place",id)
         .setSource(XContentFactory.jsonBuilder()
		.startObject()
		.field("search", placeIndex.get("search"))
		.startArray("location")
		.value(placeIndex.get("location_1_coordinate").asInstanceOf[Double])
		.value(placeIndex.get("location_0_coordinate").asInstanceOf[Double])
		.endArray()
		.endObject()))
//      println(i + ":" + placeIndex.get("_id") )
    }
    val bulkResponse = bulkRequest.execute().actionGet();
    println("bulkResponse = " + bulkResponse)
    if(bulkResponse.hasFailures()) {
        println(bulkResponse.buildFailureMessage())
    }


    client.close()
    System.exit(0)
  }
}