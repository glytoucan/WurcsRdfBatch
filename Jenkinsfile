REPOSITORY="WurcsRdfBatch"
ARTIFACTID="wurcsrdf"
PROJECT="docker-motif-build"
WORKSPACE_PARENT="/srv/jenkins"
WORKSPACE="workspace"
PROJECT_PATH=WORKSPACE_PARENT + "/" + WORKSPACE + "/" + PROJECT
RELEASE_VERSION="0.0.1-SNAPSHOT"

node {
  stage PROJECT + 'version ' + env.BATCH_VERSION + ' start'

  stage 'git clone'
  git 'https://github.com/glytoucan/' + REPOSITORY + '.git'

  stage 'build jar'
  docker.image('maven:3.3.3-jdk-8').inside {
    writeFile file: 'settings.xml', text: "<settings><localRepository>${pwd()}/.m2repo</localRepository></settings>"
    sh 'mvn -DskipTests=true -B -s settings.xml clean package'
  }

//   stage 'package jar'
//   def c = docker.image('aokinobu/java-release:0.0.1-SNAPSHOT').run('-e PROJECT_FOLDER=target/ -e PROJECT_FILE=' + ARTIFACTID + '- -e VERSION_NUMBER=' + env.BATCH_VERSION + ' -e EXTENSION=.jar -v /var/jenkins_home/workspace/' + WORKSPACE + ':/workspace --workdir=/workspace', 'ls -al /workspace');
//   sh 'echo ' + pwd()
//   sh 'ls -al ' + pwd()
//   sh 'ls -al /' + WORKSPACE_PARENT + '/' + WORKSPACE + '/' + PROJECT

//   sh 'docker run --rm -v ' + WORKSPACE_PARENT + '/' + WORKSPACE + '/' + PROJECT + ':/workspace debian:jessie ls -al /workspace'
//   sh 'docker run --rm -v /var/jenkins_home/' + WORKSPACE + '/' + PROJECT + ':/workspace debian:jessie ls -al /workspace'
//   sh 'echo WORKSPACE_PARENT=' + WORKSPACE_PARENT + ' WORKSPACE=' + WORKSPACE + ' PROJECT=' + PROJECT + ' docker-compose -f docker-compose.build.yml up'
//   sh 'WORKSPACE_PARENT=' + WORKSPACE_PARENT + ' WORKSPACE=' + WORKSPACE + ' PROJECT=' + PROJECT + ' docker-compose -f docker-compose.build.yml rm -f'
//   sh 'WORKSPACE_PARENT=' + WORKSPACE_PARENT + ' WORKSPACE=' + WORKSPACE + ' PROJECT=' + PROJECT + ' docker-compose -f docker-compose.build.yml up'
  
//   sh 'ls -al /var/jenkins_home/workspace/' + PROJECT

//   sh 'docker logs ' + c.id

//   sh 'docker run --rm -e PROJECT_FOLDER=target/ -e PROJECT_FILE=' + ARTIFACTID + '- -e VERSION_NUMBER=' + env.BATCH_VERSION + ' -e EXTENSION=.jar -v /var/jenkins_home/workspace/' + WORKSPACE + ':/workspace --workdir=/workspace aokinobu/java-release:0.0.1-SNAPSHOT ls -al /workspace/target'

//   sh 'docker stop ' + ARTIFACTID + '_container'
//   sh 'docker rm ' + ARTIFACTID + '_container'
//   sh 'docker run -e PROJECT_FOLDER=/workspace/target/ -e PROJECT_FILE=' + ARTIFACTID + '- -e VERSION_NUMBER=' + env.BATCH_VERSION + ' -e EXTENSION=.jar -v /var/jenkins_home/workspace/' + WORKSPACE + ':/workspace --name=' + ARTIFACTID + '_container aokinobu/java-release:0.0.1-SNAPSHOT sh /run.sh'
  
//   def d = docker.image('debian:jessie').run('', 'ls -al /var/jenkins_home/workspace/docker-motif-build')
//   sh 'docker logs ' + d.id
//   docker.image('aokinobu/java-release').pull()
  def c = docker.image('aokinobu/java-release:' + RELEASE_VERSION).run('-e PROJECT_FOLDER=target/ -e PROJECT_FILE=' + ARTIFACTID + '- -e VERSION_NUMBER=' + env.BATCH_VERSION + ' -e EXTENSION=.jar --workdir=/workspace -v ' + PROJECT_PATH + ':/workspace', '/run.sh')
//   docker.image('aokinobu/java-release:' + RELEASE_VERSION).inside('-e PROJECT_FOLDER=target/ -e PROJECT_FILE=' + ARTIFACTID + '- -e VERSION_NUMBER=' + env.BATCH_VERSION + ' -e EXTENSION=.jar') {
//      c -> 
//      sh '/run.sh'
//   }
//   sh 'docker commit ${c.id} ' + ARTIFACTID
  
//  def count = 0;
//  waitUntil {
//    sh 'docker inspect -f {{.State.Running}} ' + c.id
//  }
  
  stage 'docker commit'

  sh 'docker commit ' + c.id + ' ' + ARTIFACTID

  stage 'docker push'
  docker.withRegistry('http://glycoinfo.org:5000') {
    docker.image(ARTIFACTID).push(env.BATCH_VERSION)
  }

  stage 'docker stop container'
  sh 'docker stop ' + c.id
  
  stage 'docker rm container'
  sh 'docker rm ' + c.id
}
