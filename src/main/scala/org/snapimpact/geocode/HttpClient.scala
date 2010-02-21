package org.snapimpact.geocode

import org.apache.commons.httpclient.methods.GetMethod

trait HttpClient {
    val hc = new org.apache.commons.httpclient.HttpClient

    private def getNodeSeqFromUrl(url: String) {
        val gm = new GetMethod(url)
        hc.executeMethod(gm)

        //Use constructing parser -- no network hit
        
    }
}