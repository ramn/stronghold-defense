package com.pkukielka.stronghold.enemy

import com.pkukielka.stronghold.assets.EnemyAssets
import com.pkukielka.stronghold.Lifecycle
import com.badlogic.gdx.math.Vector2

trait Enemy extends Lifecycle {
  def position: Vector2

  var isHold: Boolean

  def isDead: Boolean

  def assets: EnemyAssets

  def directionVector: Vector2

  def width: Float

  def height: Float

  def velocity: Float

  def die(): Unit

  def isHit(segmentStart: Vector2, segmentEnd: Vector2): Boolean

  def hit(damage: Float): Unit

  def harm(damage: Float): Unit

  def turn(): Unit

  def update(deltaTime: Float)
}
