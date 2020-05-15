#!/bin/bash
die () {
    echo >&2 "$@"
    exit 1
}

[ "$#" -eq 1 ] || die "Docker tag expected as argument"
mvn -pl '!pbs-middleware-it' compile
#docker build -t llmiddleware/llm_server:$1 -t llmiddleware/llm_server:latest .
docker build -t llmiddleware/llm_server:$1 .
docker push llmiddleware/llm_server:$1
#docker push llmiddleware/llm_server:latest
