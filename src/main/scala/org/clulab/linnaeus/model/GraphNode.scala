package org.clulab.linnaeus.model

//class GraphNode[IdentityType, ValueType <: Valuable[_]](val id: IdentityType, val value: ValueType) extends Identifyable[IdentityType] {
class GraphNode[IdentityType, DataType](val id: IdentityType, val data: DataType) extends Identifyable[IdentityType] {
  def getId: IdentityType = id
//  def getValue: _ = value.getValue
}

class StringValue(val value: String) extends Valuable[String] {
  def getValue: String = value
}

class IntGraphNode(id: Int, value: StringValue) extends GraphNode[Int, StringValue](id, value)
