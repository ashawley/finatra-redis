#!/bin/bash

set -x

sbt "run -file='${1}'"
