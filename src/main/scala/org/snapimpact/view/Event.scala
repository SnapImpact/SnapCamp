package org.snapimpact.view
{
//import org.scala_tools.time.Imports._

case class Interest(uri: String)
case class Location(uri: String)
case class Organization(uri: String, description: String, id: String, name: String, organizationTypeId: String)
case class Source(etlClass: String, id: String, name: String, url: String)
case class TimeStamp(uri: String, id: String, timestamp: String )

case class Events(uri: String, sourceName: String, description: String, duration: String, email: String,
        id: String, interestAreaCollection: List[Interest], LocationCollection: List[Location],
        organizationCollection: List[Organization], phone: String, sourceId: String,
        sourceUrl: String, timestampCollection: List[TimeStamp], title: String,
        url: String)

}