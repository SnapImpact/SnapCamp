package org.snapimpact.model

import org.scala_libs.jpa.LocalEMF
import net.liftweb.jpa.RequestVarEM

object Model extends LocalEMF("pgunit") with RequestVarEM