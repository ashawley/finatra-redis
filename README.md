## Line Server

Serve individual lines of an immutable text file.

- `GET /lines/<line number>`
 - HTTP 200 and the text of the line
 - HTTP 404 if the line is beyond the end of the file

Requires [SBT](http://www.scala-sbt.org/download.html) to compile and run the system.

A text file is loaded line-by-line in to Redis, an in-memory key store server, using zero-indexed keys.  Redis uses a fast hash indexing function for retrieval.

The Redis client library available from Twitter Finagle is used.  It can launch a Redis server/cluster within the JVM.  This is done in the code by default.  However, it is only for testing, and it is also not performant.

An SBT task will create a large file of random ASCII characters.  It is written using Scala (and Java libraries) and uses some of SBT's handy API.

### Large files

Retrieval from Redis uses its hash table implementation.  In the worst case a lookup is O(_n_) for n records.  It is O(_1_), or constant, for average lookups -- assuming Redis is behaving.   Redis will misbehave with large data, but not before running out of memory.  Redis offers partitioning to distribute data across a cluster.  There is no evidence that Redis uses a binary search tree, which would give retrievals O(_log n_) in the worse case.

The space complexity for hash tables is O(_n_) as well.  Redis offers distributed clustering to alleviate memory consumption.  With larger data sets, using a file-based rather than in-memory database should be used instead.  Redis used to offer virtual memory to disk, but that is no longer available.

Creating a large file of random characters is a task in SBT.  It will run in O(_n_) time, but only constants memory since it is built using the stream implementation from Scala's collections library.  I verified that writing a file works even when memory was constrained.  When running the task in a JVM that is constrained to a ~88MB memory footprint, it takes about 40 minutes to create a 1GB file -- even with an SSD drive.  When constrained to the default JVM setting of 2GB, it takes about 4 minutes.  Presumably, the JVM's garbage collection is at fault for the 10x increase.

### Performance

On my local machine, testing it with ApacheBench with 2000 requests at a time with concurrent "users" seemed to only show that I couldn't find the upper bound of users before overloading the system socket limits:

```
1 user, 1.657 seconds, 1206 requests/s, 0.9 ms/request
10 users, 0.707 seconds, 2827 requests/s, 3.5 ms/request
50 users, 0.756 seconds, 2653 requests/s, 19 ms/request
100 users, 0.822 seconds, 2432 requests/s, 41 ms/request
150 users, 0.927 seconds, 2158 requests/s, 69 ms/request
175 users, 0.756 seconds, 2614 requests/s, 66 ms/request
200 users, 0.713 seconds, 2806 requests/s, 71 ms/request
225 users, 0.661 seconds, 3024 requests/s, 74 ms/request
250 users, 0.962 seconds, 2080 requests/s, 120 ms/request
```

A shell command for running ApacheBench

```
$ for n in 1 10 50 100 150 175 200 250 300;
  do ab -n 2000 -c $n http://localhost:8888/line/$(($RANDOM % 150 + 1));
done
```

According to ApacheBench, the waiting time to connect did increase by 100% from 225 to 250 concurrent connections.  The Finagle/Twitter/Netty server runs about 10 threads by default.  This produces a JVM that requires less than a gigabyte of memory.  Scaling to 10,00 users will require tuning memory consumption on the Web server and the JVM within the limits of the machine to the maximum servable connections.

### Notes

[Blog post on generating random strings in Scala](http://alvinalexander.com/scala/creating-random-strings-in-scala)

[Javadocs for java.io.File](http://docs.oracle.com/javase/6/docs/api/java/io/File.html)

Scala source code documentation

- http://github.com/scala/scala
- `src/library/scala/util/Random.scala`
- `src/library/scala/collection/immutable/Stream.scala`
- `src/library/scala/collection/Iterator.scala`

[SBT Scaladoc API](http://www.scala-sbt.org/0.13/api/)

[SBT Reference manual](http://www.scala-sbt.org/0.13/docs/), including
"Tasks", "Input tasks", "Paths", "Generating files".

Stackoverflow posts related to SBT tasks.

[Finatra User Guide](http://twitter.github.io/finatra/user-guide/)

[Finatra #148: Processing flags parsed after server starts](https://github.com/twitter/finatra/issues/148)

[Finagle API docs](https://twitter.github.io/finagle/docs/)

[Finagle: Concurrent programming with Futures](https://twitter.github.io/finagle/guide/Futures.html)

[Effective Scala](http://twitter.github.io/effectivescala/), section on "Futures".

[Finagle Redis example](https://github.com/listatree/finagle-redis-sample) by Carlos Saltos

[Redis internals](http://redis.io/topics/internals)

[Redis core implementation](http://key-value-stories.blogspot.com/2015/01/redis-core-implementation.html) blog post by Roman Leventov

[Twitter Scala School: Finagle](https://twitter.github.io/scala_school/finagle.html)

### Dependencies

The code use Scala 2.11.8 on the JVM.  I run it on Java 1.8.  I've used other Web frameworks in Scala, but decided to try version 2.1 of Twitter Finatra.  This was a first for me.  It is an HTTP framework similar to Ruby's Sinatra, and Scala's Akka Http (previously Spray).

Twitter Finatra is implemented on top of the Scala-based HTTP server, Twitter Server, and uses the Twitter Finagle library.  I used the Redis client (and server) available from Finagle 6.34.  I connected to Redis 2.8.19 installed on my machine.

Finatra is opinionated about how dependency injection should work and recommends using Google Guice 4.0.  It helped to bootstrap integration tests of the Finatra code.

I choose to write property tests using Specs2 and Scalacheck, but the Web integration tests for Finatra suggest Scalatest.  The two different test suites cooperate very well when they live side-by-side in a project.

The simple build tool, SBT version 0.13.8, is used to launch compile the code, launch the Web server, run tests and manage dependencies.  I even wrote an SBT task for creating a randomly-generated file.  I used two SBT plugins, scalariform and revolver.  Scalariform automatically formats Scala source code to convention.  Revolver is a plugin that can launch your application in a background JVM, while supporting SBT's triggered execution.  Triggered execution is a feature where SBT notices source files change, and can automatically re-run compilation and even the application.

An early implementation used Apache Spark 3.6 to store the file.  Spark standalone cluster on the JVM was extremely adept at loading the data set quickly.  Retrieving a single record by an index is not a use case, however.

### TODO

1. Deploy it to continuous integration in a PaaS like Heroku.
2. Configure Redis server connection with a command-line flag.
3. Determine the steps to build a (large) file.
4. Determine how to load a (large) file in production.
5. Try to load a (large) file to a cluster of Redis servers.
6. Try reading from a cluster of Redis servers.
7. The creating of a large file is done in constant memory, slurping the file in to Redis should be as well.
8. Write more unit tests that capture the specification

### FIXME

1. The use of dependency injection is effective, but could the system be written in such a way that any line retrieving service could be swapped in or out with the Web service?
2. Good use of unit tests, but how much confidence is in the automated tests about the complete application?  Should there be more error cases and use cases?
3. Further, does the testing produce good code coverage statistics?
4. Could the file be loaded using parallelization to go faster?
5. Could you use `twitter.util.Bijection` to implicitly convert between a string and a Redis channel buffer?
5. Could you convert the twitter.util.Futures to Scala futures in tests to use fewer `Await.result()`, and leverage Specs2 syntactic sugar for Scala futures?
