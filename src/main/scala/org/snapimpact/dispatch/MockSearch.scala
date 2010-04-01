package org.snapimpact.dispatch

import org.snapimpact.model._
import java.math.BigInteger
import org.snapimpact.view.TimeStamp
import java.util.{Date, ArrayList, UUID}

/**
 * Created by IntelliJ IDEA.
 * User: okristjansson
 * Date: Feb 23, 2010
 * Time: 9:13:25 PM
 * To change this template use File | Settings | File Templates.
 */
object MockSearch
{

  def getEvents() =
  {
    var lEvents = genEvent :: Nil;
    //
    lEvents = genEvent :: lEvents
    lEvents = genEvent :: lEvents
    lEvents = genEvent :: lEvents
    lEvents = genEvent :: lEvents

    // returns the list
    lEvents
  }

   def genEvent =
   {
      val e = new Event
      e.setId("MockId")
      e.setTitle("MockTitle")
      e.setDescription("MockDescr")
      e.setDuration( "MockDuration")
      e.setContact("MockContct")
      e.setUrl("MockUrl" )
      e.setPhone("MockPhne")
      e.setEmail("MockeMail")
      e.setSourceKey("MockSrcKey" )
      e.setSourceUrl("MockSrcUrl" )
      val mySource = genSource( e )
      e.setSourceId( mySource )
      // Loc and Org collection as well
      val myOrg = genOrganization( mySource, e )
      e.setLocationCollection(new ArrayList[Location]())
      e.getLocationCollection.add( genLocation( e, myOrg ) )
      //
      e.setOrganizationCollection( new ArrayList[Organization]())
      e.getOrganizationCollection.add( myOrg )
      // Set the interests
      e.setInterestAreaCollection(new ArrayList[InterestArea]())
      e.getInterestAreaCollection.add(genInterestArea(mySource, e, myOrg ) )
      e.getInterestAreaCollection.add(genInterestArea(mySource, e, myOrg ) )
      //
      e.setTimestampCollection( new ArrayList[Timestamp])
      e.getTimestampCollection.add( genTimestamp( e ) )
      //
      e
    }


    def genInterestArea( pxSource :Source, pxEvent :Event, pxOrganization :Organization ) =
    {
        val i = new InterestArea
        //
        i.setId("Id," + rndStr)
        i.setName("Nmn," + rndStr)

        // Than the collections
        i.setOrganizationCollection( new ArrayList[Organization]() )
        i.getOrganizationCollection.add( pxOrganization )
        //
        i.setEventCollection( new ArrayList[Event]() )
        i.getEventCollection.add( pxEvent )
        //
        i.setFilterCollection( new ArrayList[Filter]() )
        i.getFilterCollection.add( genFilter( genTimeFrame ) )
        //
        i.setSourceInterestMapCollection( new ArrayList[SourceInterestMap]() )
        i.getSourceInterestMapCollection.add( genSourceInterestMap( pxSource, pxEvent, i  )  )
        //
        i
    }

    def genOrganization( pxSource :Source, pxEvent :Event ) =
    {
      val org = new Organization( "MockOrgId")
      org.setDescription( "MockDescript");
      org.setEmail( "MockEmail")
      org.setName( "MockName")
      org.setPhone( "MockPhone")
      org.setSourceKey( "MockSrcKey")
      org.setSourceUrl( "http://SourceMockUrl.com")
      org.setUrl( "http://MockUrl.com" )
      org.setOrganizationTypeId( genOrginizationType( genSourceOrgTypeMap( pxSource )) )
      org.setSourceId( pxSource )

      // Lists
      org.setEventCollection( new ArrayList[Event]() )
      org.getEventCollection.add( pxEvent )
      //
      // Loc and Org collection as well
      org.setLocationCollection(new ArrayList[Location]())
      org.getLocationCollection.add( genLocation( pxEvent, org ) )
      //
      org.setInterestAreaCollection( new ArrayList[InterestArea]() )
      org.getInterestAreaCollection.add( genInterestArea( pxSource, pxEvent, org ))

      //
      org
    }

