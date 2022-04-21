package com.knoldus
import zio.{ExitCode, Fiber, IO, Task, UIO, URIO, ZIO}

import scala.io.{BufferedSource, Source}

object ZioWordCounter extends zio.App {

  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    (for {
      value <- bracketed
      _ <- zio.console.putStrLn(value.toString)
    } yield ()).exitCode
  }

  val fileReader: Task[BufferedSource] = IO(Source.fromFile("/home/knoldus/IdeaProjects/Zio-Learning/File1"))

  val fileCloser: Source => UIO[Unit] = (source: Source) => URIO(source.close())

  val bracketed: ZIO[Any, Throwable, Int] = fileReader.bracket(fileCloser) { file: BufferedSource =>
    IO(wordCounter(file))
  }

  def wordCounter(file: BufferedSource): Int = {
    file.getLines().flatMap(_.split(" ")).length
  }
}
