package com.pkukielka.stronghold.enemy.units

import com.pkukielka.stronghold.enemy.{PathFinder, EnemyCore}
import com.pkukielka.stronghold.assets.Assets

class WyvernAir(implicit pathFinder: PathFinder) extends EnemyCore(pathFinder) {
  override def assets = Assets.wyvernAir

  override def velocity: Float = 5.0f
}