package com.knoldus
import com.knoldus.ZioWordCounter.wordCounter
import zio.{ExitCode, Fiber, IO, Task, UIO, URIO, ZIO}

import scala.io.{BufferedSource, Source}

object ZioWordCounterMultiple extends zio.App {

  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    (for {
      value <- fileInstance
      _ <- zio.console.putStrLn(value.toString)
    } yield ()).exitCode
  }

  def wordCounter(file: BufferedSource): Int = {
    file.getLines().flatMap(_.split(" ")).length
  }

  val list: List[String] = List("/home/knoldus/IdeaProjects/Zio-Learning/File1","/home/knoldus/IdeaProjects/Zio-Learning/File2")

  val fileCloser: Source => UIO[Unit] = (source: Source) => URIO(source.close())

  val fileInstance: ZIO[Any, Nothing, List[Fiber.Runtime[Throwable, Int]]] = ZIO.foreachPar(list) {
    filePath => IO(Source.fromFile(filePath)).bracket(fileCloser) { file: BufferedSource =>
      IO(wordCounter(file))
    }.fork
  }

  //def printThread = s"[${Thread.currentThread().getName}]"



















//  val bracketed: List[ZIO[Any, Throwable, Fiber.Runtime[Throwable, Int]]] = ins.map(_.bracket(fileCloser) { file: BufferedSource =>
//    IO(wordCounter(file)).fork}
//  )
}
