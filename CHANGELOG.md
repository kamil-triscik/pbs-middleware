# Changelog
All notable changes to this project will be documented in this file.

## [0.0.38] - 2020-04-11
    - project package changed
      - `cz.muni.ll.middleware.server.*` -> `com.pbs.middleware.server`
    - changed app props prefix
      - `cz.muni.ll.middleware.server.*` -> `middleware.server.*`
    - updated `MongoConfig`

## [0.0.37] - 2020-04-10
    - added
       - ErroCodes(Common, Download, Upload, Job)
       - ConnectionHealthCheck
       - Job
           - added class `JobRestartRequest` for restarting job
           - added class `KillJobRequest` for killing job
           - added class `JobEmailFactory`, `JobEmailParams` + job freemarker template
           - added EmailRepository
              - flyway script #16
              - `CrudRepository`
              - `EmailEntity` class
              - `EmailService` class
           - added events
              - `JobLauncher` abstract class
              - `JobFailed` - in case job is killed
              - `jobRestart` - in case someone ask for restart
              - `JobMoved` - for case, job is noved in PBS
              - `JobNotificationRequest` - for case, someone want send job related notification
              - `StartAutomatedJobAction` - foc case, job is in unexpected state(HELD OR FINISHED with Exit code != 0)
           - added Groovy execution logic
              - `JobOutput` - for stdout and stderr from meta
              - `JobServiceProvider` - container binded to the groovy script proving services
              - `JobConfigurationContainer` - container handling jov config
              - `JobContainer` - container binded to the groovy script proving data about job
              - `CustomScriptHandler` - component preparing and executing groovy script
              - `DefaultScriptHandler` - used in case, no groovy script is defined
            - added listeners
              - `AutomatedActionRequestListener` - job event listener for groovy script reuqests
              - `JobKillingListener` - listener for killing job
              - `JobNotificationListener` - for processing notfication requests
              - `JobRestartListener` - for processing restart requests
              - `JobRestartedListener` - for processing logi after job restart
        - script
            - added logic for working groovy template
    - updated
       - removed eventWrapper
       - exception handling using errorCodes
       - deprecated MediaType APPLICATION_JSON_UTF8_VALUE replaced with APPLICATION_JSON_VALUE
       - Job
           - JobsController
              - using static paths from ApiConfig
              - REST method `/{id}/details` maked as  deprecated, `/{id}` should be used for fetching job state
              - added `/{id}/restart` and `/{id}/kill` REST methods
              - added swagger doc to DTO objects
           -
            - updated `JobsStatusTester`
            - `JobDto` - aded fields
            - `QstatResponse` - added fields + deserializer update
       - `TemplateProperties` - replaced with `JobConfiguration` class 
             
           
## [0.0.36] - 2020-03-30
    - updated
       - #43 Upload will create destination directory first
       - #46 Do not send email of not set (Upload and Download) 
       
## [0.0.35] - 2020-03-30
    - updated
       - fixed #42 upload fail cause infinite loop - fixed by removing unnecessary ID from UploadFailed
       
## [0.0.34] - 2020-03-30
    - updated
       - Removed `@NonNull` as template could be null when submitting job
       
## [0.0.33] - 2020-03-29
    - updated
       - fixed #40 problem with download event processing. Processing continue in new thread after processing of of first event.
       - used `ConcurrentLinkedQueue` to `solve ConcurrentModificationException`
       - fixed #41 by change in `JschScp`
       - adde Download state mapping for client
       - Download state FETCHED changed to READY

## [0.0.32] - 2020-03-28
    updated
      - `application.properties` - added PSQL database platform
      - `pom.xml` - updated dependencies versions
      - `Dockerfile` - added build stage
      - `dockerbuild.sh` - `compile` instead `install`
      - `JobsMonitor` - temporary turned off `ForkJoinPool` does to issue during loading data from DB
      - added `@Document` annotation to all event classes 

## [0.0.31] - 2020-02-03
    added
      - prometheus
    updated
      - dcoker compose example + prometheus conf example

## [0.0.30] - 2020-02-25
    added
      - delete method for connection, script, job, upload, download
      
## [0.0.29] - 2020-02-18
    added
      - global upload state
      - chunk remove with by 0 count
      
## [0.0.28] - 2019-12-23
    added
      - handling MethodArgumentNotValidException
    fixed
      - resources mapper 

## [0.0.27] - 2019-12-23
    added
      - events moved to the mongo DB

## [0.0.26] - 2019-12-16
    added
      - scp/shell support ssh key

## [0.0.25] - 2019-11-30
    added
      - validations
      - fe hotfixes
      
