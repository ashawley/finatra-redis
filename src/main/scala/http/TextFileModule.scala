/**
 * TextFileModule --- Inject TextFile in to Twitter server
 */

package com.github.ashawley
package http

import com.twitter.inject.TwitterModule
import com.google.inject.Inject
import com.google.inject.Provides
import com.google.inject.Singleton

object TextFileModule extends TwitterModule {
  val fileFlag = flag("file", new java.io.File("NONE"), "Input file")

  @Singleton
  @Provides
  def provideFile: TextFileLike = {
    val file = fileFlag()
    print(s"Loading ${file}...")
    val textFile = new spark.TextFile(file)
    println("done")
    textFile
  }
}
