package com.pkukielka.stronghold.enemy

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import scala.collection.mutable.ArrayBuffer
import com.badlogic.gdx.maps.tiled.TiledMap
import scala.Array
import com.pkukielka.stronghold.enemy.units._
import com.pkukielka.stronghold.{InfluencesManager, MapBuilder, IsometricMapUtils}

object AnimationManager {

  object lifeBar {
    val width = 0.4f
    val height = 0.06f
    val margin = 0.02f
    val distance = 0.1f
  }

}

class AnimationManager(camera: OrthographicCamera, map: TiledMap, mapName: String) {
  private val utils = new IsometricMapUtils(camera)
  private val attack = Gdx.audio.newSound(Gdx.files.internal("data/sound/powers/shoot.ogg"))
  private val shapeRenderer = new ShapeRenderer()

  private val mapBuilder = new MapBuilder(map)
  private implicit val influencesManager = new InfluencesManager(mapBuilder.width, mapBuilder.height)
  private implicit val pathFinder = new PathFinder(mapBuilder, influencesManager)

  def updatePath() = pathFinder.findShortestPathsTo(pathFinder.getNode(7, 29))

  updatePath()

  private val enemies = ArrayBuffer.fill(10)(Array(
    new Skeleton, new SkeletonArcher, new SkeletonMage, new SkeletonWeak,
    new Goblin, new EliteGoblin, new EarthAnt, new FireAnt, new IceAnt,
    new Minotaur, new Stealth, new Zombie,
    new WyvernWater, new WyvernFire, new WyvernAir, new WyvernEarth
  )).flatten

  def hit(x: Float, y: Float) {
    attack.play()
    enemies.foreach {
      enemy =>
        if (!enemy.isDead && enemy.model.isHit(x, y, utils)) {
          enemy.hit((30 + Math.random() * 50).toInt)

          if (enemy.isDead) {
            influencesManager.add(10000f, enemy.position, 1000f, 200, 3f)
          }
        }
    }
    updatePath()
  }

  def update(batch: SpriteBatch, deltaTime: Float) {
    val delta = deltaTime

    influencesManager.update(delta)

    enemies.foreach(_.update(delta))

    batch.begin()
    enemies.filter(_.isDead).foreach(_.model.drawModel(batch, utils))
    batch.end()

    batch.begin()
    enemies.filter(!_.isDead).foreach(_.model.drawModel(batch, utils))
    batch.end()

    shapeRenderer.setProjectionMatrix(camera.combined)
    shapeRenderer.begin(ShapeType.Filled)
    enemies.foreach(_.model.drawLifeBar(shapeRenderer, utils))
    shapeRenderer.end()
  }
}
