package org.clulab.linnaeus.model

trait Identifyable[IdentityType] {
  def getId: IdentityType
}
