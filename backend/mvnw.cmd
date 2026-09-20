@ECHO OFF
IF EXIST "C:\Users\dhanu\.m2\wrapper\dists\apache-maven-3.9.6-bin\3311e1d4\apache-maven-3.9.6\bin\mvn.cmd" (
    CALL "C:\Users\dhanu\.m2\wrapper\dists\apache-maven-3.9.6-bin\3311e1d4\apache-maven-3.9.6\bin\mvn.cmd" %*
) ELSE (
    mvn %*
)
