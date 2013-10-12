package com.pkukielka.stronghold.enemy

import com.badlogic.gdx.graphics.g2d.{ParticleEffectPool, SpriteBatch, TextureRegion}
import com.pkukielka.stronghold.IsometricMapUtils
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.Color
import com.pkukielka.stronghold.effect.FireEffect
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion

object EnemyRenderer {

  object lifeBar {
    val width = 0.4f
    val height = 0.06f
    val margin = 0.02f
    val distance = 0.1f
  }

}

trait EnemyRenderer {
  self: Enemy =>

  val unitScale = 1 / 64f
  var fireEffect: ParticleEffectPool#PooledEffect = null

  def currentFrame: TextureRegion = {
    if (isDead) {
      assets.gfx.dieAnimations(angle).getKeyFrame(animationTime, false)
    }
    else {
      assets.gfx.moveAnimations(angle).getKeyFrame(animationTime, true)
    }
  }

  def width: Float = currentFrame.getRegionWidth * unitScale

  def height: Float = currentFrame.getRegionHeight * unitScale

  def setOnFire(time: Float) {
    if (fireEffect == null) {
      fireEffect = FireEffect.obtain
    }

    fireEffect.setDuration((time * 1000).toInt)
    fireEffect.reset()
  }

  def drawLifeBar(shapeRenderer: ShapeRenderer) {
    import EnemyRenderer.lifeBar

    if (!isDead && life != maxLife) {
      val pos = IsometricMapUtils.mapToCameraCoordinates(position.x, position.y)
      val x = pos.x + (width - 0.5f) / 2
      val y = pos.y + height + lifeBar.distance
      val lifeRatio = life / maxLife

      shapeRenderer.setColor(Color.BLACK)
      shapeRenderer.rect(x, y, lifeBar.width, lifeBar.height)
      shapeRenderer.setColor(1.2f - Math.pow(lifeRatio, 2).toFloat, 1f - Math.pow(1f - lifeRatio, 2).toFloat - 0.2f, 0f, 1f)
      shapeRenderer.rect(x + lifeBar.margin, y + lifeBar.margin, lifeRatio * (lifeBar.width - 2 * lifeBar.margin), lifeBar.height - 2 * lifeBar.margin)
    }
  }

  def drawModel(batch: SpriteBatch, deltaTime: Float) {
    val pos = IsometricMapUtils.mapToCameraCoordinates(position.x, position.y)
    batch.draw(
      currentFrame,
      pos.x - 0.5f + currentFrame.asInstanceOf[AtlasRegion].offsetX * unitScale,
      pos.y - 0.5f + currentFrame.asInstanceOf[AtlasRegion].offsetY * unitScale,
      width,
      height
    )

    if (fireEffect != null) {
      if (fireEffect.isComplete) {
        fireEffect.free()
        fireEffect = null
      }
      else {
        fireEffect.setPosition(pos.x + width * 0.5f, pos.y + height * 0.5f)
        fireEffect.draw(batch, deltaTime)
      }
    }
  }

}
