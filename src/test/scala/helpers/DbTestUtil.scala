package helpers

import javax.persistence.EntityManager
import org.snapimpact.model.Model

object DbTestUtil {

    implicit def symbol2String(s: Symbol) = s.name

    def withTx(f: EntityManager => Any) = {
        val em = Model.openEM
        val tx = em.getTransaction
        try {
            f(em)
            tx.commit
        } catch {
            case e => {
                tx.rollback
                throw e
            }
        }
    }

    //Native to postgres
    def clearDatabase() = {

        def buildTruncateStatement(ts: String*) = ts.map("TRUNCATE " + _ + " CASCADE;").reduceLeft(_+_)

        val query = buildTruncateStatement(
            'api,
            'distance,
            'event,
            'event_interest_area,
            'event_location,
            'event_organization,
            'event_timestamp,
            'filter,
            'filter_interest_area,
            'filter_organization_type,
            'integration,
            'interest_area,
            'iv_user,
            'location,
            'network,
            'organization,
            'organization_interest_area,
            'organization_location,
            'organization_type,
            'source,
            'source_interest_map,
            'source_org_type_map,
            'timeframe,
            'timestamp)

        
        withTx {
            em =>
                em.createNativeQuery(query).executeUpdate
                em.flush
        }
    }


}