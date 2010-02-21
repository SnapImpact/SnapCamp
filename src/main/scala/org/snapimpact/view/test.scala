package org.snapimpact.view
//
import scala.xml.NodeSeq
import net.liftweb.http.LiftView
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonDSL._


class Test extends LiftView {

  override def dispatch = {
    case "index" => index _
    case "hello" => hello _
  }

  case class Winner(id: Long, numbers: List[Int])
  case class Lotto(id: Long, winningNumbers: List[Int], winners: List[Winner], drawDate: Option[java.util.Date])

  val winners = List(Winner(23, List(2, 45, 34, 23, 3, 5)), Winner(54, List(52, 3, 12, 11, 18, 22)))
  val lotto = Lotto(5, List(2, 45, 34, 23, 7, 5, 3), winners, None)

  val json =
    ("lotto" ->
      ("lotto-id" -> lotto.id) ~
      ("winning-numbers" -> lotto.winningNumbers) ~
      ("draw-date" -> lotto.drawDate.map(_.toString)) ~
      ("winners" ->
        lotto.winners.map { w =>
          (("winner-id" -> w.id) ~
           ("numbers" -> w.numbers))}))

  // println(compact(JsonAST.render(json)))

  def index(): NodeSeq = {
    <p>here's some info....</p>
  }
  def hello(): NodeSeq = {
   <p>{compact(JsonAST.render(json))}</p>
  }


}
