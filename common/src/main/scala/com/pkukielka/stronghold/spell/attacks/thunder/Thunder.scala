package com.pkukielka.stronghold.spell.attacks.thunder

import com.pkukielka.stronghold.enemy.Enemy
import com.badlogic.gdx.math.Vector2
import com.pkukielka.stronghold.spell.{ShootAndForget, Attack}
import com.pkukielka.stronghold.enemy.features.flammable.Flammable

object Thunder {
  val range = 1f
  val damageToTriggerFire = 60f
  val fireDamage = 10f
  val fireLength = 3f
}

class Thunder extends ThunderBase with ShootAndForget

trait ThunderBase extends Attack {
  protected val position: Vector2 = new Vector2()
  override protected val lifeTime: Float = 1f

  import Thunder._

  protected def damage = 20f + (60f * scala.math.random.toFloat)

  override def init(xStart: Float, yStart: Float, xEnd: Float, yEnd: Float, heightsDifference: Float) {
    activate()
    position.set(xStart, yStart)
  }

  override def update(deltaTime: Float, enemies: Array[Enemy]): Unit = {
    for (enemy <- enemies if !enemy.isDead && enemy.position.dst(position) < range) {
      val currentDamage = damage
      enemy.hit(currentDamage)
      if (currentDamage > damageToTriggerFire) {
        enemy match {
          case e: Flammable => e.setOnFire(fireLength, fireDamage)
          case _ =>
        }
      }
    }
  }
}
