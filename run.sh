#!/bin/bash

if [ -z "${1}" ]; then
    echo "No file specified" >&2
    echo -e "Usage:\n\t./run.sh FILE" >&2
    exit 1
elif [ ! -f "${1}" ]; then
    echo "File ${1} does not exist"
    exit 1
fi

echo Starting server...
echo ^C to exit

set -x

sbt "run -file=${1}"
