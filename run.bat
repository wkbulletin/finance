echo off
set MJS_HOME=%~dp0
java -Djava.ext.dirs=%MJS_HOME%\WebContent\WEB-INF\lib -cp %MJS_HOME%\WebContent\WEB-INF\classes webserver.WebServerRun %*