    def genSource( pxEvent: Event ) =
    {
      val src = new Source( "MockSrcId")
      src.setName( "MockSrcName")
      src.setApiUrl( "MockUrl" )
      src.setEtlClass( "MockEtlClass")
      src.setUrl( "MockUrl")
      // Lists
      src.setEventCollection( new ArrayList[Event]() )
      src.getEventCollection.add( pxEvent )

      // Loc and Org collection as well
      src.setApiCollection( new ArrayList[Api]() )
      src.getApiCollection.add( genApi( src ) )
      //
      src.setSourceOrgTypeMapCollection(new ArrayList[SourceOrgTypeMap]())
      src.getSourceOrgTypeMapCollection.add( genSourceOrgTypeMap(src) )
      //
      val myOrg = genOrganization( src, pxEvent );
      src.setOrganizationCollection( new ArrayList[Organization]())
      src.getOrganizationCollection.add( myOrg )
      //
      src.setSourceInterestMapCollection(new ArrayList[SourceInterestMap]())
      src.getSourceInterestMapCollection.add( genSourceInterestMap(src, pxEvent, genInterestArea( src, pxEvent, myOrg ) ) )

      //
      src
    }

    def genFilter( pxTimeFrame :Timeframe) =
    {
      val filt = new Filter( "MockFiltId")
      filt.setDistanceId( genDistance( filt ) )
      filt.setLatitude( "0.0330")
      filt.setLongitude( "0.0330" )
      filt.setTimeframeId( pxTimeFrame )
      //
      filt
    }

  def genDistance( pxFilter :Filter ) =
  {
    val dist = new Distance( "MockDistance")
    dist.setBucket( 5 )
    //
    dist.setFilterCollection( new ArrayList[Filter]() )
    dist.getFilterCollection.add( pxFilter )

    //
    dist
  }

  def genTimeFrame =
  {
    val timeF = new Timeframe()
    timeF.setBucket( new BigInteger("26525285981219105863630848482795") )
    timeF.setName( "MockTimeFrame")
    //
    timeF.setFilterCollection( new ArrayList[Filter]() )
    timeF.getFilterCollection.add( genFilter( timeF ) )

    //
    timeF
  }


    def genLocation ( pxEvent :Event, pxOrg :Organization )=
    {
      val loc = new Location( "MockLocId")
      loc.setLatitude( "0.0003000" )
      loc.setLongitude( "0.0003000" )
      loc.setLocation( "MockBoulder")
      loc.setCity( "MockBoulderCity")
      loc.setState( "MockColoradoBeerState")
      loc.setStreet( "MockStreet")
      loc.setZip( "07007" )
      loc.setCountry( "MockUSA" )

      // Lists
      loc.setEventCollection( new ArrayList[Event]() )
      loc.getEventCollection.add( pxEvent )
      //
      loc.setOrganizationCollection( new ArrayList[Organization]())
      loc.getOrganizationCollection.add( pxOrg )

      //
      loc
    }


    def genSourceInterestMap( pxSource :Source, pxEvent :Event, pxInterestArea :InterestArea ) =
    {
      val im = new SourceInterestMap( "MockIntrMapId")
      im.setSourceKey( "MockeSrcIntrKey")
      im.setInterestAreaId( pxInterestArea )
      im.setSourceId( pxSource )
      //
      im
    }

  def genApi( pxSource : Source ) =
  {
    val api = new Api()
    api.setId( "MockId" )
    api.setLastKey( "MockLastKey")
    api.setSourceId( pxSource )
    api.setTempFileBase( "MockTmpFileBase")
    api.setUrlPath( "MockUrlPath" )
    //
    api
  }

  def genSourceOrgTypeMap( pxSource :Source ) =
  {
    val srcotm = new SourceOrgTypeMap( "MockKey")
    srcotm.setSourceId( pxSource )
    srcotm.setSourceKey( "MockKey" )
    srcotm.setOrganizationTypeId( genOrginizationType( srcotm ) )
    srcotm.setId( "MockId")
    //
    srcotm
  }


  def genOrginizationType( pxSourceOrgTypeMap :SourceOrgTypeMap ) =
  {
    val orgtype = new OrganizationType()
    orgtype.setName("MockName")
    orgtype.setId( "MockId")
    // Lists
    orgtype.setFilterCollection( new ArrayList[Filter] )
    orgtype.getFilterCollection.add( genFilter( genTimeFrame ) )
    //
    orgtype.setSourceOrgTypeMapCollection( new ArrayList[SourceOrgTypeMap]() )
    orgtype.getSourceOrgTypeMapCollection.add( pxSourceOrgTypeMap )

    //
    orgtype
  }

  def genTimestamp( pxEvent :Event ) =
  {
    val stamp = new Timestamp()
    stamp.setId("MockstampId")
    stamp.setTimestamp( new Date() )
    //
    stamp.setEventCollection( new ArrayList[Event]() )
    stamp.getEventCollection.add( pxEvent )
    //
    stamp
  }


    // The randoms
    def rndStr = UUID.randomUUID.toString
    def rndLong = (Math.random * 1000).asInstanceOf[Long]

}
