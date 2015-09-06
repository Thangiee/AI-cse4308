package assignments

import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{BeforeAndAfter, Matchers, FlatSpec}

trait BaseSpec extends FlatSpec with BeforeAndAfter with Matchers with GeneratorDrivenPropertyChecks {
}
