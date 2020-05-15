Help()
{
   # Display Help
   echo "Please provide argument"
   echo
   echo "Syntax: llm [start|stop|docker|dir|log]"
   echo
   echo "Options:"
   echo "start     Run Middleware using doccker compose"
   echo "stop      Stopp all running docker cocntainers."
   echo "docker    Open docker-compose.yml file in Sublime text"
   echo "dir       Change directory to the LLM directory"
   echo "log       Change directory to the LLM logs directory"
   echo
}


[ "$#" -eq 1 ] || Help

if [ "$1" = "start" ]; then
    echo "Starting Middleware"
    docker-compose -f /opt/llm/docker-compose.yml up -d
    docker ps --format "{{.Names}}\t\t{{.Status}}\t\t{{.Ports}}"
    exit 0
fi

if [ "$1" = "stop" ]; then
    echo "Stopping Middleware"
    dip=$(docker ps -q)
    if [ "$dip" = "" ]; then
       echo "Notging running"
       exit 0
    fi
    docker stop LLM.Grafana
    docker stop LLM.Prometheus
    docker stop LLM.Frontend
    docker stop LLM.Server
    docker stop LLM.Adminer
    docker stop LLM.LLM.MongoExpress
    docker stop LLM.Mongo
    docker stop LLM.Postgres
    exit 0
fi

if [ "$1" = "docker" ]; then
    subl /opt/llm/docker-compose.yml
    echo "File docker-compose.yml opened"
    exit 0
fi

if [ "$1" = "dir" ]; then
    cd /opt/llm/
    echo "Moved to LLM directory"
    exec bash
    exit 0
fi

if [ "$1" = "log" ]; then
    cd /val/log/llm/
    echo "Moved to logs directory"
    exec bash
    exit 0
fi

echo "Invalid argument!"