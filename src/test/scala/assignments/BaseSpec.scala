package assignments

import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{WordSpec, BeforeAndAfter, Matchers, FlatSpec}

trait BaseSpec extends FlatSpec with BeforeAndAfter with Matchers with GeneratorDrivenPropertyChecks {
}

trait BaseWordSpec extends WordSpec with BeforeAndAfter with Matchers with GeneratorDrivenPropertyChecks {
}