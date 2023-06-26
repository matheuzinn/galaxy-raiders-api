package galaxyraiders.core.score

import galaxyraiders.Config
import java.util.UUID
import java.time.LocalDateTime
import com.fasterxml.jackson.module.kotlin.*
import java.io.File

class Run {
  var startTime: String = LocalDateTime.now().toString()
  var asteroidsCount: Int = 0
  var score: Double = 0.0

  fun update(points: Double) {
    asteroidsCount++
    score += points
  }
}

class ScoreSaver() {
  private val reader = jacksonObjectMapper()
  private val scoreboardFilename = "./src/main/kotlin/galaxyraiders/core/score/Scoreboard.json"
  private val leaderboardFilename = "./src/main/kotlin/galaxyraiders/core/score/Leaderboard.json"
  private val pastRuns: List<Run> = getPastRuns()
  private val currentRun: Run = Run()

  fun update(points: Double) {
    currentRun.update(points)
    save()
  }

  private fun getPastRuns(): List<Run> {
    val file = File(scoreboardFilename)
    if (!file.exists()){
      return emptyList()
    }
    var lastRunsList: List<Run> = this.reader.readValue(file)
    return ArrayList(lastRunsList)
  }

  private fun save() {
    val runs = pastRuns + currentRun
    this.reader.writerWithDefaultPrettyPrinter().writeValue(
      File(scoreboardFilename),
      ArrayList(runs)
    )
    val sortedRuns = runs.sortedByDescending { it.score }
    val bestRuns = sortedRuns.take(3)
    this.reader.writerWithDefaultPrettyPrinter().writeValue(
      File(leaderboardFilename),
      ArrayList(bestRuns)
    )
  }
}