## [0.0.24] - 2019-11-30
    added
      - upload is able to process also connection name
      - small UploadDto improvements
    
## [0.0.23] - 2019-11-26
    added
      - ConstraintViolationException(validation error) handling
      - added SubmitJobRequest DTO
      - all pbs stuff moved to the common package
      - selector replaced with resources
      - created new template properties merger
      - few custom validators - will be finished/used later
      - added copy and delete logic
      
## [0.0.22] - 2019-11-23
    - fixed Optional
    - template now allow adding both, uuid and name of script and connection
    - created JobService
    - Created JobListenersFactory
    - JobState eum rename to State
    - JobStateInfo renamed to the JobState
    - JobCreateXXX renamed to JobSubmitXXX
    - Configuration_template renamed to the job_template
    - clinet now see only 2 job state FINISHED when job is FINISHEd otherwise RUNNING
    - Added empty qsub response(jobId) handling
    - packges chnages
      
## [0.0.21] - 2019-11-22
    Added UploadLogginListener
    
## [0.0.20] - 2019-11-22
    Fixed missing exit status in qstat response
    
## [0.0.19] - 2019-11-17
    Added:
     - ownership handling
     - remmebering newest qstat
     - api for group job statuses
     - many improvements

## [0.0.18] - 2019-11-09
    Download:
      - added new API methods
      - added download error handling
      - 2 new tables + 2 new domain objects, 2 repositories, 1 service
      - download job for redownloading failed downloads, notifying admins, ending failed download
      - added/updated email templates
          
    Other:
      - SCP download finished
      - upload small fixes
      - updated logging listener
      - updated PMD config
      - small fixes
        
## [0.0.17] - 2019-11-03
    Upload:
      - added new API methods
      - added upload error handling
      - 2 new tables + 2 new domain objects, 2 repositories, 1 service
      - upload job for reuploading failed uploads, notifying admins, ending failed uploads
      - added/updated email templates
      - refactored upload email factory
    
    Other:
      - ContactService - provide admin emails
      - default values in props
        
## [0.0.16] - 2019-10-26
    - simple contact feature(api, service, DB)
    - enable to create contact for specific situation(problem in APP/upload/download/job)
    - can be used as simple email handler
    
## [0.0.15] - 2019-10-19
    - Email service
    - email templates for upload

## [0.0.14] - 2019-10-18
    - CORS origin configurable via app properties

## [0.0.13] - 2019-10-11
    - custom Optional class
    - PBSPAths - added after and beffore script  + following necessary changes
    - removed SshConnecction
    - event sourcing facories now return optional objects
    - added NOT_STARTED job state
    - Event Error handling fixes
    - pbs command fixes
    - PasswordShell now support custom port if it's part of sshHost
    - Template - added description
                
## [0.0.12] - 2019-09-05
    - Docker updates
    
## [0.0.11] - 2019-09-03
    - so many changes OMG
    - necessary to fix/finish PBS
    - working job launch and monitoring
    
## [0.0.10] - 2019-08-21
### Added
    - PBS builder + additional changes
    
## [0.0.9] - 2019-08-16
### Changed
  - TemplateService - PBSServer, User, Script replaced with Connection and Script UUID
  
## [0.0.8] - 2019-08-16
### Added
  - _Scripts_ table
  - _Scripts_ repository, service, api
  - _Scripts_ swagger API
### Changed
  - updated V3 migration script :o

## [0.0.7] - 2019-08-16
### Added
  - SSHShell stuff - working local shell + ssh with password, able to run command
  - SSH SCP - working upload with password
  
## [0.0.6] - 2019-08-15
### Changed
  - connection changes based on meeting result
  
## [0.0.5] - 2019-07-31
### Added
  - _Connections_ table
  - _Connections_ repository, service, api
  - _Connections_ swagger API
### Changed
  - updated bitbucket-pipelines

## [0.0.4] - 2019-07-30
### Added
 - _Jobs_ table
 - _Jobs_ functionality(Factory, Controller, ...) - not finished
 - _Jobs_ swagger API
### Removed
 - _Customer_ stuff

## [0.0.3] - 2019-07-27
### Changed
  - updated bitbucket-pipelines

## [0.0.2] - 2019-07-26
### Added
 - hibernate validations
 - logger in template service
### Changed
 - **template update** - only one event for update
 - **template DB table** - primary key consist of _domain_id_ and _event_uuid_, updated flyway init script

## [0.0.1] - 2019-07-22
### Added
 - enabled actuator
 - enabled health, info, metrics, env actuator
 - `JobsStatusInfoContributor`
 - added git plugin
 - changelog
### Changed
 - updated mvn versions
        
