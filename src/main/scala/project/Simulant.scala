package project

trait simulant:

  var position = ???

  var target = ???

  def mass = ???

  def heading = ???

  def update() = ???


object leader extends simulant:

  def realtarget = ???


class follower extends simulant:
  override def mass: Nothing = super.mass