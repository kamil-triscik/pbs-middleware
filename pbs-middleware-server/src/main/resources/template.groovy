//job
//qstat
//errorCode

<REPLACE>

def isFirst() {
    println "isFirst"
    return job.restarts == 0
}

def isSecond() {
    println "isSecond"
    return job.restarts == 1
}